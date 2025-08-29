package gh.z0736190100.apparelstore.mappers;

import gh.z0736190100.apparelstore.entities.Apparel;
import gh.z0736190100.apparelstore.entities.ApparelOrderLine;
import gh.z0736190100.apparelstore.models.ApparelOrderLineDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ApparelOrderLineMapperTest {

    private ApparelOrderLineMapper apparelOrderLineMapper;
    private ApparelOrderLine testApparelOrderLine;
    private Apparel testApparel;

    @BeforeEach
    void setUp() {
        apparelOrderLineMapper = Mappers.getMapper(ApparelOrderLineMapper.class);
        
        // Create test apparel
        testApparel = Apparel.builder()
                .apparelName("Test Apparel")
                .apparelStyle("Loose")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        testApparel.setId(1);
        
        // Create test apparel order line
        testApparelOrderLine = ApparelOrderLine.builder()
                .orderQuantity(2)
                .quantityAllocated(2)
                .status("ALLOCATED")
                .apparel(testApparel)
                .build();
        testApparelOrderLine.setId(1);
    }

    @Test
    void testApparelOrderLineToApparelOrderLineDto() {
        // When
        ApparelOrderLineDto apparelOrderLineDto = apparelOrderLineMapper.apparelOrderLineToApparelOrderLineDto(testApparelOrderLine);
        
        // Then
        assertThat(apparelOrderLineDto).isNotNull();
        assertThat(apparelOrderLineDto.getId()).isEqualTo(testApparelOrderLine.getId());
        assertThat(apparelOrderLineDto.getApparelId()).isEqualTo(testApparel.getId());
        assertThat(apparelOrderLineDto.getApparelName()).isEqualTo(testApparel.getApparelName());
        assertThat(apparelOrderLineDto.getApparelStyle()).isEqualTo(testApparel.getApparelStyle());
        assertThat(apparelOrderLineDto.getUpc()).isEqualTo(testApparel.getUpc());
        assertThat(apparelOrderLineDto.getOrderQuantity()).isEqualTo(testApparelOrderLine.getOrderQuantity());
        assertThat(apparelOrderLineDto.getQuantityAllocated()).isEqualTo(testApparelOrderLine.getQuantityAllocated());
        assertThat(apparelOrderLineDto.getStatus()).isEqualTo(testApparelOrderLine.getStatus());
    }

    @Test
    void testApparelOrderLineDtoToApparelOrderLine() {
        // Given
        ApparelOrderLineDto apparelOrderLineDto = ApparelOrderLineDto.builder()
                .id(2)
                .apparelId(1)
                .apparelName("Test Apparel")
                .apparelStyle("Loose")
                .upc("123456")
                .orderQuantity(3)
                .quantityAllocated(0)
                .status("NEW")
                .build();
        
        // When
        ApparelOrderLine apparelOrderLine = apparelOrderLineMapper.apparelOrderLineDtoToApparelOrderLine(apparelOrderLineDto);
        
        // Then
        assertThat(apparelOrderLine).isNotNull();
        assertThat(apparelOrderLine.getId()).isNull(); // ID should be ignored in mapping
        assertThat(apparelOrderLine.getApparel()).isNull(); // Apparel should be ignored in mapping
        assertThat(apparelOrderLine.getOrderQuantity()).isEqualTo(apparelOrderLineDto.getOrderQuantity());
        assertThat(apparelOrderLine.getQuantityAllocated()).isEqualTo(apparelOrderLineDto.getQuantityAllocated());
        assertThat(apparelOrderLine.getStatus()).isEqualTo(apparelOrderLineDto.getStatus());
    }
}