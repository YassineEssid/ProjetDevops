package tn.esprit.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.controllers.SkierRestController;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.services.ISkierServices;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SkierRestController.class)
class SkierRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISkierServices skierServices;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddSkier() throws Exception {
        Skier skier = new Skier();
        Mockito.when(skierServices.addSkier(any(Skier.class))).thenReturn(skier);

        mockMvc.perform(post("/skier/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skier)))
                .andExpect(status().isOk());
    }

    @Test
    void testAddSkierAndAssignToCourse() throws Exception {
        Skier skier = new Skier();
        Mockito.when(skierServices.addSkierAndAssignToCourse(any(Skier.class), eq(1L))).thenReturn(skier);

        mockMvc.perform(post("/skier/addAndAssign/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skier)))
                .andExpect(status().isOk());
    }

    @Test
    void testAssignToSubscription() throws Exception {
        Skier skier = new Skier();
        Mockito.when(skierServices.assignSkierToSubscription(1L, 2L)).thenReturn(skier);

        mockMvc.perform(put("/skier/assignToSub/1/2"))
                .andExpect(status().isOk());
    }

    @Test
    void testAssignToPiste() throws Exception {
        Skier skier = new Skier();
        Mockito.when(skierServices.assignSkierToPiste(1L, 2L)).thenReturn(skier);

        mockMvc.perform(put("/skier/assignToPiste/1/2"))
                .andExpect(status().isOk());
    }

    @Test
    void testRetrieveSkiersBySubscriptionType() throws Exception {
        List<Skier> skierList = Arrays.asList(new Skier(), new Skier());
        Mockito.when(skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.MONTHLY)).thenReturn(skierList);

        mockMvc.perform(get("/skier/getSkiersBySubscription")
                        .param("typeSubscription", "MONTHLY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetById() throws Exception {
        Skier skier = new Skier();
        Mockito.when(skierServices.retrieveSkier(1L)).thenReturn(skier);

        mockMvc.perform(get("/skier/get/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete("/skier/delete/1"))
                .andExpect(status().isOk());

        Mockito.verify(skierServices).removeSkier(1L);
    }

    @Test
    void testGetAllSkiers() throws Exception {
        List<Skier> skierList = Arrays.asList(new Skier(), new Skier());
        Mockito.when(skierServices.retrieveAllSkiers()).thenReturn(skierList);

        mockMvc.perform(get("/skier/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
