package gh.z0736190100.apparelstore.services;

import gh.z0736190100.apparelstore.entities.Apparel;
import gh.z0736190100.apparelstore.entities.ApparelOrder;
import gh.z0736190100.apparelstore.entities.ApparelOrderLine;
import gh.z0736190100.apparelstore.entities.Customer;
import gh.z0736190100.apparelstore.mappers.ApparelOrderLineMapper;
import gh.z0736190100.apparelstore.mappers.ApparelOrderMapper;
import gh.z0736190100.apparelstore.models.ApparelOrderDto;
import gh.z0736190100.apparelstore.models.ApparelOrderLineDto;
import gh.z0736190100.apparelstore.models.CustomerDto;
import gh.z0736190100.apparelstore.repositories.ApparelOrderRepository;
import gh.z0736190100.apparelstore.repositories.ApparelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApparelOrderServiceImplTest {

    @Mock
    ApparelOrderRepository apparelOrderRepository;

    @Mock
    ApparelRepository apparelRepository;

    @Mock
    ApparelOrderMapper apparelOrderMapper;

    @Mock
    ApparelOrderLineMapper apparelOrderLineMapper;

    @InjectMocks
    ApparelOrderServiceImpl apparelOrderService;

    ApparelOrder testApparelOrder;
    ApparelOrderDto testApparelOrderDto;
    Apparel testApparel;
    ApparelOrderLine testApparelOrderLine;
    ApparelOrderLineDto testApparelOrderLineDto;
    Customer testCustomer;
    CustomerDto testCustomerDto;

    @BeforeEach
    void setUp() {
        // Create test apparel
        testApparel = Apparel.builder()
                .apparelName("Test Apparel")
                .apparelStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        testApparel.setId(1);

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

        // Create test customer DTO
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

        // Create test apparel order line
        testApparelOrderLine = ApparelOrderLine.builder()
                .orderQuantity(2)
                .quantityAllocated(2)
                .status("ALLOCATED")
                .apparel(testApparel)
                .build();
        testApparelOrderLine.setId(1);

        // Create test apparel order
        testApparelOrder = ApparelOrder.builder()
                .customer(testCustomer)
                .paymentAmount(new BigDecimal("25.98"))
                .status("NEW")
                .build();
        testApparelOrder.setId(1);
        testApparelOrder.addApparelOrderLine(testApparelOrderLine);

        // Create test apparel order line DTO
        testApparelOrderLineDto = ApparelOrderLineDto.builder()
                .id(1)
                .apparelId(1)
                .apparelName("Test Apparel")
                .apparelStyle("IPA")
                .upc("123456")
                .orderQuantity(2)
                .quantityAllocated(2)
                .status("ALLOCATED")
                .build();

        // Create test apparel order DTO
        Set<ApparelOrderLineDto> lines = new HashSet<>();
        lines.add(testApparelOrderLineDto);
        testApparelOrderDto = ApparelOrderDto.builder()
                .id(1)
                .customer(testCustomerDto)
                .paymentAmount(new BigDecimal("25.98"))
                .status("NEW")
                .apparelOrderLines(lines)
                .build();
    }

    @Test
    void getAllApparelOrders() {
        // Given
        when(apparelOrderRepository.findAll()).thenReturn(Arrays.asList(testApparelOrder));
        when(apparelOrderMapper.apparelOrderToApparelOrderDto(testApparelOrder)).thenReturn(testApparelOrderDto);

        // When
        List<ApparelOrderDto> apparelOrders = apparelOrderService.getAllApparelOrders();

        // Then
        assertThat(apparelOrders).hasSize(1);
        assertThat(apparelOrders.get(0).getCustomer()).isNotNull();
        assertThat(apparelOrders.get(0).getCustomer().getName()).isEqualTo("Test Customer");
        verify(apparelOrderRepository, times(1)).findAll();
        verify(apparelOrderMapper, times(1)).apparelOrderToApparelOrderDto(any(ApparelOrder.class));
    }

    @Test
    void getApparelOrderById() {
        // Given
        when(apparelOrderRepository.findById(1)).thenReturn(Optional.of(testApparelOrder));
        when(apparelOrderMapper.apparelOrderToApparelOrderDto(testApparelOrder)).thenReturn(testApparelOrderDto);

        // When
        Optional<ApparelOrderDto> apparelOrderOptional = apparelOrderService.getApparelOrderById(1);

        // Then
        assertThat(apparelOrderOptional).isPresent();
        assertThat(apparelOrderOptional.get().getCustomer()).isNotNull();
        assertThat(apparelOrderOptional.get().getCustomer().getName()).isEqualTo("Test Customer");
        verify(apparelOrderRepository, times(1)).findById(1);
        verify(apparelOrderMapper, times(1)).apparelOrderToApparelOrderDto(any(ApparelOrder.class));
    }

    @Test
    void getApparelOrderByIdNotFound() {
        // Given
        when(apparelOrderRepository.findById(1)).thenReturn(Optional.empty());

        // When
        Optional<ApparelOrderDto> apparelOrderOptional = apparelOrderService.getApparelOrderById(1);

        // Then
        assertThat(apparelOrderOptional).isEmpty();
        verify(apparelOrderRepository, times(1)).findById(1);
    }

    @Test
    void saveApparelOrder() {
        // Given
        when(apparelOrderMapper.apparelOrderDtoToApparelOrder(testApparelOrderDto)).thenReturn(testApparelOrder);
        when(apparelOrderLineMapper.apparelOrderLineDtoToApparelOrderLine(any(ApparelOrderLineDto.class))).thenReturn(testApparelOrderLine);
        when(apparelRepository.findById(1)).thenReturn(Optional.of(testApparel));
        when(apparelOrderRepository.save(any(ApparelOrder.class))).thenReturn(testApparelOrder);
        when(apparelOrderMapper.apparelOrderToApparelOrderDto(testApparelOrder)).thenReturn(testApparelOrderDto);

        // When
        ApparelOrderDto savedApparelOrderDto = apparelOrderService.saveApparelOrder(testApparelOrderDto);

        // Then
        assertThat(savedApparelOrderDto).isNotNull();
        assertThat(savedApparelOrderDto.getCustomer()).isNotNull();
        assertThat(savedApparelOrderDto.getCustomer().getName()).isEqualTo("Test Customer");
        verify(apparelOrderMapper, times(1)).apparelOrderDtoToApparelOrder(any(ApparelOrderDto.class));
        verify(apparelOrderRepository, times(1)).save(any(ApparelOrder.class));
        verify(apparelOrderMapper, times(1)).apparelOrderToApparelOrderDto(any(ApparelOrder.class));
    }

    @Test
    void deleteApparelOrderById() {
        // When
        apparelOrderService.deleteApparelOrderById(1);

        // Then
        verify(apparelOrderRepository, times(1)).deleteById(1);
    }
}
