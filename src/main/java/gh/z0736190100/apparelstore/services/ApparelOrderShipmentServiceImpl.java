package gh.z0736190100.apparelstore.services;

import gh.z0736190100.apparelstore.entities.ApparelOrder;
import gh.z0736190100.apparelstore.entities.ApparelOrderShipment;
import gh.z0736190100.apparelstore.exceptions.NotFoundException;
import gh.z0736190100.apparelstore.mappers.ApparelOrderShipmentMapper;
import gh.z0736190100.apparelstore.models.ApparelOrderShipmentDto;
import gh.z0736190100.apparelstore.repositories.ApparelOrderRepository;
import gh.z0736190100.apparelstore.repositories.ApparelOrderShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ApparelOrderShipmentService
 */
@Service
@RequiredArgsConstructor
public class ApparelOrderShipmentServiceImpl implements ApparelOrderShipmentService {

    private final ApparelOrderRepository apparelOrderRepository;
    private final ApparelOrderShipmentRepository apparelOrderShipmentRepository;
    private final ApparelOrderShipmentMapper apparelOrderShipmentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ApparelOrderShipmentDto> getAllShipments(Integer apparelOrderId) {
        return apparelOrderShipmentRepository.findByApparelOrderId(apparelOrderId)
                .stream()
                .map(apparelOrderShipmentMapper::apparelOrderShipmentToApparelOrderShipmentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ApparelOrderShipmentDto getShipmentById(Integer apparelOrderId, Integer shipmentId) {
        ApparelOrderShipment shipment = getShipmentEntity(apparelOrderId, shipmentId);
        return apparelOrderShipmentMapper.apparelOrderShipmentToApparelOrderShipmentDto(shipment);
    }

    @Override
    @Transactional
    public ApparelOrderShipmentDto createShipment(Integer apparelOrderId, ApparelOrderShipmentDto shipmentDto) {
        ApparelOrder apparelOrder = apparelOrderRepository.findById(apparelOrderId)
                .orElseThrow(() -> new NotFoundException("Apparel Order not found with id: " + apparelOrderId));

        ApparelOrderShipment shipment = apparelOrderShipmentMapper.apparelOrderShipmentDtoToApparelOrderShipment(shipmentDto);
        apparelOrder.addShipment(shipment);
        
        ApparelOrderShipment savedShipment = apparelOrderShipmentRepository.save(shipment);
        return apparelOrderShipmentMapper.apparelOrderShipmentToApparelOrderShipmentDto(savedShipment);
    }

    @Override
    @Transactional
    public ApparelOrderShipmentDto updateShipment(Integer apparelOrderId, Integer shipmentId, ApparelOrderShipmentDto shipmentDto) {
        ApparelOrderShipment shipment = getShipmentEntity(apparelOrderId, shipmentId);
        
        // Update fields
        shipment.setShipmentDate(shipmentDto.getShipmentDate());
        shipment.setCarrier(shipmentDto.getCarrier());
        shipment.setTrackingNumber(shipmentDto.getTrackingNumber());
        
        ApparelOrderShipment savedShipment = apparelOrderShipmentRepository.save(shipment);
        return apparelOrderShipmentMapper.apparelOrderShipmentToApparelOrderShipmentDto(savedShipment);
    }

    @Override
    @Transactional
    public void deleteShipment(Integer apparelOrderId, Integer shipmentId) {
        ApparelOrderShipment shipment = getShipmentEntity(apparelOrderId, shipmentId);
        ApparelOrder apparelOrder = shipment.getApparelOrder();
        apparelOrder.removeShipment(shipment);
        apparelOrderShipmentRepository.delete(shipment);
    }
    
    private ApparelOrderShipment getShipmentEntity(Integer apparelOrderId, Integer shipmentId) {
        ApparelOrderShipment shipment = apparelOrderShipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new NotFoundException("Shipment not found with id: " + shipmentId));
        
        if (!shipment.getApparelOrder().getId().equals(apparelOrderId)) {
            throw new NotFoundException("Shipment not found for Apparel Order with id: " + apparelOrderId);
        }
        
        return shipment;
    }
}