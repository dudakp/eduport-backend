package sk.dudak.eduport.service.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.dudak.eduport.dao.course.CourseRepository;
import sk.dudak.eduport.dao.course.UserRepository;
import sk.dudak.eduport.model.course.Course;
import sk.dudak.eduport.model.user.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CourseRepository courseRepository, CourseService courseService) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.courseService = courseService;
    }


    @Override
    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    @Override
    public List<User> getStudents() {
        return this.userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains("ROLE_STUDENT"))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> getById(long id) {
        return this.userRepository.findById(id);
    }

    @Override
    public List<Course> getEnrolledCourses(String username) {
        final Optional<User> byId = this.userRepository.findByUsername(username);
        return byId.map(User::getCoursesEnrolled).orElse(Collections.emptyList());
    }

    @Override
    public boolean enrollToCourse(String username, String courseName) throws Exception {
        final Optional<User> byUsername = this.userRepository.findByUsername(username);
        final Optional<Course> courseByName = this.courseService.getCourseByName(courseName);
        if (byUsername.isPresent() && courseByName.isPresent()) {
            if (byUsername.get().getCoursesEnrolled().contains(courseByName.get()))
                return false;
            byUsername.get().getCoursesEnrolled().add(courseByName.get());
            this.courseRepository.save(courseByName.get());
            return true;
        }
        return false;
    }

}
