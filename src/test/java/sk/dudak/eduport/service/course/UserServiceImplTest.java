package sk.dudak.eduport.service.course;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sk.dudak.eduport.dao.course.CourseRepository;
import sk.dudak.eduport.dao.course.UserRepository;
import sk.dudak.eduport.model.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    private UserRepository userRepository;
    private CourseRepository courseRepository;
    private CourseService courseService;

    private User student;
    private User teacher;
    private List<User> users;


    @Before
    public void setUp() {
        this.userService = Mockito.mock(UserService.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.courseService = Mockito.mock(CourseService.class);
        student = User.builder()
                .id(4)
                .username("student")
                .password("student")
                .roles(new ArrayList<String>() {{
                    add("ROLE_STUDENT");
                }})
                .build();
        teacher = User.builder()
                .id(5)
                .username("teacher")
                .password("teacher")
                .roles(new ArrayList<String>() {{
                    add("ROLE_TEACHER");
                }})
                .build();
        this.users = new ArrayList<>();
        this.users.add(student);
        this.users.add(teacher);
    }

    @Test
    public void getAllUsers() {
        Mockito.when(this.userService.getAll()).thenReturn(users);

        Assert.assertThat(this.userService.getAll(), hasSize(2));
    }

    @Test
    public void getAllStudents() {
        Mockito.when(this.userService.getStudents()).thenReturn(Collections.singletonList(student));

        Assert.assertThat(this.userService.getStudents(), hasSize(1));
    }

    @Test
    public void getById() {
        Mockito.when(this.userService.getById(4)).thenReturn(java.util.Optional.ofNullable(student));

        Assert.assertTrue(this.userService.getById(4).isPresent());
    }

    @Test
    public void getByIdWhenIdDoesNotExist() {
        Mockito.when(this.userService.getById(100000)).thenReturn(Optional.empty());

        Assert.assertFalse(this.userService.getById(100000).isPresent());
    }

    @Test
    public void enrollToCourse() throws Exception {
        Mockito.when(this.userService.enrollToCourse(student.getUsername(), "MAT2")).thenReturn(true);

        Assert.assertTrue(this.userService.enrollToCourse(student.getUsername(), "MAT2"));
    }

    @Test
    public void enrollToCourseWhenCourseDoesNotExists() throws Exception {
        Mockito.when(this.userService.enrollToCourse(student.getUsername(), "nonexistentCourse")).thenReturn(false);

        Assert.assertFalse(this.userService.enrollToCourse(student.getUsername(), "nonexistentCourse"));
    }
}
