package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Agent;

import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {

    @Query("select (count(a) > 0) from Agent a where a.firstName = ?1")
    boolean existsByFirstName(String firstName);

    @Query("select a from Agent a where a.firstName = ?1")
    Optional<Agent> findByFirstName(String agentFirstName);
}
