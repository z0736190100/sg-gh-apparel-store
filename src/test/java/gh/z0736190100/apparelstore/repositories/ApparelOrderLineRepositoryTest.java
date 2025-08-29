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
class ApparelOrderLineRepositoryTest {

    @Autowired
    ApparelOrderLineRepository apparelOrderLineRepository;

    @Autowired
    ApparelOrderRepository apparelOrderRepository;

    @Autowired
    ApparelRepository apparelRepository;

    @Autowired
    CustomerRepository customerRepository;

    private Apparel testApparel;
    private ApparelOrder testApparelOrder;
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

        // Create and save a test apparel order
        testApparelOrder = ApparelOrder.builder()
                .customer(testCustomer)
                .paymentAmount(new BigDecimal("25.98"))
                .status("NEW")
                .build();
        testApparelOrder = apparelOrderRepository.save(testApparelOrder);
    }

    @Test
    void testSaveApparelOrderLine() {
        // Given
        ApparelOrderLine apparelOrderLine = ApparelOrderLine.builder()
                .orderQuantity(2)
                .quantityAllocated(0)
                .status("NEW")
                .apparel(testApparel)
                .apparelOrder(testApparelOrder)
                .build();

        // When
        ApparelOrderLine savedApparelOrderLine = apparelOrderLineRepository.save(apparelOrderLine);

        // Then
        assertThat(savedApparelOrderLine).isNotNull();
        assertThat(savedApparelOrderLine.getId()).isNotNull();
        assertThat(savedApparelOrderLine.getApparel().getId()).isEqualTo(testApparel.getId());
        assertThat(savedApparelOrderLine.getApparelOrder().getId()).isEqualTo(testApparelOrder.getId());
    }

    @Test
    void testGetApparelOrderLineById() {
        // Given
        ApparelOrderLine apparelOrderLine = ApparelOrderLine.builder()
                .orderQuantity(2)
                .quantityAllocated(0)
                .status("NEW")
                .apparel(testApparel)
                .apparelOrder(testApparelOrder)
                .build();
        ApparelOrderLine savedApparelOrderLine = apparelOrderLineRepository.save(apparelOrderLine);

        // When
        Optional<ApparelOrderLine> fetchedApparelOrderLineOptional = apparelOrderLineRepository.findById(savedApparelOrderLine.getId());

        // Then
        assertThat(fetchedApparelOrderLineOptional).isPresent();
        ApparelOrderLine fetchedApparelOrderLine = fetchedApparelOrderLineOptional.get();
        assertThat(fetchedApparelOrderLine.getOrderQuantity()).isEqualTo(2);
        assertThat(fetchedApparelOrderLine.getApparel().getId()).isEqualTo(testApparel.getId());
        assertThat(fetchedApparelOrderLine.getApparelOrder().getId()).isEqualTo(testApparelOrder.getId());
    }

    @Test
    void testUpdateApparelOrderLine() {
        // Given
        ApparelOrderLine apparelOrderLine = ApparelOrderLine.builder()
                .orderQuantity(2)
                .quantityAllocated(0)
                .status("NEW")
                .apparel(testApparel)
                .apparelOrder(testApparelOrder)
                .build();
        ApparelOrderLine savedApparelOrderLine = apparelOrderLineRepository.save(apparelOrderLine);

        // When
        savedApparelOrderLine.setOrderQuantity(3);
        savedApparelOrderLine.setQuantityAllocated(3);
        savedApparelOrderLine.setStatus("ALLOCATED");
        ApparelOrderLine updatedApparelOrderLine = apparelOrderLineRepository.save(savedApparelOrderLine);

        // Then
        assertThat(updatedApparelOrderLine.getOrderQuantity()).isEqualTo(3);
        assertThat(updatedApparelOrderLine.getQuantityAllocated()).isEqualTo(3);
        assertThat(updatedApparelOrderLine.getStatus()).isEqualTo("ALLOCATED");
    }

    @Test
    void testDeleteApparelOrderLine() {
        // Given
        ApparelOrderLine apparelOrderLine = ApparelOrderLine.builder()
                .orderQuantity(2)
                .quantityAllocated(0)
                .status("NEW")
                .apparel(testApparel)
                .apparelOrder(testApparelOrder)
                .build();
        ApparelOrderLine savedApparelOrderLine = apparelOrderLineRepository.save(apparelOrderLine);

        // When
        apparelOrderLineRepository.deleteById(savedApparelOrderLine.getId());
        Optional<ApparelOrderLine> deletedApparelOrderLine = apparelOrderLineRepository.findById(savedApparelOrderLine.getId());

        // Then
        assertThat(deletedApparelOrderLine).isEmpty();
    }

    @Test
    void testListApparelOrderLines() {
        // Given
        apparelOrderLineRepository.deleteAll(); // Clear any existing data

        ApparelOrderLine apparelOrderLine1 = ApparelOrderLine.builder()
                .orderQuantity(2)
                .quantityAllocated(0)
                .status("NEW")
                .apparel(testApparel)
                .apparelOrder(testApparelOrder)
                .build();

        ApparelOrderLine apparelOrderLine2 = ApparelOrderLine.builder()
                .orderQuantity(3)
                .quantityAllocated(0)
                .status("NEW")
                .apparel(testApparel)
                .apparelOrder(testApparelOrder)
                .build();

        apparelOrderLineRepository.saveAll(List.of(apparelOrderLine1, apparelOrderLine2));

        // When
        List<ApparelOrderLine> apparelOrderLines = apparelOrderLineRepository.findAll();

        // Then
        assertThat(apparelOrderLines).hasSize(2);
    }
}
