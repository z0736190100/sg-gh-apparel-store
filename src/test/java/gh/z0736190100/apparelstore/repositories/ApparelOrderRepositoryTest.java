package gh.z0736190100.apparelstore.repositories;

import gh.z0736190100.apparelstore.entities.Apparel;
import gh.z0736190100.apparelstore.entities.ApparelOrder;
import gh.z0736190100.apparelstore.entities.ApparelOrderLine;
import gh.z0736190100.apparelstore.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ApparelOrderRepositoryTest {

    @Autowired
    ApparelOrderRepository apparelOrderRepository;

    @Autowired
    ApparelRepository apparelRepository;

    @Autowired
    CustomerRepository customerRepository;

    private Apparel testApparel;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        // Create and save a test apparel
        testApparel = Apparel.builder()
                .apparelName("Test Apparel")
                .apparelStyle("Loose")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        testApparel = apparelRepository.save(testApparel);

        // Create and save a test customer
        testCustomer = Customer.builder()
                .name("Test Customer")
                .email("test@example.com")
                .phoneNumber("555-123-4567")
                .addressLine1("123 Main St")
                .city("Springfield")
                .state("IL")
                .postalCode("62701")
                .build();
        testCustomer = customerRepository.save(testCustomer);
    }

    @Test
    void testSaveApparelOrder() {
        // Given
        ApparelOrder apparelOrder = ApparelOrder.builder()
                .customer(testCustomer)
                .paymentAmount(new BigDecimal("25.98"))
                .status("NEW")
                .build();

        ApparelOrderLine apparelOrderLine = ApparelOrderLine.builder()
                .orderQuantity(2)
                .quantityAllocated(0)
                .status("NEW")
                .apparel(testApparel)
                .build();

        apparelOrder.addApparelOrderLine(apparelOrderLine);

        // When
        ApparelOrder savedApparelOrder = apparelOrderRepository.save(apparelOrder);

        // Then
        assertThat(savedApparelOrder).isNotNull();
        assertThat(savedApparelOrder.getId()).isNotNull();
        assertThat(savedApparelOrder.getApparelOrderLines()).hasSize(1);
        assertThat(savedApparelOrder.getApparelOrderLines().iterator().next().getApparel().getId()).isEqualTo(testApparel.getId());
    }

    @Test
    void testGetApparelOrderById() {
        // Given
        ApparelOrder apparelOrder = ApparelOrder.builder()
                .customer(testCustomer)
                .paymentAmount(new BigDecimal("25.98"))
                .status("NEW")
                .build();

        ApparelOrderLine apparelOrderLine = ApparelOrderLine.builder()
                .orderQuantity(2)
                .quantityAllocated(0)
                .status("NEW")
                .apparel(testApparel)
                .build();

        apparelOrder.addApparelOrderLine(apparelOrderLine);
        ApparelOrder savedApparelOrder = apparelOrderRepository.save(apparelOrder);

        // When
        Optional<ApparelOrder> fetchedApparelOrderOptional = apparelOrderRepository.findById(savedApparelOrder.getId());

        // Then
        assertThat(fetchedApparelOrderOptional).isPresent();
        ApparelOrder fetchedApparelOrder = fetchedApparelOrderOptional.get();
        assertThat(fetchedApparelOrder.getCustomer()).isNotNull();
        assertThat(fetchedApparelOrder.getCustomer().getName()).isEqualTo(testCustomer.getName());
        assertThat(fetchedApparelOrder.getApparelOrderLines()).hasSize(1);
    }

    @Test
    void testUpdateApparelOrder() {
        // Given
        ApparelOrder apparelOrder = ApparelOrder.builder()
                .customer(testCustomer)
                .paymentAmount(new BigDecimal("25.98"))
                .status("NEW")
                .build();

        ApparelOrderLine apparelOrderLine = ApparelOrderLine.builder()
                .orderQuantity(2)
                .quantityAllocated(0)
                .status("NEW")
                .apparel(testApparel)
                .build();

        apparelOrder.addApparelOrderLine(apparelOrderLine);
        ApparelOrder savedApparelOrder = apparelOrderRepository.save(apparelOrder);

        // When
        // Create a new customer for the update
        Customer updatedCustomer = Customer.builder()
                .name("Updated Customer")
                .email("updated@example.com")
                .phoneNumber("555-987-6543")
                .addressLine1("456 Oak Ave")
                .city("Shelbyville")
                .state("IL")
                .postalCode("62565")
                .build();
        updatedCustomer = customerRepository.save(updatedCustomer);

        savedApparelOrder.setCustomer(updatedCustomer);
        savedApparelOrder.setStatus("PROCESSING");
        ApparelOrder updatedApparelOrder = apparelOrderRepository.save(savedApparelOrder);

        // Then
        assertThat(updatedApparelOrder.getCustomer()).isNotNull();
        assertThat(updatedApparelOrder.getCustomer().getName()).isEqualTo("Updated Customer");
        assertThat(updatedApparelOrder.getStatus()).isEqualTo("PROCESSING");
    }

    @Test
    void testDeleteApparelOrder() {
        // Given
        ApparelOrder apparelOrder = ApparelOrder.builder()
                .customer(testCustomer)
                .paymentAmount(new BigDecimal("25.98"))
                .status("NEW")
                .build();

        ApparelOrderLine apparelOrderLine = ApparelOrderLine.builder()
                .orderQuantity(2)
                .quantityAllocated(0)
                .status("NEW")
                .apparel(testApparel)
                .build();

        apparelOrder.addApparelOrderLine(apparelOrderLine);
        ApparelOrder savedApparelOrder = apparelOrderRepository.save(apparelOrder);

        // When
        apparelOrderRepository.deleteById(savedApparelOrder.getId());
        Optional<ApparelOrder> deletedApparelOrder = apparelOrderRepository.findById(savedApparelOrder.getId());

        // Then
        assertThat(deletedApparelOrder).isEmpty();
    }

    @Test
    void testListApparelOrders() {
        // Given
        apparelOrderRepository.deleteAll(); // Clear any existing data
        customerRepository.deleteAll(); // Clear any existing customers

        // Create two test customers
        Customer customer1 = Customer.builder()
                .name("Customer 1")
                .email("customer1@example.com")
                .phoneNumber("555-111-1111")
                .addressLine1("111 First St")
                .city("Springfield")
                .state("IL")
                .postalCode("62701")
                .build();
        customer1 = customerRepository.save(customer1);

        Customer customer2 = Customer.builder()
                .name("Customer 2")
                .email("customer2@example.com")
                .phoneNumber("555-222-2222")
                .addressLine1("222 Second St")
                .city("Shelbyville")
                .state("IL")
                .postalCode("62565")
                .build();
        customer2 = customerRepository.save(customer2);

        ApparelOrder apparelOrder1 = ApparelOrder.builder()
                .customer(customer1)
                .paymentAmount(new BigDecimal("25.98"))
                .status("NEW")
                .build();

        ApparelOrderLine apparelOrderLine1 = ApparelOrderLine.builder()
                .orderQuantity(2)
                .quantityAllocated(0)
                .status("NEW")
                .apparel(testApparel)
                .build();

        apparelOrder1.addApparelOrderLine(apparelOrderLine1);

        ApparelOrder apparelOrder2 = ApparelOrder.builder()
                .customer(customer2)
                .paymentAmount(new BigDecimal("39.97"))
                .status("PROCESSING")
                .build();

        ApparelOrderLine apparelOrderLine2 = ApparelOrderLine.builder()
                .orderQuantity(3)
                .quantityAllocated(0)
                .status("NEW")
                .apparel(testApparel)
                .build();

        apparelOrder2.addApparelOrderLine(apparelOrderLine2);

        apparelOrderRepository.saveAll(List.of(apparelOrder1, apparelOrder2));

        // When
        List<ApparelOrder> apparelOrders = apparelOrderRepository.findAll();

        // Then
        assertThat(apparelOrders).hasSize(2);
    }
}
