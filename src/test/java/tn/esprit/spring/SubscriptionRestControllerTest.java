package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
        import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.controllers.SubscriptionRestController;
import tn.esprit.spring.entities.*;
        import tn.esprit.spring.services.ISubscriptionServices;

import java.time.LocalDate;
import java.util.*;

        import static org.mockito.Mockito.*;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

        import com.fasterxml.jackson.databind.ObjectMapper;

public class SubscriptionRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ISubscriptionServices subscriptionServices;

    @InjectMocks
    private SubscriptionRestController controller;

    private Subscription subscription;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setStartDate(LocalDate.of(2025, 4, 1));
        subscription.setEndDate(LocalDate.of(2025, 5, 1));
        subscription.setTypeSub(TypeSubscription.MONTHLY);
        subscription.setPrice(100f);
    }

    @Test
    void testAddSubscription() throws Exception {
        when(subscriptionServices.addSubscription(any())).thenReturn(subscription);

        mockMvc.perform(post("/subscription/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(subscription)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(1L));
    }

    @Test
    void testGetSubscriptionById() throws Exception {
        when(subscriptionServices.retrieveSubscriptionById(1L)).thenReturn(subscription);

        mockMvc.perform(get("/subscription/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(1L));
    }

    @Test
    void testGetByType() throws Exception {
        Set<Subscription> subscriptions = new HashSet<>();
        subscriptions.add(subscription);

        when(subscriptionServices.getSubscriptionByType(TypeSubscription.MONTHLY)).thenReturn(subscriptions);

        mockMvc.perform(get("/subscription/all/MONTHLY"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateSubscription() throws Exception {
        when(subscriptionServices.updateSubscription(any())).thenReturn(subscription);

        mockMvc.perform(put("/subscription/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(subscription)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(1L));
    }

    @Test
    void testGetByDates() throws Exception {
        List<Subscription> subs = List.of(subscription);
        LocalDate d1 = LocalDate.of(2025, 4, 1);
        LocalDate d2 = LocalDate.of(2025, 5, 1);

        when(subscriptionServices.retrieveSubscriptionsByDates(d1, d2)).thenReturn(subs);

        mockMvc.perform(get("/subscription/all/2025-04-01/2025-05-01"))
                .andExpect(status().isOk());
    }
}
