package gh.z0736190100.apparelstore.mappers;

import gh.z0736190100.apparelstore.entities.ApparelOrderLine;
import gh.z0736190100.apparelstore.models.ApparelOrderLineDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for ApparelOrderLine entity and ApparelOrderLineDto
 */
@Mapper
public interface ApparelOrderLineMapper {
    
    @Mapping(target = "apparelId", source = "apparel.id")
    @Mapping(target = "apparelName", source = "apparel.apparelName")
    @Mapping(target = "apparelStyle", source = "apparel.apparelStyle")
    @Mapping(target = "upc", source = "apparel.upc")
    ApparelOrderLineDto apparelOrderLineToApparelOrderLineDto(ApparelOrderLine apparelOrderLine);
    
    @Mapping(target = "apparel", ignore = true)
    @Mapping(target = "apparelOrder", ignore = true)
    ApparelOrderLine apparelOrderLineDtoToApparelOrderLine(ApparelOrderLineDto apparelOrderLineDto);
}