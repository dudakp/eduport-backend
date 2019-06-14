package sk.dudak.eduport.controller.user;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sk.dudak.eduport.model.user.User;
import sk.dudak.eduport.service.course.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private User student;
    private User teacher;
    private List<User> users;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        student = User.builder()
                .id(4)
                .username("student")
                .password("student")
                .roles(new ArrayList<String>(){{add("ROLE_STUDENT");}})
                .build();
        teacher = User.builder()
                .id(5)
                .username("teacher")
                .password("teacher")
                .roles(new ArrayList<String>(){{add("ROLE_TEACHER");}})
                .build();
        this.users = new ArrayList<>();
        this.users.add(student);
        this.users.add(teacher);
    }

    @Test
    public void getUser() throws Exception {

    }

    @Test
    public void getByStudentId() throws Exception {
        Mockito.when(this.userController.getById(4)).thenReturn(ResponseEntity.ok(student));

        mockMvc.perform(get("/student/byId"))
                .andExpect(status().isOk());
    }

    @Test
    public void getByStudentIdWhenStudentDoesNotExist() throws Exception {
        Mockito.when(this.userController.getById(1000)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        mockMvc.perform(get("/student/byId"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getStudents() throws Exception {
        Mockito.when(this.userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/student/allStudents"))
                .andExpect(status().isOk());
    }

}
