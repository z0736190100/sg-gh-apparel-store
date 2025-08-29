package gh.z0736190100.apparelstore.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a apparel order
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApparelOrder extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(precision = 19, scale = 2)
    private BigDecimal paymentAmount;

    private String status;

    @OneToMany(mappedBy = "apparelOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ApparelOrderLine> apparelOrderLines = new HashSet<>();

    @OneToMany(mappedBy = "apparelOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ApparelOrderShipment> shipments = new HashSet<>();

    /**
     * Helper method to add a apparel order line to this order
     * @param line the apparel order line to add
     */
    public void addApparelOrderLine(ApparelOrderLine line) {
        if (apparelOrderLines == null) {
            apparelOrderLines = new HashSet<>();
        }
        apparelOrderLines.add(line);
        line.setApparelOrder(this);
    }

    /**
     * Helper method to remove a apparel order line from this order
     * @param line the apparel order line to remove
     */
    public void removeApparelOrderLine(ApparelOrderLine line) {
        apparelOrderLines.remove(line);
        line.setApparelOrder(null);
    }

    /**
     * Helper method to add a shipment to this order
     * @param shipment the shipment to add
     */
    public void addShipment(ApparelOrderShipment shipment) {
        if (shipments == null) {
            shipments = new HashSet<>();
        }
        shipments.add(shipment);
        shipment.setApparelOrder(this);
    }

    /**
     * Helper method to remove a shipment from this order
     * @param shipment the shipment to remove
     */
    public void removeShipment(ApparelOrderShipment shipment) {
        shipments.remove(shipment);
        shipment.setApparelOrder(null);
    }
}
