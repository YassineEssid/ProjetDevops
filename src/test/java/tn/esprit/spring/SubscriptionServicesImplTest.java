package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
        import tn.esprit.spring.entities.*;
        import tn.esprit.spring.repositories.*;
import tn.esprit.spring.services.SubscriptionServicesImpl;

import java.time.LocalDate;
import java.util.*;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

public class SubscriptionServicesImplTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISkierRepository skierRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionService;

    private Subscription subscription;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.MONTHLY);
        subscription.setStartDate(LocalDate.of(2025, 4, 1));
        subscription.setPrice(120f);
    }

    @Test
    void testAddSubscription_Monthly() {
        Subscription expected = new Subscription();
        expected.setStartDate(subscription.getStartDate());
        expected.setEndDate(subscription.getStartDate().plusMonths(1));
        expected.setTypeSub(TypeSubscription.MONTHLY);
        expected.setPrice(120f);

        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(expected);

        Subscription result = subscriptionService.addSubscription(subscription);

        assertEquals(expected.getEndDate(), result.getEndDate());
        verify(subscriptionRepository).save(subscription);
    }

    @Test
    void testUpdateSubscription() {
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        Subscription result = subscriptionService.updateSubscription(subscription);

        assertEquals(subscription, result);
        verify(subscriptionRepository).save(subscription);
    }

    @Test
    void testRetrieveSubscriptionById() {
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        Subscription result = subscriptionService.retrieveSubscriptionById(1L);

        assertEquals(subscription, result);
        verify(subscriptionRepository).findById(1L);
    }

    @Test
    void testGetSubscriptionByType() {
        Set<Subscription> subscriptions = new HashSet<>();
        subscriptions.add(subscription);

        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(TypeSubscription.MONTHLY))
                .thenReturn(subscriptions);

        Set<Subscription> result = subscriptionService.getSubscriptionByType(TypeSubscription.MONTHLY);

        assertEquals(1, result.size());
    }

    @Test
    void testRetrieveSubscriptionsByDates() {
        List<Subscription> list = List.of(subscription);
        LocalDate d1 = LocalDate.of(2025, 4, 1);
        LocalDate d2 = LocalDate.of(2025, 5, 1);

        when(subscriptionRepository.getSubscriptionsByStartDateBetween(d1, d2)).thenReturn(list);

        List<Subscription> result = subscriptionService.retrieveSubscriptionsByDates(d1, d2);

        assertEquals(1, result.size());
    }
}
