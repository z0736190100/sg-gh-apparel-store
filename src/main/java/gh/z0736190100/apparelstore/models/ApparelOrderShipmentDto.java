package gh.z0736190100.apparelstore.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * DTO for ApparelOrderShipment entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ApparelOrderShipmentDto extends BaseEntityDto {

    @NotNull(message = "Shipment date is required")
    private LocalDateTime shipmentDate;
    
    private String carrier;
    private String trackingNumber;
}