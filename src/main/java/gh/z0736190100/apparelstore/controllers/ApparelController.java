package gh.z0736190100.apparelstore.controllers;

import gh.z0736190100.apparelstore.models.ApparelDto;
import gh.z0736190100.apparelstore.models.ApparelPatchDto;
import gh.z0736190100.apparelstore.services.ApparelService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST Controller for Apparel operations
 */
@RestController
@RequestMapping("/api/v1/apparels")
public class ApparelController {

    private final ApparelService apparelService;

    public ApparelController(ApparelService apparelService) {
        this.apparelService = apparelService;
    }

    /**
     * Get all apparels with optional filtering by apparel name, apparel style, and pagination
     * @param apparelName the apparel name to filter by (optional)
     * @param apparelStyle the apparel style to filter by (optional)
     * @param page the page number (zero-based, defaults to 0)
     * @param size the page size (defaults to 20)
     * @return Page of apparels matching the criteria
     */
    @GetMapping
    public Page<ApparelDto> getAllApparels(@RequestParam(required = false) String apparelName,
                                     @RequestParam(required = false) String apparelStyle,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return apparelService.getAllApparels(apparelName, apparelStyle, pageable);
    }

    /**
     * Get a apparel by its ID
     * @param id the apparel ID
     * @return ResponseEntity with the apparel if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApparelDto> getApparelById(@PathVariable Integer id) {
        Optional<ApparelDto> apparelOptional = apparelService.getApparelById(id);

        return apparelOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new apparel
     * @param apparelDto the apparel to create
     * @return ResponseEntity with the created apparel and 201 Created status
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApparelDto createApparel(@Valid @RequestBody ApparelDto apparelDto) {
        // Ensure a new apparel is created, not an update
        apparelDto.setId(null);
        return apparelService.saveApparel(apparelDto);
    }

    /**
     * Update an existing apparel
     * @param id the apparel ID
     * @param apparelDto the updated apparel data
     * @return ResponseEntity with the updated apparel if found, or 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApparelDto> updateApparel(@PathVariable Integer id, @Valid @RequestBody ApparelDto apparelDto) {
        Optional<ApparelDto> apparelOptional = apparelService.getApparelById(id);

        if (apparelOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Ensure we're updating the correct apparel
        apparelDto.setId(id);
        ApparelDto updatedApparel = apparelService.saveApparel(apparelDto);
        return ResponseEntity.ok(updatedApparel);
    }

    /**
     * Delete a apparel by its ID
     * @param id the apparel ID
     * @return ResponseEntity with no content if successful, or 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApparel(@PathVariable Integer id) {
        Optional<ApparelDto> apparelOptional = apparelService.getApparelById(id);

        if (apparelOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        apparelService.deleteApparelById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Partially update an existing apparel
     * @param id the apparel ID
     * @param apparelPatchDto the partial apparel data to update
     * @return ResponseEntity with the updated apparel if found, or 404 Not Found
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApparelDto> patchApparel(@PathVariable Integer id, @RequestBody ApparelPatchDto apparelPatchDto) {
        Optional<ApparelDto> updatedApparelOptional = apparelService.patchApparel(id, apparelPatchDto);

        return updatedApparelOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
