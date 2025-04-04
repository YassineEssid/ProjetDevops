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

        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
    }

    @Test
    void testAddInstructor() {
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor savedInstructor = instructorServices.addInstructor(instructor);

        assertNotNull(savedInstructor);
        assertEquals("John", savedInstructor.getFirstName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveAllInstructors() {
        List<Instructor> instructors = Arrays.asList(instructor);
        when(instructorRepository.findAll()).thenReturn(instructors);

        List<Instructor> result = instructorServices.retrieveAllInstructors();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void testUpdateInstructor() {
        Instructor updatedInfo = new Instructor();
        updatedInfo.setNumInstructor(1L);
        updatedInfo.setFirstName("UpdatedName");

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(updatedInfo);

        Instructor updatedInstructor = instructorServices.updateInstructor(updatedInfo);

        assertNotNull(updatedInstructor);
        assertEquals("UpdatedName", updatedInstructor.getFirstName());
        verify(instructorRepository, times(1)).save(updatedInfo);
    }

    @Test
    void testRetrieveInstructorFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        Instructor foundInstructor = instructorServices.retrieveInstructor(1L);

        assertNotNull(foundInstructor);
        assertEquals(1L, foundInstructor.getNumInstructor());
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveInstructorNotFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        Instructor foundInstructor = instructorServices.retrieveInstructor(1L);

        assertNull(foundInstructor);
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void testAddInstructorAndAssignToCourseSuccess() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor result = instructorServices.addInstructorAndAssignToCourse(instructor, 1L);

        assertNotNull(result);
        assertNotNull(result.getCourses());
        assertEquals(1, result.getCourses().size());
        verify(courseRepository, times(1)).findById(1L);
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testAddInstructorAndAssignToCourseWhenCourseNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            instructorServices.addInstructorAndAssignToCourse(instructor, 1L);
        });

        assertTrue(exception.getMessage().contains("Course not found"));
        verify(courseRepository, times(1)).findById(1L);
        verify(instructorRepository, never()).save(any());
    }
}