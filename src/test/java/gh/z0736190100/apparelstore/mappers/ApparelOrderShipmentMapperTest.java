package gh.z0736190100.apparelstore.mappers;

import gh.z0736190100.apparelstore.entities.ApparelOrder;
import gh.z0736190100.apparelstore.entities.ApparelOrderShipment;
import gh.z0736190100.apparelstore.models.ApparelOrderShipmentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for ApparelOrderShipmentMapper
 */
class ApparelOrderShipmentMapperTest {

    private ApparelOrderShipmentMapper apparelOrderShipmentMapper;
    private ApparelOrderShipment testApparelOrderShipment;
    private ApparelOrder testApparelOrder;
    private LocalDateTime testShipmentDate;

    @BeforeEach
    void setUp() {
        apparelOrderShipmentMapper = Mappers.getMapper(ApparelOrderShipmentMapper.class);
        
        // Create test apparel order
        testApparelOrder = ApparelOrder.builder()
                .status("COMPLETED")
                .build();
        testApparelOrder.setId(1);
        
        // Create test shipment date
        testShipmentDate = LocalDateTime.now();
        
        // Create test apparel order shipment
        testApparelOrderShipment = ApparelOrderShipment.builder()
                .shipmentDate(testShipmentDate)
                .carrier("FedEx")
                .trackingNumber("123456789")
                .apparelOrder(testApparelOrder)
                .build();
        testApparelOrderShipment.setId(1);
    }

    @Test
    void testApparelOrderShipmentToApparelOrderShipmentDto() {
        // When
        ApparelOrderShipmentDto shipmentDto = apparelOrderShipmentMapper.apparelOrderShipmentToApparelOrderShipmentDto(testApparelOrderShipment);
        
        // Then
        assertThat(shipmentDto).isNotNull();
        assertThat(shipmentDto.getId()).isEqualTo(testApparelOrderShipment.getId());
        assertThat(shipmentDto.getShipmentDate()).isEqualTo(testApparelOrderShipment.getShipmentDate());
        assertThat(shipmentDto.getCarrier()).isEqualTo(testApparelOrderShipment.getCarrier());
        assertThat(shipmentDto.getTrackingNumber()).isEqualTo(testApparelOrderShipment.getTrackingNumber());
    }

    @Test
    void testApparelOrderShipmentDtoToApparelOrderShipment() {
        // Given
        ApparelOrderShipmentDto shipmentDto = ApparelOrderShipmentDto.builder()
                .id(2)
                .shipmentDate(testShipmentDate.plusDays(1))
                .carrier("UPS")
                .trackingNumber("987654321")
                .build();
        
        // When
        ApparelOrderShipment shipment = apparelOrderShipmentMapper.apparelOrderShipmentDtoToApparelOrderShipment(shipmentDto);
        
        // Then
        assertThat(shipment).isNotNull();
        assertThat(shipment.getId()).isNull(); // ID should be ignored in mapping
        assertThat(shipment.getApparelOrder()).isNull(); // ApparelOrder should be ignored in mapping
        assertThat(shipment.getShipmentDate()).isEqualTo(shipmentDto.getShipmentDate());
        assertThat(shipment.getCarrier()).isEqualTo(shipmentDto.getCarrier());
        assertThat(shipment.getTrackingNumber()).isEqualTo(shipmentDto.getTrackingNumber());
    }
}