package gh.z0736190100.apparelstore.controllers;

import gh.z0736190100.apparelstore.models.ApparelOrderShipmentDto;
import gh.z0736190100.apparelstore.services.ApparelOrderShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for ApparelOrderShipment operations
 */
@RestController
@RequestMapping("/api/v1/apparel-orders/{apparelOrderId}/shipments")
@RequiredArgsConstructor
public class ApparelOrderShipmentController {

    private final ApparelOrderShipmentService apparelOrderShipmentService;

    /**
     * Get all shipments for a apparel order
     * @param apparelOrderId the apparel order id
     * @return the list of shipments
     */
    @GetMapping
    public ResponseEntity<List<ApparelOrderShipmentDto>> getAllShipments(@PathVariable Integer apparelOrderId) {
        return ResponseEntity.ok(apparelOrderShipmentService.getAllShipments(apparelOrderId));
    }

    /**
     * Get a shipment by id
     * @param apparelOrderId the apparel order id
     * @param shipmentId the shipment id
     * @return the shipment
     */
    @GetMapping("/{shipmentId}")
    public ResponseEntity<ApparelOrderShipmentDto> getShipmentById(
            @PathVariable Integer apparelOrderId,
            @PathVariable Integer shipmentId) {
        return ResponseEntity.ok(apparelOrderShipmentService.getShipmentById(apparelOrderId, shipmentId));
    }

    /**
     * Create a new shipment
     * @param apparelOrderId the apparel order id
     * @param shipmentDto the shipment DTO
     * @return the created shipment
     */
    @PostMapping
    public ResponseEntity<ApparelOrderShipmentDto> createShipment(
            @PathVariable Integer apparelOrderId,
            @Valid @RequestBody ApparelOrderShipmentDto shipmentDto) {
        ApparelOrderShipmentDto createdShipment = apparelOrderShipmentService.createShipment(apparelOrderId, shipmentDto);
        return new ResponseEntity<>(createdShipment, HttpStatus.CREATED);
    }

    /**
     * Update a shipment
     * @param apparelOrderId the apparel order id
     * @param shipmentId the shipment id
     * @param shipmentDto the shipment DTO
     * @return the updated shipment
     */
    @PutMapping("/{shipmentId}")
    public ResponseEntity<ApparelOrderShipmentDto> updateShipment(
            @PathVariable Integer apparelOrderId,
            @PathVariable Integer shipmentId,
            @Valid @RequestBody ApparelOrderShipmentDto shipmentDto) {
        return ResponseEntity.ok(apparelOrderShipmentService.updateShipment(apparelOrderId, shipmentId, shipmentDto));
    }

    /**
     * Delete a shipment
     * @param apparelOrderId the apparel order id
     * @param shipmentId the shipment id
     * @return no content
     */
    @DeleteMapping("/{shipmentId}")
    public ResponseEntity<Void> deleteShipment(
            @PathVariable Integer apparelOrderId,
            @PathVariable Integer shipmentId) {
        apparelOrderShipmentService.deleteShipment(apparelOrderId, shipmentId);
        return ResponseEntity.noContent().build();
    }
}