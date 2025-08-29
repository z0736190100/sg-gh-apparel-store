package gh.z0736190100.apparelstore.repositories;

import gh.z0736190100.apparelstore.entities.ApparelOrderShipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for ApparelOrderShipment entity
 */
public interface ApparelOrderShipmentRepository extends JpaRepository<ApparelOrderShipment, Integer> {

    /**
     * Find shipments by apparel order id
     * @param apparelOrderId the apparel order id
     * @return the list of shipments
     */
    List<ApparelOrderShipment> findByApparelOrderId(Integer apparelOrderId);
}