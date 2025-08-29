package gh.z0736190100.apparelstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Apparel extends BaseEntity {

    @Column(nullable = false)
    private String apparelName;

    private String apparelStyle;
    private String upc;
    private Integer quantityOnHand;
    private String description;

    @Column(precision = 19, scale = 2)
    private BigDecimal price;

    @OneToMany(mappedBy = "apparel")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ApparelOrderLine> apparelOrderLines = new HashSet<>();
}
