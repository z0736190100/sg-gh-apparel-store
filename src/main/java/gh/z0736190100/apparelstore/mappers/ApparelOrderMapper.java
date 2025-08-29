package gh.z0736190100.apparelstore.mappers;

import gh.z0736190100.apparelstore.entities.ApparelOrder;
import gh.z0736190100.apparelstore.entities.ApparelOrderLine;
import gh.z0736190100.apparelstore.entities.ApparelOrderShipment;
import gh.z0736190100.apparelstore.models.ApparelOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for ApparelOrder entity and ApparelOrderDto
 */
@Mapper(uses = {ApparelOrderLineMapper.class, CustomerMapper.class, ApparelOrderShipmentMapper.class})
public interface ApparelOrderMapper {

    ApparelOrderDto apparelOrderToApparelOrderDto(ApparelOrder apparelOrder);

    @Mapping(target = "apparelOrderLines", ignore = true)
    @Mapping(target = "shipments", ignore = true)
    ApparelOrder apparelOrderDtoToApparelOrder(ApparelOrderDto apparelOrderDto);

    /**
     * Add apparel order lines to apparel order
     * @param apparelOrder the apparel order
     * @param apparelOrderDto the apparel order DTO
     * @param apparelOrderLineMapper the apparel order line mapper
     * @return the updated apparel order
     */
    default ApparelOrder addApparelOrderLines(ApparelOrder apparelOrder, ApparelOrderDto apparelOrderDto, ApparelOrderLineMapper apparelOrderLineMapper) {
        if (apparelOrderDto.getApparelOrderLines() != null && !apparelOrderDto.getApparelOrderLines().isEmpty()) {
            apparelOrderDto.getApparelOrderLines().forEach(lineDto -> {
                ApparelOrderLine line = apparelOrderLineMapper.apparelOrderLineDtoToApparelOrderLine(lineDto);
                apparelOrder.addApparelOrderLine(line);
            });
        }
        return apparelOrder;
    }

    /**
     * Add shipments to apparel order
     * @param apparelOrder the apparel order
     * @param apparelOrderDto the apparel order DTO
     * @param apparelOrderShipmentMapper the apparel order shipment mapper
     * @return the updated apparel order
     */
    default ApparelOrder addShipments(ApparelOrder apparelOrder, ApparelOrderDto apparelOrderDto, ApparelOrderShipmentMapper apparelOrderShipmentMapper) {
        if (apparelOrderDto.getShipments() != null && !apparelOrderDto.getShipments().isEmpty()) {
            apparelOrderDto.getShipments().forEach(shipmentDto -> {
                ApparelOrderShipment shipment = apparelOrderShipmentMapper.apparelOrderShipmentDtoToApparelOrderShipment(shipmentDto);
                apparelOrder.addShipment(shipment);
            });
        }
        return apparelOrder;
    }
}
