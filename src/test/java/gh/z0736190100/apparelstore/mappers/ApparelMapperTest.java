package gh.z0736190100.apparelstore.mappers;

import gh.z0736190100.apparelstore.entities.Apparel;
import gh.z0736190100.apparelstore.models.ApparelPatchDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ApparelMapperTest {

    private final ApparelMapper apparelMapper = Mappers.getMapper(ApparelMapper.class);

    @Test
    void updateApparelFromPatchDto_ShouldUpdateOnlyNonNullFields() {
        // Given
        Apparel apparel = Apparel.builder()
                .id(1)
                .apparelName("Original Apparel")
                .apparelStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .description("Original description")
                .build();

        ApparelPatchDto patchDto = ApparelPatchDto.builder()
                .apparelName("Updated Apparel")
                .price(new BigDecimal("14.99"))
                .build();

        // When
        apparelMapper.updateApparelFromPatchDto(patchDto, apparel);

        // Then
        assertThat(apparel.getId()).isEqualTo(1); // Should not change
        assertThat(apparel.getApparelName()).isEqualTo("Updated Apparel"); // Should be updated
        assertThat(apparel.getApparelStyle()).isEqualTo("IPA"); // Should not change
        assertThat(apparel.getUpc()).isEqualTo("123456"); // Should not change
        assertThat(apparel.getPrice()).isEqualTo(new BigDecimal("14.99")); // Should be updated
        assertThat(apparel.getQuantityOnHand()).isEqualTo(100); // Should not change
        assertThat(apparel.getDescription()).isEqualTo("Original description"); // Should not change
    }
}