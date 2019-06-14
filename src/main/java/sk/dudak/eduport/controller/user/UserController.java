package sk.dudak.eduport.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.dudak.eduport.dao.course.UserRepository;
import sk.dudak.eduport.model.course.Course;
import sk.dudak.eduport.model.user.User;
import sk.dudak.eduport.service.course.UserService;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<User> getUser(@RequestParam String username) {
        final Optional<User> byUsername = this.userRepository.findByUsername(username);
        if (byUsername.isPresent())
            return ok(byUsername.get());
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/byId")
    public ResponseEntity<User> getById(@RequestParam long id) {
        final Optional<User> byId = this.userService.getById(id);
        if (byId.isPresent())
            return ok(byId.get());
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/allStudents")
    public ResponseEntity<List<User>> getStudents() {
        return ok(this.userService.getStudents());
    }

    //TODO: delete, use only one in courseController
    @GetMapping(value = "/enrolled")
    public ResponseEntity<List<Course>> getEnrolledCourses(@RequestParam String username) {
        return ok(this.userRepository.findByUsername(username).get().getCoursesEnrolled());
    }

    @PostMapping
    @RequestMapping(value = "/enroll")
    public ResponseEntity enrollToCourse(@RequestParam String username,
                                         @RequestParam String courseName) throws Exception {
        if (this.userService.enrollToCourse(username, courseName))
            return new ResponseEntity(HttpStatus.OK);
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}


