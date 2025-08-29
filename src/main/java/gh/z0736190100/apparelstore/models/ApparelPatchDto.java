package gh.z0736190100.apparelstore.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * DTO for Apparel patch operations
 * This DTO is used for partial updates and does not have any validation constraints
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ApparelPatchDto extends BaseEntityDto {

    private String apparelName;
    private String apparelStyle;
    private String upc;
    private Integer quantityOnHand;
    private String description;
    private BigDecimal price;
}