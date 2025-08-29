package gh.z0736190100.apparelstore.services;

import gh.z0736190100.apparelstore.entities.Apparel;
import gh.z0736190100.apparelstore.mappers.ApparelMapper;
import gh.z0736190100.apparelstore.models.ApparelDto;
import gh.z0736190100.apparelstore.models.ApparelPatchDto;
import gh.z0736190100.apparelstore.repositories.ApparelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApparelServiceImplTest {

    @Mock
    ApparelRepository apparelRepository;

    @Mock
    ApparelMapper apparelMapper;

    @InjectMocks
    ApparelServiceImpl apparelService;

    Apparel testApparel;
    ApparelDto testApparelDto;

    @BeforeEach
    void setUp() {
        testApparel = Apparel.builder()
                .id(1)
                .apparelName("Test Apparel")
                .apparelStyle("Loose")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        testApparelDto = ApparelDto.builder()
                .id(1)
                .apparelName("Test Apparel")
                .apparelStyle("Loose")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
    }

    @Test
    void getAllApparels() {
        // Given
        when(apparelRepository.findAll()).thenReturn(Arrays.asList(testApparel));
        when(apparelMapper.apparelToApparelDto(testApparel)).thenReturn(testApparelDto);

        // When
        List<ApparelDto> apparels = apparelService.getAllApparels();

        // Then
        assertThat(apparels).hasSize(1);
        assertThat(apparels.get(0).getApparelName()).isEqualTo("Test Apparel");
        verify(apparelRepository, times(1)).findAll();
        verify(apparelMapper, times(1)).apparelToApparelDto(any(Apparel.class));
    }

    @Test
    void getAllApparelsWithPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 20);
        List<Apparel> apparels = Arrays.asList(testApparel);
        Page<Apparel> apparelPage = new PageImpl<>(apparels, pageable, 1);

        when(apparelRepository.findAllByApparelNameContainingIgnoreCaseAndApparelStyleContainingIgnoreCase("", "", pageable)).thenReturn(apparelPage);
        when(apparelMapper.apparelToApparelDto(testApparel)).thenReturn(testApparelDto);

        // When
        Page<ApparelDto> result = apparelService.getAllApparels(null, null, pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getApparelName()).isEqualTo("Test Apparel");
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(apparelRepository, times(1)).findAllByApparelNameContainingIgnoreCaseAndApparelStyleContainingIgnoreCase("", "", pageable);
        verify(apparelMapper, times(1)).apparelToApparelDto(any(Apparel.class));
    }

    @Test
    void getAllApparelsWithApparelNameFilter() {
        // Given
        String apparelName = "Test";
        Pageable pageable = PageRequest.of(0, 20);
        List<Apparel> apparels = Arrays.asList(testApparel);
        Page<Apparel> apparelPage = new PageImpl<>(apparels, pageable, 1);

        when(apparelRepository.findAllByApparelNameContainingIgnoreCase(apparelName, pageable)).thenReturn(apparelPage);
        when(apparelMapper.apparelToApparelDto(testApparel)).thenReturn(testApparelDto);

        // When
        Page<ApparelDto> result = apparelService.getAllApparels(apparelName, null, pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getApparelName()).isEqualTo("Test Apparel");
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(apparelRepository, times(1)).findAllByApparelNameContainingIgnoreCase(apparelName, pageable);
        verify(apparelMapper, times(1)).apparelToApparelDto(any(Apparel.class));
    }

    @Test
    void getAllApparelsWithApparelStyleFilter() {
        // Given
        String apparelStyle = "Loose";
        Pageable pageable = PageRequest.of(0, 20);
        List<Apparel> apparels = Arrays.asList(testApparel);
        Page<Apparel> apparelPage = new PageImpl<>(apparels, pageable, 1);

        when(apparelRepository.findAllByApparelNameContainingIgnoreCaseAndApparelStyleContainingIgnoreCase("", apparelStyle, pageable)).thenReturn(apparelPage);
        when(apparelMapper.apparelToApparelDto(testApparel)).thenReturn(testApparelDto);

        // When
        Page<ApparelDto> result = apparelService.getAllApparels(null, apparelStyle, pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getApparelName()).isEqualTo("Test Apparel");
        assertThat(result.getContent().get(0).getApparelStyle()).isEqualTo("Loose");
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(apparelRepository, times(1)).findAllByApparelNameContainingIgnoreCaseAndApparelStyleContainingIgnoreCase("", apparelStyle, pageable);
        verify(apparelMapper, times(1)).apparelToApparelDto(any(Apparel.class));
    }

    @Test
    void getAllApparelsWithApparelNameAndApparelStyleFilter() {
        // Given
        String apparelName = "Test";
        String apparelStyle = "Loose";
        Pageable pageable = PageRequest.of(0, 20);
        List<Apparel> apparels = Arrays.asList(testApparel);
        Page<Apparel> apparelPage = new PageImpl<>(apparels, pageable, 1);

        when(apparelRepository.findAllByApparelNameContainingIgnoreCaseAndApparelStyleContainingIgnoreCase(apparelName, apparelStyle, pageable)).thenReturn(apparelPage);
        when(apparelMapper.apparelToApparelDto(testApparel)).thenReturn(testApparelDto);

        // When
        Page<ApparelDto> result = apparelService.getAllApparels(apparelName, apparelStyle, pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getApparelName()).isEqualTo("Test Apparel");
        assertThat(result.getContent().get(0).getApparelStyle()).isEqualTo("Loose");
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(apparelRepository, times(1)).findAllByApparelNameContainingIgnoreCaseAndApparelStyleContainingIgnoreCase(apparelName, apparelStyle, pageable);
        verify(apparelMapper, times(1)).apparelToApparelDto(any(Apparel.class));
    }

    @Test
    void getApparelById() {
        // Given
        when(apparelRepository.findById(1)).thenReturn(Optional.of(testApparel));
        when(apparelMapper.apparelToApparelDto(testApparel)).thenReturn(testApparelDto);

        // When
        Optional<ApparelDto> apparelOptional = apparelService.getApparelById(1);

        // Then
        assertThat(apparelOptional).isPresent();
        assertThat(apparelOptional.get().getApparelName()).isEqualTo("Test Apparel");
        verify(apparelRepository, times(1)).findById(1);
        verify(apparelMapper, times(1)).apparelToApparelDto(any(Apparel.class));
    }

    @Test
    void getApparelByIdNotFound() {
        // Given
        when(apparelRepository.findById(1)).thenReturn(Optional.empty());

        // When
        Optional<ApparelDto> apparelOptional = apparelService.getApparelById(1);

        // Then
        assertThat(apparelOptional).isEmpty();
        verify(apparelRepository, times(1)).findById(1);
    }

    @Test
    void saveApparel() {
        // Given
        ApparelDto apparelDtoToSave = ApparelDto.builder()
                .apparelName("New Apparel")
                .apparelStyle("Fit")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        Apparel apparelToSave = Apparel.builder()
                .apparelName("New Apparel")
                .apparelStyle("Fit")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        Apparel savedApparel = Apparel.builder()
                .id(2)
                .apparelName("New Apparel")
                .apparelStyle("Fit")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        ApparelDto savedApparelDto = ApparelDto.builder()
                .id(2)
                .apparelName("New Apparel")
                .apparelStyle("Fit")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        when(apparelMapper.apparelDtoToApparel(apparelDtoToSave)).thenReturn(apparelToSave);
        when(apparelRepository.save(any(Apparel.class))).thenReturn(savedApparel);
        when(apparelMapper.apparelToApparelDto(savedApparel)).thenReturn(savedApparelDto);

        // When
        ApparelDto result = apparelService.saveApparel(apparelDtoToSave);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2);
        assertThat(result.getApparelName()).isEqualTo("New Apparel");
        verify(apparelMapper, times(1)).apparelDtoToApparel(any(ApparelDto.class));
        verify(apparelRepository, times(1)).save(any(Apparel.class));
        verify(apparelMapper, times(1)).apparelToApparelDto(any(Apparel.class));
    }

    @Test
    void updateApparel() {
        // Given
        ApparelDto apparelDtoToUpdate = ApparelDto.builder()
                .id(1)
                .apparelName("Updated Apparel")
                .apparelStyle("Oversize")
                .upc("789012")
                .price(new BigDecimal("16.99"))
                .quantityOnHand(150)
                .build();

        Apparel apparelToUpdate = Apparel.builder()
                .id(1)
                .apparelName("Updated Apparel")
                .apparelStyle("Oversize")
                .upc("789012")
                .price(new BigDecimal("16.99"))
                .quantityOnHand(150)
                .build();

        Apparel updatedApparel = Apparel.builder()
                .id(1)
                .apparelName("Updated Apparel")
                .apparelStyle("Oversize")
                .upc("789012")
                .price(new BigDecimal("16.99"))
                .quantityOnHand(150)
                .build();

        ApparelDto updatedApparelDto = ApparelDto.builder()
                .id(1)
                .apparelName("Updated Apparel")
                .apparelStyle("Oversize")
                .upc("789012")
                .price(new BigDecimal("16.99"))
                .quantityOnHand(150)
                .build();

        when(apparelMapper.apparelDtoToApparel(apparelDtoToUpdate)).thenReturn(apparelToUpdate);
        when(apparelRepository.save(any(Apparel.class))).thenReturn(updatedApparel);
        when(apparelMapper.apparelToApparelDto(updatedApparel)).thenReturn(updatedApparelDto);

        // When
        ApparelDto result = apparelService.saveApparel(apparelDtoToUpdate);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getApparelName()).isEqualTo("Updated Apparel");
        verify(apparelMapper, times(1)).apparelDtoToApparel(any(ApparelDto.class));
        verify(apparelRepository, times(1)).save(any(Apparel.class));
        verify(apparelMapper, times(1)).apparelToApparelDto(any(Apparel.class));
    }

    @Test
    void deleteApparelById() {
        // Given
        doNothing().when(apparelRepository).deleteById(anyInt());

        // When
        apparelService.deleteApparelById(1);

        // Then
        verify(apparelRepository, times(1)).deleteById(1);
    }

    @Test
    void patchApparelFound() {
        // Given
        ApparelPatchDto apparelPatchDto = ApparelPatchDto.builder()
                .apparelName("Patched Apparel")
                .price(new BigDecimal("15.99"))
                .build();

        Apparel existingApparel = Apparel.builder()
                .id(1)
                .apparelName("Original Apparel")
                .apparelStyle("Loose")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Apparel patchedApparel = Apparel.builder()
                .id(1)
                .apparelName("Patched Apparel")
                .apparelStyle("Loose")
                .upc("123456")
                .price(new BigDecimal("15.99"))
                .quantityOnHand(100)
                .build();

        ApparelDto patchedApparelDto = ApparelDto.builder()
                .id(1)
                .apparelName("Patched Apparel")
                .apparelStyle("Loose")
                .upc("123456")
                .price(new BigDecimal("15.99"))
                .quantityOnHand(100)
                .build();

        when(apparelRepository.findById(1)).thenReturn(Optional.of(existingApparel));
        doNothing().when(apparelMapper).updateApparelFromPatchDto(apparelPatchDto, existingApparel);
        when(apparelRepository.save(existingApparel)).thenReturn(patchedApparel);
        when(apparelMapper.apparelToApparelDto(patchedApparel)).thenReturn(patchedApparelDto);

        // When
        Optional<ApparelDto> result = apparelService.patchApparel(1, apparelPatchDto);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getApparelName()).isEqualTo("Patched Apparel");
        assertThat(result.get().getPrice()).isEqualTo(new BigDecimal("15.99"));
        verify(apparelRepository, times(1)).findById(1);
        verify(apparelMapper, times(1)).updateApparelFromPatchDto(apparelPatchDto, existingApparel);
        verify(apparelRepository, times(1)).save(existingApparel);
        verify(apparelMapper, times(1)).apparelToApparelDto(patchedApparel);
    }

    @Test
    void patchApparelNotFound() {
        // Given
        ApparelPatchDto apparelPatchDto = ApparelPatchDto.builder()
                .apparelName("Patched Apparel")
                .build();

        when(apparelRepository.findById(1)).thenReturn(Optional.empty());

        // When
        Optional<ApparelDto> result = apparelService.patchApparel(1, apparelPatchDto);

        // Then
        assertThat(result).isEmpty();
        verify(apparelRepository, times(1)).findById(1);
        verify(apparelMapper, never()).updateApparelFromPatchDto(any(), any());
        verify(apparelRepository, never()).save(any());
    }
}
