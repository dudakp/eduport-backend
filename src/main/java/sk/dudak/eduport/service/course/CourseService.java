package sk.dudak.eduport.service.course;

import sk.dudak.eduport.model.course.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> getAll();
    boolean addCourse(Course course) throws Exception;
    Optional<Course> getCourseByName(String courseName) throws Exception;
    boolean courseTitleExists(Course course);
}
