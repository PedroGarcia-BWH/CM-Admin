package es.uca.cm.admin.views.reporte.reporteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    public Reporte save(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    public List<Reporte> findByEliminationDateIsNull() {
        return reporteRepository.findByDateEliminatedIsNull();
    }

    public List<Reporte> findByEliminationDateIsNotNull() {
        return reporteRepository.findByDateEliminatedIsNotNull();
    }

    public boolean reporteExists(String reportado_uuid, String reportador_uuid, String mensaje_uuid) {
        return reporteRepository.findByReportadoUuidAndReportadorUuidAndMensajeUuid(reportado_uuid, reportador_uuid, mensaje_uuid).isPresent();
    }

     public void closeReporte(String reporte_id){
       Optional<Reporte>  reporte = reporteRepository.findById(UUID.fromString(reporte_id));
       if (reporte.isPresent()) {
           reporte.get().setDateEliminated(new Date());
           reporteRepository.save(reporte.get());
       }
     }

}
