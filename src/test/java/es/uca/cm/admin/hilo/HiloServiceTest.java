package es.uca.cm.admin.hilo;

import es.uca.cm.admin.username.Username;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class HiloServiceTest {

    @Mock
    private HiloRepository hiloRepository;

    @InjectMocks
    private HiloService hiloService;

    public HiloServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave() {
        Hilo hilo = new Hilo();
        when(hiloRepository.save(hilo)).thenReturn(hilo);

        Hilo savedHilo = hiloService.save(hilo);

        assertEquals(hilo, savedHilo);
        verify(hiloRepository, times(1)).save(hilo);
    }

    @Test
    public void testDelete() {
        Hilo hilo = new Hilo();
        Date currentDate = new Date();
        hiloService.delete(hilo);

        assertEquals(currentDate, hilo.getDateElimination());
        verify(hiloRepository, times(1)).save(hilo);
    }

    @Test
    public void testFindByAutorAndHiloPadreIsNullOrderByDateCreation() {
        Username username = new Username();
        List<Hilo> hiloList = new ArrayList<>();
        when(hiloRepository.findByAutorAndHiloPadreIsNullOrderByDateCreation(username)).thenReturn(hiloList);

        List<Hilo> result = hiloService.findByAutorAndHiloPadreIsNullOrderByDateCreation(username);

        assertEquals(hiloList, result);
        verify(hiloRepository, times(1)).findByAutorAndHiloPadreIsNullOrderByDateCreation(username);
    }

    @Test
    public void testFindByAutorOrderByDateCreation() {
        Username userApp = new Username();
        List<Hilo> hiloList = new ArrayList<>();
        when(hiloRepository.findByAutorOrderByDateCreation(userApp)).thenReturn(hiloList);

        List<Hilo> result = hiloService.findByAutorOrderByDateCreation(userApp);

        assertEquals(hiloList, result);
        verify(hiloRepository, times(1)).findByAutorOrderByDateCreation(userApp);
    }

    @Test
    public void testFindByHiloPadreIsNullOrderByDateCreation() {
        List<Hilo> hiloList = new ArrayList<>();
        when(hiloRepository.findByHiloPadreIsNullOrderByDateCreation()).thenReturn(hiloList);

        List<Hilo> result = hiloService.findByHiloPadreIsNullOrderByDateCreation();

        assertEquals(hiloList, result);
        verify(hiloRepository, times(1)).findByHiloPadreIsNullOrderByDateCreation();
    }

    @Test
    public void testFindByMensajeContainingIgnoreCase() {
        String mensaje = "hello";
        List<Hilo> hiloList = new ArrayList<>();
        when(hiloRepository.findByMensajeContainingIgnoreCase(mensaje)).thenReturn(hiloList);

        List<Hilo> result = hiloService.findByMensajeContainingIgnoreCase(mensaje);

        assertEquals(hiloList, result);
        verify(hiloRepository, times(1)).findByMensajeContainingIgnoreCase(mensaje);
    }

    @Test
    public void testFindById() {
        UUID id = UUID.randomUUID();
        Optional<Hilo> hiloOptional = Optional.of(new Hilo());
        when(hiloRepository.findById(id)).thenReturn(hiloOptional);

        Optional<Hilo> result = hiloService.findById(id);

        assertEquals(hiloOptional, result);
        verify(hiloRepository, times(1)).findById(id);
    }

    @Test
    public void testFindByHiloPadreOrderByDateCreation() {
        Hilo hiloPadre = new Hilo();
        List<Hilo> hiloList = new ArrayList<>();
        when(hiloRepository.findByHiloPadreOrderByDateCreation(hiloPadre)).thenReturn(hiloList);

        List<Hilo> result = hiloService.findByHiloPadreOrderByDateCreation(hiloPadre);

        assertEquals(hiloList, result);
        verify(hiloRepository, times(1)).findByHiloPadreOrderByDateCreation(hiloPadre);
    }
}