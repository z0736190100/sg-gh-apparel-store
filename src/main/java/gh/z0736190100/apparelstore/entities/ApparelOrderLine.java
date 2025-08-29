package gh.z0736190100.apparelstore.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a line item in a apparel order
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApparelOrderLine extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "apparel_order_id")
    private ApparelOrder apparelOrder;

    @ManyToOne
    @JoinColumn(name = "apparel_id")
    private Apparel apparel;

    private Integer orderQuantity;
    private Integer quantityAllocated;
    private String status;
}