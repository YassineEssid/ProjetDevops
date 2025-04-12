package tn.esprit.spring;


import org.junit.jupiter.api.Test;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class SubscriptionTest {

    @Test
    void testSubscriptionConstructorAndFields() {
        Subscription sub = new Subscription(
                1L,
                LocalDate.of(2025, 4, 1),
                LocalDate.of(2025, 5, 1),
                100f,
                TypeSubscription.MONTHLY
        );

        assertEquals(1L, sub.getNumSub());
        assertEquals(LocalDate.of(2025, 4, 1), sub.getStartDate());
        assertEquals(TypeSubscription.MONTHLY, sub.getTypeSub());
        assertEquals(100f, sub.getPrice());
    }
}
