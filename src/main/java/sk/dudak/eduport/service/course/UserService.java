package sk.dudak.eduport.service.course;

import sk.dudak.eduport.model.course.Course;
import sk.dudak.eduport.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();
    List<Course> getEnrolledCourses(String username);
    List<User> getStudents();
    Optional<User> getById(long id);
    boolean enrollToCourse(String username, String courseName) throws Exception;
}
