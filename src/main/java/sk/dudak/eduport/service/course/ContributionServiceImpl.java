package sk.dudak.eduport.service.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.dudak.eduport.dao.course.ContributionRepository;
import sk.dudak.eduport.dao.course.CourseRepository;
import sk.dudak.eduport.dao.course.UserRepository;
import sk.dudak.eduport.model.course.Contribution;
import sk.dudak.eduport.model.course.Course;
import sk.dudak.eduport.model.user.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ContributionServiceImpl implements ContributionService {

    private final UserRepository userRepository;
    private final ContributionRepository contributionRepository;
    private final CourseService courseService;
    private final CourseRepository courseRepository;

    @Autowired
    public ContributionServiceImpl(UserRepository userRepository,
                                   ContributionRepository contributionRepository,
                                   CourseService courseService, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.contributionRepository = contributionRepository;
        this.courseService = courseService;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Contribution> getAll() {
        return this.contributionRepository.findAll();
    }

    @Override
    public List<Contribution> getAllById(long id) {
        return this.courseRepository.findById(id).get().getContributions();
    }

    @Override
    public boolean addContribution(Contribution contribution, String course) throws Exception {
        Objects.requireNonNull(contribution);
        final Optional<Course> courseByName = this.courseService.getCourseByName(course);
        if (courseByName.isPresent()) {
            contribution.setCourse(courseByName.get());
            courseByName.get().addContrib(contribution);
            this.contributionRepository.save(contribution);
            return true;
        } else
            return false;
    }

    @Override
    public List<Contribution> getAllForUser(String username) {
        final Optional<User> byUsername = this.userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return this.contributionRepository.findAll().stream()
                    .filter(contribution -> contribution.getCourse().getUsers()
                            .contains(byUsername.get()))
                    .collect(Collectors.toList());
        }
        else
            return Collections.emptyList();
    }

    @Override
    public List<Contribution> getAllForCourse(String courseName) {
        return this.contributionRepository.findAll().stream()
                .filter(contribution -> contribution.getCourse().getAbbreviation().equals(courseName))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteContribution(String contributionTitle) throws Exception {
        final Optional<Contribution> toDelete = this.contributionRepository.findAll().stream()
                .filter(contribution -> contribution.getTitle().equals(contributionTitle))
                .findFirst();
        if (toDelete.isPresent()) {
            this.contributionRepository.delete(toDelete.get());
            return true;
        } else
            return false;
    }
}
