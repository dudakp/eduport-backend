package sk.dudak.eduport.service.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.dudak.eduport.dao.course.CourseRepository;
import sk.dudak.eduport.model.course.Course;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getAll() {
        return this.courseRepository.findAll();
    }

    @Override
    public boolean addCourse(Course course) {
        if (!courseTitleExists(course)) {
            this.courseRepository.save(course);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Course> getCourseByName(String courseName) {
        return this.getAll().stream().filter(course -> course.getAbbreviation().equals(courseName)).findFirst();
    }

    @Override
    public boolean courseTitleExists(Course course) {
        return this.getAll().stream().anyMatch(course1 -> course1.getTitle().equals(course.getTitle()));
    }

}
