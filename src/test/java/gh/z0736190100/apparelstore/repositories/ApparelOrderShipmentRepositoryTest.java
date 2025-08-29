package gh.z0736190100.apparelstore.repositories;

import gh.z0736190100.apparelstore.entities.ApparelOrder;
import gh.z0736190100.apparelstore.entities.ApparelOrderShipment;
import gh.z0736190100.apparelstore.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for ApparelOrderShipmentRepository
 */
@DataJpaTest
class ApparelOrderShipmentRepositoryTest {

    @Autowired
    ApparelOrderShipmentRepository apparelOrderShipmentRepository;

    @Autowired
    ApparelOrderRepository apparelOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    private ApparelOrder testApparelOrder;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
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
    void testSaveApparelOrderShipment() {
        // Given
        ApparelOrderShipment shipment = ApparelOrderShipment.builder()
                .shipmentDate(LocalDateTime.now())
                .carrier("FedEx")
                .trackingNumber("123456789")
                .build();
        testApparelOrder.addShipment(shipment);

        // When
        ApparelOrderShipment savedShipment = apparelOrderShipmentRepository.save(shipment);

        // Then
        assertThat(savedShipment).isNotNull();
        assertThat(savedShipment.getId()).isNotNull();
        assertThat(savedShipment.getApparelOrder().getId()).isEqualTo(testApparelOrder.getId());
        assertThat(savedShipment.getCarrier()).isEqualTo("FedEx");
        assertThat(savedShipment.getTrackingNumber()).isEqualTo("123456789");
    }

    @Test
    void testGetApparelOrderShipmentById() {
        // Given
        ApparelOrderShipment shipment = ApparelOrderShipment.builder()
                .shipmentDate(LocalDateTime.now())
                .carrier("UPS")
                .trackingNumber("987654321")
                .build();
        testApparelOrder.addShipment(shipment);
        ApparelOrderShipment savedShipment = apparelOrderShipmentRepository.save(shipment);

        // When
        Optional<ApparelOrderShipment> fetchedShipmentOptional = apparelOrderShipmentRepository.findById(savedShipment.getId());

        // Then
        assertThat(fetchedShipmentOptional).isPresent();
        ApparelOrderShipment fetchedShipment = fetchedShipmentOptional.get();
        assertThat(fetchedShipment.getApparelOrder()).isNotNull();
        assertThat(fetchedShipment.getApparelOrder().getId()).isEqualTo(testApparelOrder.getId());
        assertThat(fetchedShipment.getCarrier()).isEqualTo("UPS");
        assertThat(fetchedShipment.getTrackingNumber()).isEqualTo("987654321");
    }

    @Test
    void testUpdateApparelOrderShipment() {
        // Given
        ApparelOrderShipment shipment = ApparelOrderShipment.builder()
                .shipmentDate(LocalDateTime.now())
                .carrier("DHL")
                .trackingNumber("ABCDEF123")
                .build();
        testApparelOrder.addShipment(shipment);
        ApparelOrderShipment savedShipment = apparelOrderShipmentRepository.save(shipment);

        // When
        savedShipment.setCarrier("USPS");
        savedShipment.setTrackingNumber("UPDATED123");
        ApparelOrderShipment updatedShipment = apparelOrderShipmentRepository.save(savedShipment);

        // Then
        assertThat(updatedShipment.getCarrier()).isEqualTo("USPS");
        assertThat(updatedShipment.getTrackingNumber()).isEqualTo("UPDATED123");
    }

    @Test
    void testDeleteApparelOrderShipment() {
        // Given
        ApparelOrderShipment shipment = ApparelOrderShipment.builder()
                .shipmentDate(LocalDateTime.now())
                .carrier("Amazon")
                .trackingNumber("AMAZON123")
                .build();
        testApparelOrder.addShipment(shipment);
        ApparelOrderShipment savedShipment = apparelOrderShipmentRepository.save(shipment);

        // When
        apparelOrderShipmentRepository.deleteById(savedShipment.getId());
        Optional<ApparelOrderShipment> deletedShipment = apparelOrderShipmentRepository.findById(savedShipment.getId());

        // Then
        assertThat(deletedShipment).isEmpty();
    }

    @Test
    void testFindByApparelOrderId() {
        // Given
        apparelOrderShipmentRepository.deleteAll(); // Clear any existing data

        ApparelOrderShipment shipment1 = ApparelOrderShipment.builder()
                .shipmentDate(LocalDateTime.now())
                .carrier("FedEx")
                .trackingNumber("FEDEX123")
                .build();
        testApparelOrder.addShipment(shipment1);
        apparelOrderShipmentRepository.save(shipment1);

        ApparelOrderShipment shipment2 = ApparelOrderShipment.builder()
                .shipmentDate(LocalDateTime.now().plusDays(1))
                .carrier("UPS")
                .trackingNumber("UPS456")
                .build();
        testApparelOrder.addShipment(shipment2);
        apparelOrderShipmentRepository.save(shipment2);

        // Create another apparel order with a shipment
        ApparelOrder anotherApparelOrder = ApparelOrder.builder()
                .customer(testCustomer)
                .paymentAmount(new BigDecimal("50.00"))
                .status("NEW")
                .build();
        anotherApparelOrder = apparelOrderRepository.save(anotherApparelOrder);

        ApparelOrderShipment shipment3 = ApparelOrderShipment.builder()
                .shipmentDate(LocalDateTime.now())
                .carrier("DHL")
                .trackingNumber("DHL789")
                .build();
        anotherApparelOrder.addShipment(shipment3);
        apparelOrderShipmentRepository.save(shipment3);

        // When
        List<ApparelOrderShipment> shipments = apparelOrderShipmentRepository.findByApparelOrderId(testApparelOrder.getId());

        // Then
        assertThat(shipments).hasSize(2);
        assertThat(shipments.get(0).getApparelOrder().getId()).isEqualTo(testApparelOrder.getId());
        assertThat(shipments.get(1).getApparelOrder().getId()).isEqualTo(testApparelOrder.getId());
    }
}