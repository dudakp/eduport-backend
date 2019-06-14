package sk.dudak.eduport.controller.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.dudak.eduport.dao.course.UserRepository;
import sk.dudak.eduport.model.course.Course;
import sk.dudak.eduport.model.user.User;
import sk.dudak.eduport.service.course.CourseService;
import sk.dudak.eduport.service.course.UserService;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "v1/course")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public CourseController(CourseService courseService, UserService userService, UserRepository userRepository) {
        this.courseService = courseService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return this.courseService.getAll();
    }

    @GetMapping(value = "/byName")
    public ResponseEntity<Course> getCourseByName(@RequestParam String name) throws Exception {
        final Optional<Course> courseByName = this.courseService.getCourseByName(name);
        if (courseByName.isPresent())
            return ok(courseByName.get());
        else
            return new ResponseEntity<>(courseByName.get(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/byUserId")
    public ResponseEntity<List<Course>> getAllForStudent(@RequestParam long id) {
        final Optional<User> byId = this.userService.getById(id);
        if (byId.isPresent())
            return ok(this.userService.getEnrolledCourses(byId.get().getUsername()));
        else
            return new ResponseEntity<>(byId.get().getCoursesEnrolled(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        if (this.courseService.addCourse(course))
            return ok(course);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
