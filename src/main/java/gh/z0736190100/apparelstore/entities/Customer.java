package gh.z0736190100.apparelstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a customer
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Customer extends BaseEntity {

    @Column(nullable = false)
    private String name;
    
    private String email;
    
    private String phoneNumber;
    
    @Column(nullable = false)
    private String addressLine1;
    
    private String addressLine2;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private String state;
    
    @Column(nullable = false)
    private String postalCode;
    
    @OneToMany(mappedBy = "customer")
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ApparelOrder> apparelOrders = new HashSet<>();
    
    /**
     * Helper method to add a apparel order to this customer
     * @param apparelOrder the apparel order to add
     */
    public void addApparelOrder(ApparelOrder apparelOrder) {
        if (apparelOrders == null) {
            apparelOrders = new HashSet<>();
        }
        apparelOrders.add(apparelOrder);
        apparelOrder.setCustomer(this);
    }
    
    /**
     * Helper method to remove a apparel order from this customer
     * @param apparelOrder the apparel order to remove
     */
    public void removeApparelOrder(ApparelOrder apparelOrder) {
        apparelOrders.remove(apparelOrder);
        apparelOrder.setCustomer(null);
    }
}