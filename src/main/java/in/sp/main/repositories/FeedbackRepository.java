package in.sp.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sp.main.entities.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>
{

}
