package gh.z0736190100.apparelstore.mappers;

import gh.z0736190100.apparelstore.entities.Apparel;
import gh.z0736190100.apparelstore.entities.ApparelOrder;
import gh.z0736190100.apparelstore.entities.ApparelOrderLine;
import gh.z0736190100.apparelstore.entities.Customer;
import gh.z0736190100.apparelstore.models.ApparelOrderDto;
import gh.z0736190100.apparelstore.models.ApparelOrderLineDto;
import gh.z0736190100.apparelstore.models.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class ApparelOrderMapperTest {

    private ApparelOrderMapper apparelOrderMapper;
    private ApparelOrderLineMapper apparelOrderLineMapper;
    private CustomerMapper customerMapper;
    private ApparelOrder testApparelOrder;
    private Apparel testApparel;
    private Customer testCustomer;
    private CustomerDto testCustomerDto;

    @BeforeEach
    void setUp() {
        apparelOrderMapper = Mappers.getMapper(ApparelOrderMapper.class);
        apparelOrderLineMapper = Mappers.getMapper(ApparelOrderLineMapper.class);
        customerMapper = Mappers.getMapper(CustomerMapper.class);

        ReflectionTestUtils.setField(apparelOrderMapper, "apparelOrderLineMapper", apparelOrderLineMapper);
        ReflectionTestUtils.setField(apparelOrderMapper, "customerMapper", customerMapper);

        // Create test customer
        testCustomer = Customer.builder()
                .name("Test Customer")
                .email("test@example.com")
                .phoneNumber("555-123-4567")
                .addressLine1("123 Main St")
                .city("Springfield")
                .state("IL")
                .postalCode("62701")
                .build();
        testCustomer.setId(1);

        testCustomerDto = CustomerDto.builder()
                .id(1)
                .name("Test Customer")
                .email("test@example.com")
                .phoneNumber("555-123-4567")
                .addressLine1("123 Main St")
                .city("Springfield")
                .state("IL")
                .postalCode("62701")
                .build();

        // Create test apparel
        testApparel = Apparel.builder()
                .apparelName("Test Apparel")
                .apparelStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        testApparel.setId(1);

        // Create test apparel order
        testApparelOrder = ApparelOrder.builder()
                .customer(testCustomer)
                .paymentAmount(new BigDecimal("25.98"))
                .status("NEW")
                .build();
        testApparelOrder.setId(1);
        testApparelOrder.setCreatedDate(LocalDateTime.now());
        testApparelOrder.setUpdateDate(LocalDateTime.now());

        // Create test apparel order line
        ApparelOrderLine testApparelOrderLine = ApparelOrderLine.builder()
                .orderQuantity(2)
                .quantityAllocated(2)
                .status("ALLOCATED")
                .build();
        testApparelOrderLine.setId(1);
        testApparelOrderLine.setApparel(testApparel);

        // Add apparel order line to apparel order
        testApparelOrder.addApparelOrderLine(testApparelOrderLine);
    }

    @Test
    void testApparelOrderToApparelOrderDto() {
        // When
        ApparelOrderDto apparelOrderDto = apparelOrderMapper.apparelOrderToApparelOrderDto(testApparelOrder);

        // Then
        assertThat(apparelOrderDto).isNotNull();
        assertThat(apparelOrderDto.getId()).isEqualTo(testApparelOrder.getId());
        assertThat(apparelOrderDto.getCustomer()).isNotNull();
        assertThat(apparelOrderDto.getCustomer().getName()).isEqualTo(testCustomer.getName());
        assertThat(apparelOrderDto.getPaymentAmount()).isEqualTo(testApparelOrder.getPaymentAmount());
        assertThat(apparelOrderDto.getStatus()).isEqualTo(testApparelOrder.getStatus());
        assertThat(apparelOrderDto.getApparelOrderLines()).hasSize(1);

        ApparelOrderLineDto lineDto = apparelOrderDto.getApparelOrderLines().iterator().next();
        assertThat(lineDto.getApparelId()).isEqualTo(testApparel.getId());
        assertThat(lineDto.getApparelName()).isEqualTo(testApparel.getApparelName());
        assertThat(lineDto.getApparelStyle()).isEqualTo(testApparel.getApparelStyle());
        assertThat(lineDto.getUpc()).isEqualTo(testApparel.getUpc());
        assertThat(lineDto.getOrderQuantity()).isEqualTo(2);
        assertThat(lineDto.getQuantityAllocated()).isEqualTo(2);
        assertThat(lineDto.getStatus()).isEqualTo("ALLOCATED");
    }

    @Test
    void testApparelOrderDtoToApparelOrder() {
        // Given
        ApparelOrderDto apparelOrderDto = ApparelOrderDto.builder()
                .id(2)
                .customer(testCustomerDto)
                .paymentAmount(new BigDecimal("39.97"))
                .status("PENDING")
                .apparelOrderLines(new HashSet<>())
                .build();

        ApparelOrderLineDto lineDto = ApparelOrderLineDto.builder()
                .id(2)
                .apparelId(1)
                .apparelName("Test Apparel")
                .apparelStyle("IPA")
                .upc("123456")
                .orderQuantity(3)
                .quantityAllocated(0)
                .status("NEW")
                .build();

        apparelOrderDto.getApparelOrderLines().add(lineDto);

        // When
        ApparelOrder apparelOrder = apparelOrderMapper.apparelOrderDtoToApparelOrder(apparelOrderDto);
        apparelOrder = apparelOrderMapper.addApparelOrderLines(apparelOrder, apparelOrderDto, apparelOrderLineMapper);

        // Then
        assertThat(apparelOrder).isNotNull();
        assertThat(apparelOrder.getId()).isNull(); // ID should be ignored in mapping
        assertThat(apparelOrder.getCustomer()).isNotNull();
        assertThat(apparelOrder.getCustomer().getName()).isEqualTo(testCustomer.getName());
        assertThat(apparelOrder.getPaymentAmount()).isEqualTo(apparelOrderDto.getPaymentAmount());
        assertThat(apparelOrder.getStatus()).isEqualTo(apparelOrderDto.getStatus());
        assertThat(apparelOrder.getApparelOrderLines()).hasSize(1);

        ApparelOrderLine line = apparelOrder.getApparelOrderLines().iterator().next();
        assertThat(line.getOrderQuantity()).isEqualTo(3);
        assertThat(line.getQuantityAllocated()).isEqualTo(0);
        assertThat(line.getStatus()).isEqualTo("NEW");
        assertThat(line.getApparelOrder()).isEqualTo(apparelOrder); // Bidirectional relationship should be set
    }
}
