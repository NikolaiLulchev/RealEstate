package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Town;

@Repository
public interface TownRepository extends JpaRepository<Town, Long> {

    @Query("select t from Town t where t.townName = ?1")
    Town findByTownName(String town);
}
