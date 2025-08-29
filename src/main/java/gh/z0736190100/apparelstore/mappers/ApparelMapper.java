package gh.z0736190100.apparelstore.mappers;

import gh.z0736190100.apparelstore.entities.Apparel;
import gh.z0736190100.apparelstore.models.ApparelDto;
import gh.z0736190100.apparelstore.models.ApparelPatchDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper for Apparel entity and ApparelDto
 */
@Mapper(componentModel = "spring")
public interface ApparelMapper {

    ApparelDto apparelToApparelDto(Apparel apparel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "apparelOrderLines", ignore = true)
    Apparel apparelDtoToApparel(ApparelDto apparelDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "apparelOrderLines", ignore = true)
    void updateApparelFromDto(ApparelDto apparelDto, @MappingTarget Apparel apparel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "apparelOrderLines", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateApparelFromPatchDto(ApparelPatchDto apparelPatchDto, @MappingTarget Apparel apparel);
}
