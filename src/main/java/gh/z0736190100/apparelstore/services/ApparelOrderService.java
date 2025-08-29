package gh.z0736190100.apparelstore.services;

import gh.z0736190100.apparelstore.models.ApparelOrderDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for ApparelOrder operations
 */
public interface ApparelOrderService {

    /**
     * Get all apparel orders
     * @return List of all apparel orders
     */
    List<ApparelOrderDto> getAllApparelOrders();

    /**
     * Get a apparel order by its ID
     * @param id the apparel order ID
     * @return Optional containing the apparel order if found
     */
    Optional<ApparelOrderDto> getApparelOrderById(Integer id);

    /**
     * Save a new apparel order or update an existing one
     * @param apparelOrderDto the apparel order to save
     * @return the saved apparel order
     */
    ApparelOrderDto saveApparelOrder(ApparelOrderDto apparelOrderDto);

    /**
     * Delete a apparel order by its ID
     * @param id the apparel order ID
     */
    void deleteApparelOrderById(Integer id);
}