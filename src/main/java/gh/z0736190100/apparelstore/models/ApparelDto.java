package gh.z0736190100.apparelstore.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ApparelDto extends BaseEntityDto {

    @NotBlank(message = "Apparel name is required")
    private String apparelName;

    // style of the apparel, ALE, PALE ALE, IPA, etc
    @NotBlank(message = "Apparel style is required")
    private String apparelStyle;

    // Universal Product Code, a 13-digit number assigned to each unique apparel product by the Federal Bar Association
    @NotBlank(message = "UPC is required")
    private String upc;

    @NotNull(message = "Quantity on hand is required")
    @PositiveOrZero(message = "Quantity on hand must be zero or positive")
    private Integer quantityOnHand;

    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
}
