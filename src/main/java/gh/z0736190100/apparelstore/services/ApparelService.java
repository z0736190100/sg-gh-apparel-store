package gh.z0736190100.apparelstore.services;

import gh.z0736190100.apparelstore.models.ApparelDto;
import gh.z0736190100.apparelstore.models.ApparelPatchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Apparel operations
 */
public interface ApparelService {

    /**
     * Get all apparels
     * @return List of all apparels
     */
    List<ApparelDto> getAllApparels();

    /**
     * Get all apparels with optional filtering by apparel name and apparel style, with pagination
     * @param apparelName the apparel name to filter by (can be null)
     * @param apparelStyle the apparel style to filter by (can be null)
     * @param pageable pagination information
     * @return Page of apparels matching the criteria
     */
    Page<ApparelDto> getAllApparels(String apparelName, String apparelStyle, Pageable pageable);

    /**
     * Get a apparel by its ID
     * @param id the apparel ID
     * @return Optional containing the apparel if found
     */
    Optional<ApparelDto> getApparelById(Integer id);

    /**
     * Save a new apparel or update an existing one
     * @param apparelDto the apparel to save
     * @return the saved apparel
     */
    ApparelDto saveApparel(ApparelDto apparelDto);

    /**
     * Partially update an existing apparel
     * @param id the apparel ID
     * @param apparelPatchDto the apparel patch data
     * @return Optional containing the updated apparel if found
     */
    Optional<ApparelDto> patchApparel(Integer id, ApparelPatchDto apparelPatchDto);

    /**
     * Delete a apparel by its ID
     * @param id the apparel ID
     */
    void deleteApparelById(Integer id);
}
