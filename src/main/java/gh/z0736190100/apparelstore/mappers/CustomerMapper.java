package gh.z0736190100.apparelstore.mappers;

import gh.z0736190100.apparelstore.entities.Customer;
import gh.z0736190100.apparelstore.models.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for Customer entity and CustomerDto
 */
@Mapper
public interface CustomerMapper {

    @Mapping(target = "apparelOrders", ignore = true)
    CustomerDto customerToCustomerDto(Customer customer);

    @Mapping(target = "apparelOrders", ignore = true)
    Customer customerDtoToCustomer(CustomerDto customerDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "apparelOrders", ignore = true)
    void updateCustomerFromDto(CustomerDto customerDto, @MappingTarget Customer customer);
}
