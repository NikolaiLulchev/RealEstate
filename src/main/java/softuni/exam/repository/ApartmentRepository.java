package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;
import softuni.exam.models.enums.ApartmentType;

import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    boolean existsByTownAndArea(Town townId, double area);

    ApartmentType findByApartmentType(ApartmentType apartmentType);

    Optional<Apartment> findById(Long id);
}
