package gh.z0736190100.apparelstore.services;

import gh.z0736190100.apparelstore.entities.Apparel;
import gh.z0736190100.apparelstore.entities.ApparelOrder;
import gh.z0736190100.apparelstore.entities.ApparelOrderLine;
import gh.z0736190100.apparelstore.mappers.ApparelOrderLineMapper;
import gh.z0736190100.apparelstore.mappers.ApparelOrderMapper;
import gh.z0736190100.apparelstore.models.ApparelOrderDto;
import gh.z0736190100.apparelstore.repositories.ApparelOrderRepository;
import gh.z0736190100.apparelstore.repositories.ApparelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of ApparelOrderService that uses ApparelOrderRepository for persistence
 */
@Service
public class ApparelOrderServiceImpl implements ApparelOrderService {

    private final ApparelOrderRepository apparelOrderRepository;
    private final ApparelRepository apparelRepository;
    private final ApparelOrderMapper apparelOrderMapper;
    private final ApparelOrderLineMapper apparelOrderLineMapper;

    public ApparelOrderServiceImpl(ApparelOrderRepository apparelOrderRepository,
                               ApparelRepository apparelRepository,
                               ApparelOrderMapper apparelOrderMapper,
                               ApparelOrderLineMapper apparelOrderLineMapper) {
        this.apparelOrderRepository = apparelOrderRepository;
        this.apparelRepository = apparelRepository;
        this.apparelOrderMapper = apparelOrderMapper;
        this.apparelOrderLineMapper = apparelOrderLineMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApparelOrderDto> getAllApparelOrders() {
        return apparelOrderRepository.findAll().stream()
                .map(apparelOrderMapper::apparelOrderToApparelOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApparelOrderDto> getApparelOrderById(Integer id) {
        return apparelOrderRepository.findById(id)
                .map(apparelOrderMapper::apparelOrderToApparelOrderDto);
    }

    @Override
    @Transactional
    public ApparelOrderDto saveApparelOrder(ApparelOrderDto apparelOrderDto) {
        ApparelOrder apparelOrder = apparelOrderMapper.apparelOrderDtoToApparelOrder(apparelOrderDto);
        
        // Process apparel order lines
        if (apparelOrderDto.getApparelOrderLines() != null) {
            apparelOrderDto.getApparelOrderLines().forEach(lineDto -> {
                // Create a new apparel order line
                ApparelOrderLine line = apparelOrderLineMapper.apparelOrderLineDtoToApparelOrderLine(lineDto);
                
                // Find and set the apparel reference
                if (lineDto.getApparelId() != null) {
                    Optional<Apparel> apparelOptional = apparelRepository.findById(lineDto.getApparelId());
                    apparelOptional.ifPresent(line::setApparel);
                }
                
                // Add the line to the order
                apparelOrder.addApparelOrderLine(line);
            });
        }
        
        ApparelOrder savedApparelOrder = apparelOrderRepository.save(apparelOrder);
        return apparelOrderMapper.apparelOrderToApparelOrderDto(savedApparelOrder);
    }

    @Override
    @Transactional
    public void deleteApparelOrderById(Integer id) {
        apparelOrderRepository.deleteById(id);
    }
}