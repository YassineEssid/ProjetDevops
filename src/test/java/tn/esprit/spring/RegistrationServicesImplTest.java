package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.services.RegistrationServicesImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServicesImplTest {

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    private Skier skier;
    private Course course;
    private Registration registration;

    @BeforeEach
    void setUp() {
        skier = new Skier();
        skier.setNumSkier(1L);
        skier.setDateOfBirth(LocalDate.now().minusYears(20));

        course = new Course();
        course.setNumCourse(1L);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);

        registration = new Registration();
        registration.setNumWeek(1);
    }

    @Test
    void testAddRegistrationAndAssignToSkier() {
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, 1L);

        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void testAssignRegistrationToCourse() {
        registration.setCourse(null);
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.assignRegistrationToCourse(1L, 1L);

        assertNotNull(result);
        assertEquals(course, result.getCourse());
        verify(registrationRepository).save(registration);
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_SuccessfulAdult() {
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong())).thenReturn(0L);
        when(registrationRepository.countByCourseAndNumWeek(course, 1)).thenReturn(3L);
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        assertNotNull(result);
        assertEquals(course, result.getCourse());
        assertEquals(skier, result.getSkier());
        verify(registrationRepository).save(registration);
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_AlreadyRegistered() {
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(1, 1L, 1L)).thenReturn(1L);

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        assertNull(result);
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_InvalidSkierOrCourse() {
        when(skierRepository.findById(1L)).thenReturn(Optional.empty());

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        assertNull(result);
        verify(registrationRepository, never()).save(any());
    }
}
