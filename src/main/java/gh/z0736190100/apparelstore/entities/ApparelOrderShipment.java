package gh.z0736190100.apparelstore.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity representing a shipment for a apparel order
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApparelOrderShipment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "apparel_order_id")
    private ApparelOrder apparelOrder;

    private LocalDateTime shipmentDate;
    private String carrier;
    private String trackingNumber;
}