package gh.z0736190100.apparelstore.repositories;

import gh.z0736190100.apparelstore.entities.ApparelOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for ApparelOrder entity
 */
public interface ApparelOrderRepository extends JpaRepository<ApparelOrder, Integer> {
    // Spring Data JPA will implement basic CRUD operations
    // Custom query methods can be added here if needed
}