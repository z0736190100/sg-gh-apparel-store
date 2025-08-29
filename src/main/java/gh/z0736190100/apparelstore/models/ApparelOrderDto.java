package gh.z0736190100.apparelstore.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Set;

/**
 * DTO for ApparelOrder entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ApparelOrderDto extends BaseEntityDto {

    @NotNull(message = "Customer is required")
    private CustomerDto customer;

    @NotNull(message = "Payment amount is required")
    @Positive(message = "Payment amount must be positive")
    private BigDecimal paymentAmount;

    // enum status of the order, NEW, PAID, CANCELLED, INPROCESS, COMPLETE.
    private String status;

    @NotEmpty(message = "Apparel order must have at least one apparel order line")
    @Valid
    private Set<ApparelOrderLineDto> apparelOrderLines;

    @Valid
    private Set<ApparelOrderShipmentDto> shipments;
}
