package sk.dudak.eduport.dao.course;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.dudak.eduport.model.course.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
