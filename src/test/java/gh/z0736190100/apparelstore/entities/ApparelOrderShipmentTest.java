package gh.z0736190100.apparelstore.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for ApparelOrderShipment entity
 */
class ApparelOrderShipmentTest {

    @Test
    void testApparelOrderShipmentProperties() {
        // Create a apparel order shipment
        LocalDateTime shipmentDate = LocalDateTime.now();
        ApparelOrderShipment shipment = ApparelOrderShipment.builder()
                .shipmentDate(shipmentDate)
                .carrier("FedEx")
                .trackingNumber("123456789")
                .build();

        // Verify properties
        assertThat(shipment).isNotNull();
        assertThat(shipment.getShipmentDate()).isEqualTo(shipmentDate);
        assertThat(shipment.getCarrier()).isEqualTo("FedEx");
        assertThat(shipment.getTrackingNumber()).isEqualTo("123456789");
    }

    @Test
    void testApparelOrderRelationship() {
        // Create a apparel order
        ApparelOrder apparelOrder = ApparelOrder.builder()
                .status("NEW")
                .build();

        // Create a apparel order shipment
        ApparelOrderShipment shipment = ApparelOrderShipment.builder()
                .shipmentDate(LocalDateTime.now())
                .carrier("UPS")
                .trackingNumber("987654321")
                .build();

        // Add shipment to apparel order
        apparelOrder.addShipment(shipment);

        // Verify relationship
        assertThat(apparelOrder.getShipments()).hasSize(1);
        assertThat(apparelOrder.getShipments()).contains(shipment);
        assertThat(shipment.getApparelOrder()).isEqualTo(apparelOrder);

        // Test removing shipment
        apparelOrder.removeShipment(shipment);
        assertThat(apparelOrder.getShipments()).isEmpty();
        assertThat(shipment.getApparelOrder()).isNull();
    }
}