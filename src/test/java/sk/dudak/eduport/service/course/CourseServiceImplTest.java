package sk.dudak.eduport.service.course;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import sk.dudak.eduport.dao.course.CourseRepository;
import sk.dudak.eduport.model.course.Course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

public class CourseServiceImplTest {

    private CourseService courseService;
    private Course mat2;
    private Course api;
    private Course pt;

    @Before
    public void setup() {
        CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
        this.courseService = new CourseServiceImpl(courseRepository);

        mat2 = Course.builder()
                            .id(1)
                            .title("Mathematics 2")
                            .abbreviation("MAT2")
                            .users(Collections.emptyList())
                            .build();
        api = Course.builder()
                           .id(1)
                           .title("Computer architecture")
                           .abbreviation("API")
                           .users(Collections.emptyList())
                           .build();

        pt = Course.builder()
                   .id(1)
                   .title("Programming techniques")
                   .abbreviation("PT")
                   .users(Collections.emptyList())
                   .build();

        List<Course> courses = new ArrayList<>();
        courses.add(mat2);
        courses.add(api);

        Mockito.when(courseRepository.findAll()).thenReturn(courses);
        Mockito.when(courseRepository.save(mat2)).thenReturn(mat2);
        Mockito.when(courseRepository.save(api)).thenReturn(api);

    }

    @Test
    public void courseTitleExists() {
        Assert.assertTrue(this.courseService.courseTitleExists(api));
    }

    @Test
    public void getAll() {
        Assert.assertThat(this.courseService.getAll(), hasSize(2));
    }

    @Test
    public void addCourse() throws Exception {
        Assert.assertTrue(this.courseService.addCourse(pt));
    }

    @Test(expected = NullPointerException.class)
    public void addNullCourse() throws Exception {
        this.courseService.addCourse(null);
    }

    @Test
    public void addExistingCourse() throws Exception {
        Assert.assertFalse(this.courseService.addCourse(mat2));
    }

    @Test
    public void getCourseByName() throws Exception {
        Assert.assertTrue(this.courseService.getCourseByName("MAT2").isPresent());
    }

    @Test
    public void getNotExistingCourseByName() throws Exception {
        Assert.assertFalse(this.courseService.getCourseByName("ano").isPresent());
    }

    @Test
    public void getCourseByNullName() throws Exception {
        Assert.assertFalse(this.courseService.getCourseByName(null).isPresent());
    }
}
