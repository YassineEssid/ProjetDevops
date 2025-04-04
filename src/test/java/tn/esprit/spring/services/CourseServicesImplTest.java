package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CourseServicesImplTest {

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private CourseServicesImpl courseServices;

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(2);
        course.setPrice(100.0f);
        course.setTimeSlot(3);
        course.setTypeCourse(TypeCourse.INDIVIDUAL);
        course.setSupport(Support.SKI); // ✅ mise à jour ici
    }

    @Test
    void testRetrieveAllCourses() {
        // Arrange
        Course course2 = new Course();
        course2.setNumCourse(2L);
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course, course2));

        // Act
        List<Course> courses = courseServices.retrieveAllCourses();

        // Assert
        assertEquals(2, courses.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testAddCourse() {
        // Arrange
        when(courseRepository.save(course)).thenReturn(course);

        // Act
        Course saved = courseServices.addCourse(course);

        // Assert
        assertNotNull(saved);
        assertEquals(1L, saved.getNumCourse());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testUpdateCourse() {
        // Arrange
        course.setLevel(5);
        when(courseRepository.save(course)).thenReturn(course);

        // Act
        Course updated = courseServices.updateCourse(course);

        // Assert
        assertEquals(5, updated.getLevel());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testRetrieveCourse() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Act
        Course found = courseServices.retrieveCourse(1L);

        // Assert
        assertNotNull(found);
        assertEquals(1L, found.getNumCourse());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveCourseNotFound() {
        // Arrange
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Course found = courseServices.retrieveCourse(999L);

        // Assert
        assertNull(found);
        verify(courseRepository, times(1)).findById(999L);
    }
}
