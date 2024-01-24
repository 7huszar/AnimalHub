package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationSumDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.DonationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.DonationMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Donation;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.repository.DonationRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.DonationService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final ShelterRepository shelterRepository;
    private final DonationMapper donationMapper;

    @Transactional
    @Override
    public List<DonationSumDto> getDonationsByReceiver(UUID shelterId) {
        return donationRepository.getAnimalDonations(shelterId)
            .map(donationSumDto -> new DonationSumDto(donationSumDto.receiver(), donationSumDto.name(),
                donationSumDto.amount() != null ? donationSumDto.amount() : new BigDecimal(0)))
            .collect(Collectors
                .toList());
    }

    @Override
    public List<DonationListDto> findListedDonations(UUID shelterId, DonationSearchDto searchDto) {
        Pageable pageable = PageRequest.of(searchDto.page(), 50, Sort.by("stamp"));

        Specification<Donation> spec = createSpecification(
            searchDto.dateAfter() != null ? LocalDateTime.of(searchDto.dateAfter(), LocalTime.of(0, 0, 0)) : null,
            searchDto.dateBefore() != null ? LocalDateTime.of(searchDto.dateBefore(), LocalTime.of(0, 0, 0)) : null,
            searchDto.id(),
            searchDto.amountAfter(),
            searchDto.amountBefore(),
            shelterRepository.findShelterById(shelterId)
        );

        Page<Donation> donationPage = donationRepository.findAll(spec, pageable);

        return donationPage.stream().map(donationMapper::donationToDonationListDto).collect(Collectors.toList());
    }

    private Specification<Donation> createSpecification(
        LocalDateTime stampAfter,
        LocalDateTime stampBefore,
        String id,
        BigDecimal amountAfter,
        BigDecimal amountBefore,
        Shelter shelter) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (stampAfter != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("stamp"), stampAfter));
            }
            if (stampBefore != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("stamp"), stampBefore));
            }
            if (id != null && !id.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("id")), "%" + id.toLowerCase() + "%"));
            }
            if (amountAfter != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), amountAfter));
            }
            if (amountBefore != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), amountBefore));
            }
            if (shelter != null) {
                predicates.add(criteriaBuilder.equal(root.get("shelter"), shelter));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
