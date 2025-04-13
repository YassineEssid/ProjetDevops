package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PisteServicesImplTest {

    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteServices;

    private Piste piste;

    @BeforeEach
    void setUp() {
        piste = new Piste();
        piste.setNumPiste(1L);
        piste.setNamePiste("Piste Verte");
        piste.setColor(Color.GREEN);
        piste.setLength(1500);
        piste.setSlope(30);
    }

    @Test
    void testRetrieveAllPistes() {
        // Given
        Piste piste2 = new Piste();
        piste2.setNumPiste(2L);
        when(pisteRepository.findAll()).thenReturn(Arrays.asList(piste, piste2));

        // When
        List<Piste> result = pisteServices.retrieveAllPistes();

        // Then
        assertEquals(2, result.size());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testAddPiste_Success() {
        // Given
        when(pisteRepository.save(any(Piste.class))).thenReturn(piste);

        // When
        Piste result = pisteServices.addPiste(piste);

        // Then
        assertNotNull(result.getNumPiste());
        verify(pisteRepository, times(1)).save(any(Piste.class));
    }

    @Test
    void testRetrievePiste_Found() {
        // Given
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));

        // When
        Piste result = pisteServices.retrievePiste(1L);

        // Then
        assertEquals("Piste Verte", result.getNamePiste());
        verify(pisteRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrievePiste_NotFound() {
        // Given
        when(pisteRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Piste result = pisteServices.retrievePiste(99L);

        // Then
        assertNull(result);
    }

    @Test
    void testRemovePiste() {
        // Given
        doNothing().when(pisteRepository).deleteById(1L);

        // When
        pisteServices.removePiste(1L);

        // Then
        verify(pisteRepository, times(1)).deleteById(1L);
    }
}