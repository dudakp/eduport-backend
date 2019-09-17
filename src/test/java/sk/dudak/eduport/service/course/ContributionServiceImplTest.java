package sk.dudak.eduport.service.course;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sk.dudak.eduport.dao.course.ContributionRepository;
import sk.dudak.eduport.dao.course.CourseRepository;
import sk.dudak.eduport.dao.course.UserRepository;
import sk.dudak.eduport.model.course.Contribution;
import sk.dudak.eduport.model.course.Course;
import sk.dudak.eduport.model.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;

public class ContributionServiceImplTest {

    private ContributionService contributionService;
    private Contribution contrib3;
    private Course mat2;

    @Before
    public void setUp() throws Exception {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ContributionRepository contributionRepository = Mockito.mock(ContributionRepository.class);
        CourseService courseService = Mockito.mock(CourseService.class);
        CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
        Course course = Mockito.mock(Course.class);

        this.contributionService = new ContributionServiceImpl(userRepository, contributionRepository,
                courseService, courseRepository);
        Contribution contrib1 = Contribution.builder()
                                            .id(0)
                                            .title("title")
                                            .content("content")
                                            .course(Course.builder().id(0).title("Mathematics 2").abbreviation("MAT2")
                                                          .users(Collections.emptyList())
                                                          .build())
                                            .build();
        mat2 = Course.builder()
                     .id(1)
                     .title("Mathematics 2")
                     .abbreviation("MAT2")
                     .contributions(new ArrayList<Contribution>(){{add(contrib1);}})
                     .users(Collections.emptyList())
                     .build();

        Contribution contrib2 = Contribution.builder()
                                            .id(1)
                                            .title("nazov")
                                            .content("obsah")
                                            .course(Course.builder().id(1)
                                                          .title("Objektovo orientovane-programovanie")
                                                          .users(new ArrayList<>())
                                                          .abbreviation("OOP")
                                                          .build())
                                            .build();
        Course oop = Course.builder()
                           .id(1)
                           .title("Objektovo orientovane-programovanie")
                           .abbreviation("OOP")
                           .contributions(Collections.singletonList(contrib2))
                           .users(new ArrayList<>())
                           .build();
        User student = User.builder()
                           .id(4)
                           .username("student")
                           .password("student")
                           .roles(new ArrayList<String>() {{
                               add("ROLE_STUDENT");
                           }})
                           .coursesEnrolled(new ArrayList<Course>() {{
                               add(oop);
                           }})
                           .build();
        oop.getUsers().add(student);
        contrib2.getCourse().getUsers().add(student);

        contrib3 = Contribution.builder()
                               .id(3)
                               .title("novy nazov")
                               .content("novy obsah")
                               .course(Course.builder().id(1).title("Objektovo orientovane-programovanie")
                                             .abbreviation("OOP").build())
                               .build();

        List<Contribution> contributions = new ArrayList<Contribution>() {{
            add(contrib1);
            add(contrib2);
        }};

        List<Course> courses = new ArrayList<>();
        courses.add(mat2);
        courses.add(oop);

        Mockito.when(contributionRepository.findAll()).thenReturn(contributions);
        Mockito.when(contributionRepository.findById((long) 0)).thenReturn(Optional.ofNullable(contrib1));
        Mockito.when(contributionRepository.findById((long) 1)).thenReturn(Optional.ofNullable(contrib2));
        Mockito.when(contributionRepository.save(contrib3)).thenReturn(contrib3);

        Mockito.when(courseRepository.findAll()).thenReturn(courses);
        Mockito.when(courseRepository.findById((long) 1)).thenReturn(Optional.ofNullable(mat2));

        Mockito.when(userRepository.findByUsername("student")).thenReturn(Optional.ofNullable(student));

        Mockito.when(courseService.getCourseByName("MAT2")).thenReturn(Optional.ofNullable(mat2));

        Mockito.when(course.addContrib(contrib3)).thenReturn(true);

    }

    @Test
    public void getAll() {
        Assert.assertThat(contributionService.getAll(), hasSize(2));
    }

    @Test
    public void getAllById() {
        Assert.assertThat(contributionService.getAllById((long) 1), hasSize(1));
    }

    @Test
    public void getAllForUser() {
        Assert.assertThat(contributionService.getAllForUser("student"), hasSize(1));
    }

    @Test
    public void getAllForNullUser() {
        Assert.assertThat(contributionService.getAllForUser(null), hasSize(0));
    }

    @Test
    public void getAllForNonExistingUser() {
        Assert.assertThat(contributionService.getAllForUser("ano"), hasSize(0));
    }

    @Test
    public void getAllForCourse() {
        Assert.assertThat(contributionService.getAllForCourse("MAT2"), hasSize(1));
    }

    @Test
    public void getAllForNullCourse() {
        Assert.assertThat(contributionService.getAllForCourse(null), hasSize(0));
    }

    @Test
    public void getAllForNotExistingCourse() {
        Assert.assertThat(contributionService.getAllForCourse("predmet"), hasSize(0));
    }

    @Test
    public void deleteContribution() throws Exception {
        Assert.assertTrue(contributionService.deleteContribution("nazov"));
    }

    @Test
    public void deleteNullContribution() throws Exception {
        Assert.assertFalse(contributionService.deleteContribution(null));
    }

    @Test
    public void deleteNonExistingContribution() throws Exception {
        Assert.assertFalse(contributionService.deleteContribution("aaaaaaa"));
    }

    @Test
    public void addContribution() throws Exception {
        Assert.assertTrue(contributionService.addContribution(contrib3, mat2.getAbbreviation()));
    }

    @Test(expected = NullPointerException.class)
    public void addNullContribution() throws Exception {
        contributionService.addContribution(null, "MAT2");
    }

    @Test
    public void addContributionToNonExistingCourse() throws Exception {
        Assert.assertFalse(contributionService.addContribution(contrib3, "AAAAAAAAA"));
    }

    @Test
    public void addContributionToNullCourse() throws Exception {
        Assert.assertFalse(contributionService.addContribution(contrib3, null));
    }
}
