package gh.z0736190100.apparelstore.mappers;

import gh.z0736190100.apparelstore.entities.ApparelOrderShipment;
import gh.z0736190100.apparelstore.models.ApparelOrderShipmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for ApparelOrderShipment entity and ApparelOrderShipmentDto
 */
@Mapper
public interface ApparelOrderShipmentMapper {

    ApparelOrderShipmentDto apparelOrderShipmentToApparelOrderShipmentDto(ApparelOrderShipment apparelOrderShipment);

    @Mapping(target = "apparelOrder", ignore = true)
    ApparelOrderShipment apparelOrderShipmentDtoToApparelOrderShipment(ApparelOrderShipmentDto apparelOrderShipmentDto);
}