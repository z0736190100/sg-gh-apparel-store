package gh.z0736190100.apparelstore.services;

import gh.z0736190100.apparelstore.models.ApparelOrderShipmentDto;

import java.util.List;

/**
 * Service for ApparelOrderShipment operations
 */
public interface ApparelOrderShipmentService {

    /**
     * Get all shipments for a apparel order
     * @param apparelOrderId the apparel order id
     * @return the list of shipments
     */
    List<ApparelOrderShipmentDto> getAllShipments(Integer apparelOrderId);

    /**
     * Get a shipment by id
     * @param apparelOrderId the apparel order id
     * @param shipmentId the shipment id
     * @return the shipment
     */
    ApparelOrderShipmentDto getShipmentById(Integer apparelOrderId, Integer shipmentId);

    /**
     * Create a new shipment
     * @param apparelOrderId the apparel order id
     * @param shipmentDto the shipment DTO
     * @return the created shipment
     */
    ApparelOrderShipmentDto createShipment(Integer apparelOrderId, ApparelOrderShipmentDto shipmentDto);

    /**
     * Update a shipment
     * @param apparelOrderId the apparel order id
     * @param shipmentId the shipment id
     * @param shipmentDto the shipment DTO
     * @return the updated shipment
     */
    ApparelOrderShipmentDto updateShipment(Integer apparelOrderId, Integer shipmentId, ApparelOrderShipmentDto shipmentDto);

    /**
     * Delete a shipment
     * @param apparelOrderId the apparel order id
     * @param shipmentId the shipment id
     */
    void deleteShipment(Integer apparelOrderId, Integer shipmentId);
}