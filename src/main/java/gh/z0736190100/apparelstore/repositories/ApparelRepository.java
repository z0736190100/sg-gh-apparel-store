package gh.z0736190100.apparelstore.repositories;

import gh.z0736190100.apparelstore.entities.Apparel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Apparel entity
 */
public interface ApparelRepository extends JpaRepository<Apparel, Integer> {
    // Spring Data JPA will implement basic CRUD operations

    /**
     * Find all apparels with optional filtering by apparel name
     * @param apparelName the apparel name to filter by (can be null)
     * @param pageable pagination information
     * @return Page of apparels matching the criteria
     */
    Page<Apparel> findAllByApparelNameContainingIgnoreCase(String apparelName, Pageable pageable);

    /**
     * Find all apparels with optional filtering by apparel name and apparel style
     * @param apparelName the apparel name to filter by (can be null)
     * @param apparelStyle the apparel style to filter by (can be null)
     * @param pageable pagination information
     * @return Page of apparels matching the criteria
     */
    Page<Apparel> findAllByApparelNameContainingIgnoreCaseAndApparelStyleContainingIgnoreCase(
            String apparelName, String apparelStyle, Pageable pageable);
}
