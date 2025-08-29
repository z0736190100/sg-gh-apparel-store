package gh.z0736190100.apparelstore.services;

import gh.z0736190100.apparelstore.entities.Apparel;
import gh.z0736190100.apparelstore.mappers.ApparelMapper;
import gh.z0736190100.apparelstore.models.ApparelDto;
import gh.z0736190100.apparelstore.models.ApparelPatchDto;
import gh.z0736190100.apparelstore.repositories.ApparelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of ApparelService that uses ApparelRepository for persistence
 */
@Service
public class ApparelServiceImpl implements ApparelService {

    private final ApparelRepository apparelRepository;
    private final ApparelMapper apparelMapper;

    public ApparelServiceImpl(ApparelRepository apparelRepository, ApparelMapper apparelMapper) {
        this.apparelRepository = apparelRepository;
        this.apparelMapper = apparelMapper;
    }

    @Override
    public List<ApparelDto> getAllApparels() {
        return apparelRepository.findAll().stream()
                .map(apparelMapper::apparelToApparelDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ApparelDto> getAllApparels(String apparelName, String apparelStyle, Pageable pageable) {
        // Handle different combinations of parameters
        boolean hasName = StringUtils.hasText(apparelName);
        boolean hasStyle = StringUtils.hasText(apparelStyle);

        Page<Apparel> apparelPage;

        if (hasName && hasStyle) {
            // Both parameters provided
            apparelPage = apparelRepository.findAllByApparelNameContainingIgnoreCaseAndApparelStyleContainingIgnoreCase(
                    apparelName, apparelStyle, pageable);
        } else if (hasName) {
            // Only apparelName provided
            apparelPage = apparelRepository.findAllByApparelNameContainingIgnoreCase(apparelName, pageable);
        } else if (hasStyle) {
            // Only apparelStyle provided - use the combined method with empty string for name
            apparelPage = apparelRepository.findAllByApparelNameContainingIgnoreCaseAndApparelStyleContainingIgnoreCase(
                    "", apparelStyle, pageable);
        } else {
            // No parameters provided - use the combined method with empty strings
            apparelPage = apparelRepository.findAllByApparelNameContainingIgnoreCaseAndApparelStyleContainingIgnoreCase(
                    "", "", pageable);
        }

        return apparelPage.map(apparelMapper::apparelToApparelDto);
    }

    @Override
    public Optional<ApparelDto> getApparelById(Integer id) {
        return apparelRepository.findById(id)
                .map(apparelMapper::apparelToApparelDto);
    }

    @Override
    public ApparelDto saveApparel(ApparelDto apparelDto) {
        Apparel apparel = apparelMapper.apparelDtoToApparel(apparelDto);
        Apparel savedApparel = apparelRepository.save(apparel);
        return apparelMapper.apparelToApparelDto(savedApparel);
    }

    @Override
    public void deleteApparelById(Integer id) {
        apparelRepository.deleteById(id);
    }

    @Override
    public Optional<ApparelDto> patchApparel(Integer id, ApparelPatchDto apparelPatchDto) {
        return apparelRepository.findById(id)
                .map(apparel -> {
                    // Update the apparel with non-null values from the patch DTO
                    apparelMapper.updateApparelFromPatchDto(apparelPatchDto, apparel);

                    // Save the updated apparel
                    Apparel savedApparel = apparelRepository.save(apparel);

                    // Convert back to DTO and return
                    return apparelMapper.apparelToApparelDto(savedApparel);
                });
    }
}
