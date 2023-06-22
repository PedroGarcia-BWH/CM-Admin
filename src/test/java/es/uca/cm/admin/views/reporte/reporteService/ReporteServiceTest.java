package es.uca.cm.admin.views.reporte.reporteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.helger.commons.mock.CommonsAssert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @InjectMocks
    private ReporteService reporteService;

    @BeforeEach
    public void ReporteServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        Reporte reporte = new Reporte();
        when(reporteRepository.save(reporte)).thenReturn(reporte);

        Reporte savedReporte = reporteService.save(reporte);

        assertNotNull(savedReporte);
        assertEquals(reporte, savedReporte);
        verify(reporteRepository, times(1)).save(reporte);
    }

    @Test
    public void testFindByEliminationDateIsNull() {
        List<Reporte> reporteList = new ArrayList<>();
        when(reporteRepository.findByDateEliminatedIsNull()).thenReturn(reporteList);

        List<Reporte> foundReporteList = reporteService.findByEliminationDateIsNull();

        assertNotNull(foundReporteList);
        assertEquals(reporteList, foundReporteList);
        verify(reporteRepository, times(1)).findByDateEliminatedIsNull();
    }

    @Test
    public void testFindByEliminationDateIsNotNull() {
        List<Reporte> reporteList = new ArrayList<>();
        when(reporteRepository.findByDateEliminatedIsNotNull()).thenReturn(reporteList);

        List<Reporte> foundReporteList = reporteService.findByEliminationDateIsNotNull();

        assertNotNull(foundReporteList);
        assertEquals(reporteList, foundReporteList);
        verify(reporteRepository, times(1)).findByDateEliminatedIsNotNull();
    }

    @Test
    public void testReporteExists() {
        String reportadoUuid = "reportado_uuid";
        String reportadorUuid = "reportador_uuid";
        String mensajeUuid = "mensaje_uuid";
        when(reporteRepository.findByReportadoUuidAndReportadorUuidAndMensajeUuid(reportadoUuid, reportadorUuid, mensajeUuid))
                .thenReturn(Optional.of(new Reporte()));

        boolean exists = reporteService.reporteExists(reportadoUuid, reportadorUuid, mensajeUuid);

        assertTrue(exists);
        verify(reporteRepository, times(1)).findByReportadoUuidAndReportadorUuidAndMensajeUuid(reportadoUuid, reportadorUuid, mensajeUuid);
    }

    @Test
    public void testCloseReporte() {
        String reporteId = UUID.randomUUID().toString();
        Reporte reporte = new Reporte();
        reporte.setId(UUID.fromString(reporteId));
        Optional<Reporte> optionalReporte = Optional.of(reporte);
        when(reporteRepository.findById(UUID.fromString(reporteId))).thenReturn(optionalReporte);
        when(reporteRepository.save(reporte)).thenReturn(reporte);

        reporteService.closeReporte(reporteId);

        assertNotNull(reporte.getDateEliminated());
        verify(reporteRepository, times(1)).findById(UUID.fromString(reporteId));
        verify(reporteRepository, times(1)).save(reporte);
    }
}