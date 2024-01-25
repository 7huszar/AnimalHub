package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationPageDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationSumDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.DonationMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.entity.Donation;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.repository.AnimalRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.DonationRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.DonationService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DonationRepository donationRepository;
    private final ShelterRepository shelterRepository;
    private final DonationMapper donationMapper;
    private final AnimalRepository animalRepository;

    @Transactional
    @Override
    public List<DonationSumDto> getDonationsByReceiver(UUID shelterId) {
        LOGGER.trace("getDonationsByReceiver({})", shelterId);

        return donationRepository.getAnimalDonations(shelterId)
            .map(donationSumDto -> new DonationSumDto(donationSumDto.receiver(), donationSumDto.name(),
                donationSumDto.amount() != null ? donationSumDto.amount() : new BigDecimal(0)))
            .collect(Collectors
                .toList());
    }

    @Override
    public DonationPageDto findListedDonations(UUID shelterId, DonationSearchDto searchDto) {
        LOGGER.trace("findListedDonations({}, {})", shelterId, searchDto);

        Specification<Donation> spec = createSpecification(
            searchDto.dateAfter() != null ? LocalDateTime.of(searchDto.dateAfter(), LocalTime.of(0, 0, 0)) : null,
            searchDto.dateBefore() != null ? LocalDateTime.of(searchDto.dateBefore(), LocalTime.of(0, 0, 0)) : null,
            searchDto.id(),
            searchDto.amountAfter(),
            searchDto.amountBefore(),
            shelterRepository.findShelterById(shelterId)
        );

        int total = donationRepository.count(spec);
        int actual = searchDto.page();

        if (total < searchDto.pageSize() * searchDto.page()) {
            actual = searchDto.page() / searchDto.pageSize();
            actual += total % searchDto.pageSize() == 0 ? 0 : 1;
        }

        Pageable pageable = PageRequest.of(actual, searchDto.pageSize(), Sort.by("stamp"));


        Page<Donation> donationPage = donationRepository.findAll(spec, pageable);

        return new DonationPageDto(donationPage.stream()
            .map(donationMapper::donationToDonationListDto)
            .map(donationList -> {
                UUID id = UUID.fromString(donationList.receiver());
                Animal animal = animalRepository.findAnimalById(id);
                if (animal != null) {
                    return new DonationListDto(
                        donationList.id(),
                        donationList.amount(),
                        donationList.date(),
                        donationList.type(),
                        animal.getName());
                }
                Shelter shelter = shelterRepository.findShelterById(id);
                if (shelter != null) {
                    return new DonationListDto(
                        donationList.id(),
                        donationList.amount(),
                        donationList.date(),
                        donationList.type(),
                        shelter.getName());
                }
                shelter = shelterRepository.findShelterById(shelterId);
                if (shelter != null) {
                    return new DonationListDto(
                        donationList.id(),
                        donationList.amount(),
                        donationList.date(),
                        donationList.type(),
                        shelter.getName());
                }
                return new DonationListDto(
                    donationList.id(),
                    donationList.amount(),
                    donationList.date(),
                    donationList.type(),
                    "error");
            })
            .collect(Collectors.toList()), actual, total % searchDto.pageSize() == 0 ? 0 : 1 + total / searchDto.pageSize());
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
