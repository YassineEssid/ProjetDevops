package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.repositories.*;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SkierTest {

    @Autowired
    private ISkierRepository skierRepository;

    @Autowired
    private ISubscriptionRepository subscriptionRepository;

    @Test
    public void testSaveSkierWithSubscription() {
        // Create and save subscription
        Subscription sub = new Subscription();
        sub.setTypeSub(TypeSubscription.MONTHLY);
        sub.setStartDate(LocalDate.of(2025, 4, 1));
        sub = subscriptionRepository.save(sub); // reassign to managed entity

        // Create and save skier
        Skier skier = new Skier();
        skier.setFirstName("Alice");
        skier.setLastName("Snow");
        skier.setCity("Chamonix");
        skier.setDateOfBirth(LocalDate.of(2000, 1, 1));
        skier.setSubscription(sub);
        skier.setPistes(new HashSet<>()); // empty set

        skier = skierRepository.save(skier); // persist

        // Assertions
        assertNotNull(skier.getNumSkier(), "Skier ID should not be null");
        assertEquals("Alice", skier.getFirstName());
        assertEquals(TypeSubscription.MONTHLY, skier.getSubscription().getTypeSub());
    }
}
