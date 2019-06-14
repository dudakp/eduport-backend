package sk.dudak.eduport.dao.course;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.dudak.eduport.model.course.Contribution;

import java.util.List;

public interface ContributionRepository extends JpaRepository<Contribution, Long> {
}
