package sk.dudak.eduport.service.course;

import sk.dudak.eduport.model.course.Contribution;

import java.util.List;

public interface ContributionService {
    boolean addContribution(Contribution contribution, String course) throws Exception;
    boolean deleteContribution(String contributionTitle) throws Exception;
    List<Contribution> getAll();
    List<Contribution> getAllForUser(String username);
    List<Contribution> getAllForCourse(String courseName);
    List<Contribution> getAllById(long id);
}

