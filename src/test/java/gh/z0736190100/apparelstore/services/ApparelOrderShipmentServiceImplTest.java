package gh.z0736190100.apparelstore.services;

import gh.z0736190100.apparelstore.entities.ApparelOrder;
import gh.z0736190100.apparelstore.entities.ApparelOrderShipment;
import gh.z0736190100.apparelstore.exceptions.NotFoundException;
import gh.z0736190100.apparelstore.mappers.ApparelOrderShipmentMapper;
import gh.z0736190100.apparelstore.models.ApparelOrderShipmentDto;
import gh.z0736190100.apparelstore.repositories.ApparelOrderRepository;
import gh.z0736190100.apparelstore.repositories.ApparelOrderShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for ApparelOrderShipmentServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class ApparelOrderShipmentServiceImplTest {

    @Mock
    ApparelOrderRepository apparelOrderRepository;

    @Mock
    ApparelOrderShipmentRepository apparelOrderShipmentRepository;

    @Mock
    ApparelOrderShipmentMapper apparelOrderShipmentMapper;

    @InjectMocks
    ApparelOrderShipmentServiceImpl apparelOrderShipmentService;

    ApparelOrder testApparelOrder;
    ApparelOrderShipment testApparelOrderShipment;
    ApparelOrderShipmentDto testApparelOrderShipmentDto;
    LocalDateTime testShipmentDate;

    @BeforeEach
    void setUp() {
        // Create test shipment date
        testShipmentDate = LocalDateTime.now();

        // Create test apparel order
        testApparelOrder = ApparelOrder.builder()
                .status("COMPLETED")
                .build();
        testApparelOrder.setId(1);

        // Create test apparel order shipment
        testApparelOrderShipment = ApparelOrderShipment.builder()
                .shipmentDate(testShipmentDate)
                .carrier("FedEx")
                .trackingNumber("123456789")
                .apparelOrder(testApparelOrder)
                .build();
        testApparelOrderShipment.setId(1);

        // Create test apparel order shipment DTO
        testApparelOrderShipmentDto = ApparelOrderShipmentDto.builder()
                .id(1)
                .shipmentDate(testShipmentDate)
                .carrier("FedEx")
                .trackingNumber("123456789")
                .build();
    }

    @Test
    void getAllShipments() {
        // Given
        when(apparelOrderShipmentRepository.findByApparelOrderId(1)).thenReturn(Arrays.asList(testApparelOrderShipment));
        when(apparelOrderShipmentMapper.apparelOrderShipmentToApparelOrderShipmentDto(testApparelOrderShipment)).thenReturn(testApparelOrderShipmentDto);

        // When
        List<ApparelOrderShipmentDto> shipments = apparelOrderShipmentService.getAllShipments(1);

        // Then
        assertThat(shipments).hasSize(1);
        assertThat(shipments.get(0).getCarrier()).isEqualTo("FedEx");
        assertThat(shipments.get(0).getTrackingNumber()).isEqualTo("123456789");
        verify(apparelOrderShipmentRepository, times(1)).findByApparelOrderId(1);
        verify(apparelOrderShipmentMapper, times(1)).apparelOrderShipmentToApparelOrderShipmentDto(any(ApparelOrderShipment.class));
    }

    @Test
    void getShipmentById() {
        // Given
        when(apparelOrderShipmentRepository.findById(1)).thenReturn(Optional.of(testApparelOrderShipment));
        when(apparelOrderShipmentMapper.apparelOrderShipmentToApparelOrderShipmentDto(testApparelOrderShipment)).thenReturn(testApparelOrderShipmentDto);

        // When
        ApparelOrderShipmentDto shipmentDto = apparelOrderShipmentService.getShipmentById(1, 1);

        // Then
        assertThat(shipmentDto).isNotNull();
        assertThat(shipmentDto.getCarrier()).isEqualTo("FedEx");
        assertThat(shipmentDto.getTrackingNumber()).isEqualTo("123456789");
        verify(apparelOrderShipmentRepository, times(1)).findById(1);
        verify(apparelOrderShipmentMapper, times(1)).apparelOrderShipmentToApparelOrderShipmentDto(any(ApparelOrderShipment.class));
    }

    @Test
    void getShipmentByIdNotFound() {
        // Given
        when(apparelOrderShipmentRepository.findById(1)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> apparelOrderShipmentService.getShipmentById(1, 1))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Shipment not found with id: 1");
    }

    @Test
    void getShipmentByIdWrongApparelOrder() {
        // Given
        ApparelOrder anotherApparelOrder = ApparelOrder.builder().status("NEW").build();
        anotherApparelOrder.setId(2);
        
        ApparelOrderShipment shipment = ApparelOrderShipment.builder()
                .shipmentDate(testShipmentDate)
                .carrier("UPS")
                .trackingNumber("987654321")
                .apparelOrder(anotherApparelOrder)
                .build();
        shipment.setId(1);
        
        when(apparelOrderShipmentRepository.findById(1)).thenReturn(Optional.of(shipment));

        // Then
        assertThatThrownBy(() -> apparelOrderShipmentService.getShipmentById(1, 1))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Shipment not found for Apparel Order with id: 1");
    }

    @Test
    void createShipment() {
        // Given
        when(apparelOrderRepository.findById(1)).thenReturn(Optional.of(testApparelOrder));
        when(apparelOrderShipmentMapper.apparelOrderShipmentDtoToApparelOrderShipment(testApparelOrderShipmentDto)).thenReturn(testApparelOrderShipment);
        when(apparelOrderShipmentRepository.save(any(ApparelOrderShipment.class))).thenReturn(testApparelOrderShipment);
        when(apparelOrderShipmentMapper.apparelOrderShipmentToApparelOrderShipmentDto(testApparelOrderShipment)).thenReturn(testApparelOrderShipmentDto);

        // When
        ApparelOrderShipmentDto createdShipmentDto = apparelOrderShipmentService.createShipment(1, testApparelOrderShipmentDto);

        // Then
        assertThat(createdShipmentDto).isNotNull();
        assertThat(createdShipmentDto.getCarrier()).isEqualTo("FedEx");
        assertThat(createdShipmentDto.getTrackingNumber()).isEqualTo("123456789");
        verify(apparelOrderRepository, times(1)).findById(1);
        verify(apparelOrderShipmentMapper, times(1)).apparelOrderShipmentDtoToApparelOrderShipment(any(ApparelOrderShipmentDto.class));
        verify(apparelOrderShipmentRepository, times(1)).save(any(ApparelOrderShipment.class));
        verify(apparelOrderShipmentMapper, times(1)).apparelOrderShipmentToApparelOrderShipmentDto(any(ApparelOrderShipment.class));
    }

    @Test
    void createShipmentApparelOrderNotFound() {
        // Given
        when(apparelOrderRepository.findById(1)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> apparelOrderShipmentService.createShipment(1, testApparelOrderShipmentDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Apparel Order not found with id: 1");
    }

    @Test
    void updateShipment() {
        // Given
        when(apparelOrderShipmentRepository.findById(1)).thenReturn(Optional.of(testApparelOrderShipment));
        when(apparelOrderShipmentRepository.save(any(ApparelOrderShipment.class))).thenReturn(testApparelOrderShipment);
        when(apparelOrderShipmentMapper.apparelOrderShipmentToApparelOrderShipmentDto(testApparelOrderShipment)).thenReturn(testApparelOrderShipmentDto);

        // Create updated DTO
        ApparelOrderShipmentDto updatedDto = ApparelOrderShipmentDto.builder()
                .id(1)
                .shipmentDate(testShipmentDate)
                .carrier("UPS")
                .trackingNumber("UPDATED123")
                .build();

        // When
        ApparelOrderShipmentDto updatedShipmentDto = apparelOrderShipmentService.updateShipment(1, 1, updatedDto);

        // Then
        assertThat(updatedShipmentDto).isNotNull();
        verify(apparelOrderShipmentRepository, times(1)).findById(1);
        verify(apparelOrderShipmentRepository, times(1)).save(any(ApparelOrderShipment.class));
        verify(apparelOrderShipmentMapper, times(1)).apparelOrderShipmentToApparelOrderShipmentDto(any(ApparelOrderShipment.class));
    }

    @Test
    void deleteShipment() {
        // Given
        when(apparelOrderShipmentRepository.findById(1)).thenReturn(Optional.of(testApparelOrderShipment));

        // When
        apparelOrderShipmentService.deleteShipment(1, 1);

        // Then
        verify(apparelOrderShipmentRepository, times(1)).findById(1);
        verify(apparelOrderShipmentRepository, times(1)).delete(any(ApparelOrderShipment.class));
    }
}