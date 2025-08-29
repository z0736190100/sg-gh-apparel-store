package gh.z0736190100.apparelstore.controllers;

import gh.z0736190100.apparelstore.models.ApparelOrderDto;
import gh.z0736190100.apparelstore.services.ApparelOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for ApparelOrder operations
 */
@RestController
@RequestMapping("/api/v1/apparel-orders")
public class ApparelOrderController {

    private final ApparelOrderService apparelOrderService;

    public ApparelOrderController(ApparelOrderService apparelOrderService) {
        this.apparelOrderService = apparelOrderService;
    }

    /**
     * Get all apparel orders
     * @return List of all apparel orders
     */
    @GetMapping
    public List<ApparelOrderDto> getAllApparelOrders() {
        return apparelOrderService.getAllApparelOrders();
    }

    /**
     * Get a apparel order by its ID
     * @param id the apparel order ID
     * @return ResponseEntity with the apparel order if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApparelOrderDto> getApparelOrderById(@PathVariable Integer id) {
        Optional<ApparelOrderDto> apparelOrderOptional = apparelOrderService.getApparelOrderById(id);

        return apparelOrderOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new apparel order
     * @param apparelOrderDto the apparel order to create
     * @return ResponseEntity with the created apparel order and 201 Created status
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApparelOrderDto createApparelOrder(@Valid @RequestBody ApparelOrderDto apparelOrderDto) {
        // Ensure a new apparel order is created, not an update
        apparelOrderDto.setId(null);
        return apparelOrderService.saveApparelOrder(apparelOrderDto);
    }

    /**
     * Update an existing apparel order
     * @param id the apparel order ID
     * @param apparelOrderDto the updated apparel order data
     * @return ResponseEntity with the updated apparel order if found, or 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApparelOrderDto> updateApparelOrder(@PathVariable Integer id, @Valid @RequestBody ApparelOrderDto apparelOrderDto) {
        Optional<ApparelOrderDto> apparelOrderOptional = apparelOrderService.getApparelOrderById(id);

        if (apparelOrderOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Ensure we're updating the correct apparel order
        apparelOrderDto.setId(id);
        ApparelOrderDto updatedApparelOrder = apparelOrderService.saveApparelOrder(apparelOrderDto);
        return ResponseEntity.ok(updatedApparelOrder);
    }

    /**
     * Delete a apparel order by its ID
     * @param id the apparel order ID
     * @return ResponseEntity with no content if successful, or 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApparelOrder(@PathVariable Integer id) {
        Optional<ApparelOrderDto> apparelOrderOptional = apparelOrderService.getApparelOrderById(id);

        if (apparelOrderOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        apparelOrderService.deleteApparelOrderById(id);
        return ResponseEntity.noContent().build();
    }
}