package sk.dudak.eduport.service.course;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sk.dudak.eduport.dao.course.CourseRepository;
import sk.dudak.eduport.dao.course.UserRepository;
import sk.dudak.eduport.model.course.Course;
import sk.dudak.eduport.model.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class UserServiceImplTest {

    private UserService userService;
    private User student;


    @Before
    public void setUp() throws Exception {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
        CourseService courseService = Mockito.mock(CourseService.class);
        User user = Mockito.mock(User.class);
        this.userService = new UserServiceImpl(userRepository, courseRepository, courseService);
        Course mat2 = Course.builder()
                            .id(1)
                            .title("Mathematics 2")
                            .abbreviation("MAT2")
                            .users(Collections.emptyList())
                            .build();
        Course api = Course.builder()
                            .id(1)
                            .title("Computer architecture")
                            .abbreviation("API")
                            .users(Collections.emptyList())
                            .build();
        student = User.builder()
                      .id(4)
                      .username("student")
                      .password("student")
                      .roles(new ArrayList<String>() {{
                          add("ROLE_STUDENT");
                      }})
                      .coursesEnrolled(new ArrayList<Course>(){{
                          add(api);
                      }})
                      .build();
        User teacher = User.builder()
                           .id(5)
                           .username("teacher")
                           .password("teacher")
                           .roles(new ArrayList<String>() {{
                               add("ROLE_TEACHER");
                           }})
                           .build();
        List<User> users = new ArrayList<>();
        users.add(student);
        users.add(teacher);

        Mockito.when(userRepository.findAll()).thenReturn(users);
        Mockito.when(userRepository.findById((long) 4)).thenReturn(Optional.ofNullable(student));
        Mockito.when(userRepository.findByUsername("student")).thenReturn(Optional.ofNullable(student));
        Mockito.when(courseRepository
                .save(mat2)).thenReturn(mat2);
        Mockito.when(courseService.getCourseByName("MAT2")).thenReturn(Optional.of(mat2));
        Mockito.when(courseService.getCourseByName("API")).thenReturn(Optional.of(api));
        Mockito.when(user.addCourse(mat2)).thenReturn(true);
    }

    @Test
    public void getAllUsers() {
        Assert.assertThat(this.userService.getAll(), hasSize(2));
    }

    @Test
    public void getAllStudents() {
        Assert.assertThat(this.userService.getStudents(), hasSize(1));
    }

    @Test
    public void getById() {
        Assert.assertTrue(this.userService.getById(4).isPresent());
    }

    @Test
    public void getByIdWhenIdDoesNotExist() {
        Assert.assertFalse(this.userService.getById(100000).isPresent());
    }

    @Test
    public void getEnrolledCourses() {
        Assert.assertThat(this.userService.getEnrolledCourses("student"), hasSize(1));
    }

    @Test
    public void enrollToCourse() throws Exception {
        Assert.assertTrue(this.userService.enrollToCourse(student.getUsername(), "MAT2"));
    }

    @Test
    public void enrollToEnrolledCourse() throws Exception {
        Assert.assertFalse(this.userService.enrollToCourse(student.getUsername(), "API"));
    }

    @Test
    public void enrollToCourseWhenCourseDoesNotExists() throws Exception {
        Assert.assertFalse(this.userService.enrollToCourse(student.getUsername(), "nonexistentCourse"));
    }

    @Test
    public void enrollToCourseWhenUserDoesNotExists() throws Exception {
        Assert.assertFalse(this.userService.enrollToCourse("nonexistentUser", "nonexistentCourse"));
    }
}
