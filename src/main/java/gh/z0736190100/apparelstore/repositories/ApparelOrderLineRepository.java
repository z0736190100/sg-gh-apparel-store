package gh.z0736190100.apparelstore.repositories;

import gh.z0736190100.apparelstore.entities.ApparelOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for ApparelOrderLine entity
 */
public interface ApparelOrderLineRepository extends JpaRepository<ApparelOrderLine, Integer> {
    // Spring Data JPA will implement basic CRUD operations
    // Custom query methods can be added here if needed
}