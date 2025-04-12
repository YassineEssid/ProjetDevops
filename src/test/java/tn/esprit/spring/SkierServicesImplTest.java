package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;
import tn.esprit.spring.services.SkierServicesImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkierServicesImplTest {

    @InjectMocks
    private SkierServicesImpl skierServices;

    @Mock
    private ISkierRepository skierRepository;
    @Mock
    private IPisteRepository pisteRepository;
    @Mock
    private ICourseRepository courseRepository;
    @Mock
    private IRegistrationRepository registrationRepository;
    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void retrieveAllSkiers() {
        List<Skier> skiers = Arrays.asList(new Skier(), new Skier());
        when(skierRepository.findAll()).thenReturn(skiers);

        List<Skier> result = skierServices.retrieveAllSkiers();

        assertEquals(2, result.size());
        verify(skierRepository).findAll();
    }

    @Test
    void addSkier_setsCorrectEndDateAndSaves() {
        Subscription subscription = new Subscription();
        subscription.setStartDate(LocalDate.now());
        subscription.setTypeSub(TypeSubscription.ANNUAL);

        Skier skier = new Skier();
        skier.setSubscription(subscription);

        when(skierRepository.save(skier)).thenReturn(skier);

        Skier result = skierServices.addSkier(skier);

        assertNotNull(result.getSubscription().getEndDate());
        assertEquals(skier.getSubscription().getStartDate().plusYears(1), result.getSubscription().getEndDate());
        verify(skierRepository).save(skier);
    }

    @Test
    void assignSkierToSubscription() {
        Skier skier = new Skier();
        Subscription subscription = new Subscription();

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(2L)).thenReturn(Optional.of(subscription));
        when(skierRepository.save(skier)).thenReturn(skier);

        Skier result = skierServices.assignSkierToSubscription(1L, 2L);

        assertEquals(subscription, result.getSubscription());
        verify(skierRepository).save(skier);
    }

    @Test
    void addSkierAndAssignToCourse() {
        Course course = new Course();
        Skier skier = new Skier();
        Registration reg1 = new Registration();
        Registration reg2 = new Registration();
        skier.setRegistrations(new HashSet<>(Arrays.asList(reg1, reg2)));
        when(skierRepository.save(skier)).thenReturn(skier);
        when(courseRepository.getById(1L)).thenReturn(course);

        Skier result = skierServices.addSkierAndAssignToCourse(skier, 1L);

        for (Registration reg : result.getRegistrations()) {
            assertEquals(course, reg.getCourse());
            assertEquals(skier, reg.getSkier());
        }

        verify(registrationRepository, times(2)).save(any(Registration.class));
    }

    @Test
    void removeSkier() {
        skierServices.removeSkier(1L);
        verify(skierRepository).deleteById(1L);
    }

    @Test
    void retrieveSkier_found() {
        Skier skier = new Skier();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));

        Skier result = skierServices.retrieveSkier(1L);

        assertEquals(skier, result);
    }

    @Test
    void retrieveSkier_notFound() {
        when(skierRepository.findById(1L)).thenReturn(Optional.empty());
        assertNull(skierServices.retrieveSkier(1L));
    }

    @Test
    void assignSkierToPiste() {
        Skier skier = new Skier();
        skier.setPistes(null); // simulate null pistes list
        Piste piste = new Piste();

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(2L)).thenReturn(Optional.of(piste));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        Skier result = skierServices.assignSkierToPiste(1L, 2L);

        assertTrue(result.getPistes().contains(piste));
    }

    @Test
    void retrieveSkiersBySubscriptionType() {
        TypeSubscription type = TypeSubscription.MONTHLY;
        List<Skier> mockList = Arrays.asList(new Skier());
        when(skierRepository.findBySubscription_TypeSub(type)).thenReturn(mockList);

        List<Skier> result = skierServices.retrieveSkiersBySubscriptionType(type);

        assertEquals(1, result.size());
    }
}
