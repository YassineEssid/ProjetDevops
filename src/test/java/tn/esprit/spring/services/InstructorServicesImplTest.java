package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorServicesImplTest {

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    private Instructor instructor;
    private Course course;

    @BeforeEach
    void setUp() {
        instructor = new Instructor();
        instructor.setNumInstructor(1L);
        instructor.setFirstName("John");
        instructor.setLastName("Doe");
        instructor.setDateOfHire(LocalDate.now());
        instructor.setCourses(new HashSet<>());

        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
    }

    @Test
    void testAddInstructor() {
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor savedInstructor = instructorServices.addInstructor(instructor);

        assertNotNull(savedInstructor, "Saved instructor should not be null");
        assertEquals("John", savedInstructor.getFirstName(), "First name should match");
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveAllInstructors() {
        List<Instructor> instructors = Collections.singletonList(instructor);
        when(instructorRepository.findAll()).thenReturn(instructors);

        List<Instructor> result = instructorServices.retrieveAllInstructors();

        assertEquals(1, result.size(), "Should return one instructor");
        assertEquals("John", result.get(0).getFirstName(), "First name should match");
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void testUpdateInstructor() {
        Instructor updatedInfo = new Instructor();
        updatedInfo.setNumInstructor(1L);
        updatedInfo.setFirstName("UpdatedName");
        updatedInfo.setLastName("Doe");
        updatedInfo.setDateOfHire(LocalDate.now());

        when(instructorRepository.save(any(Instructor.class))).thenReturn(updatedInfo);

        Instructor updatedInstructor = instructorServices.updateInstructor(updatedInfo);

        assertNotNull(updatedInstructor, "Updated instructor should not be null");
        assertEquals("UpdatedName", updatedInstructor.getFirstName(), "First name should be updated");
        verify(instructorRepository, times(1)).save(updatedInfo);
    }

    @Test
    void testRetrieveInstructorFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        Instructor foundInstructor = instructorServices.retrieveInstructor(1L);

        assertNotNull(foundInstructor, "Instructor should be found");
        assertEquals(1L, foundInstructor.getNumInstructor(), "ID should match");
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveInstructorNotFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        Instructor foundInstructor = instructorServices.retrieveInstructor(1L);

        assertNull(foundInstructor, "Should return null when not found");
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void testAddInstructorAndAssignToCourseSuccess() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor result = instructorServices.addInstructorAndAssignToCourse(instructor, 1L);

        assertNotNull(result, "Result should not be null");
        assertNotNull(result.getCourses(), "Courses set should not be null");
        assertEquals(1, result.getCourses().size(), "Should have one course assigned");
        assertTrue(result.getCourses().contains(course), "Course should be assigned");
        verify(courseRepository, times(1)).findById(1L);
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testAddInstructorAndAssignToCourseWhenCourseNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            instructorServices.addInstructorAndAssignToCourse(instructor, 1L);
        }, "Should throw exception when course not found");

        assertEquals("Course not found", exception.getMessage(), "Exception message should match");
        verify(courseRepository, times(1)).findById(1L);
        verify(instructorRepository, never()).save(any());
    }
}