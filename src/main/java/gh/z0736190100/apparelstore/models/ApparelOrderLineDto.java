package gh.z0736190100.apparelstore.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for ApparelOrderLine entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ApparelOrderLineDto extends BaseEntityDto {
    
    private Integer apparelId;
    private String apparelName;

    private String apparelStyle;
    private String upc;
    
    @NotNull(message = "Order quantity is required")
    @Positive(message = "Order quantity must be positive")
    private Integer orderQuantity;
    
    @PositiveOrZero(message = "Quantity allocated must be zero or positive")
    private Integer quantityAllocated;
    
    private String status;
}