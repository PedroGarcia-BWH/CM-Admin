package es.uca.cm.admin.views.reporte.reporteService;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.UUID;

@Entity
public class Reporte {
    @Id
    @GeneratedValue
    @Column(length=16)
    private UUID id;

    @Column
    private String reportadoUuid;

    @Column
    private String reportadorUuid;

    @Column
    private String motivo;

    @Column
    private String descripcion;

    @Column
    private String mensajeUuid;

    @Column
    private Date dateCreated;

    @Column
    private Date dateEliminated;

    public Reporte(){}

    public Reporte (String reportadoUuid, String reportadorUuid, String motivo, String descripcion, String mensajeUuid) {
        this.reportadoUuid = reportadoUuid;
        this.reportadorUuid = reportadorUuid;
        this.motivo = motivo;
        this.descripcion = descripcion;
        this.mensajeUuid = mensajeUuid;
        this.dateCreated = new Date();
    }

    //getters
    public UUID getId() {
        return id;
    }

    public String getReportado_uuid() {
        return reportadoUuid;
    }

    public String getReportador_uuid() {
        return reportadorUuid;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateEliminated() {
        return dateEliminated;
    }

    public String getMensajeUuid() {
        return mensajeUuid;
    }

    //setters
    public void setId(UUID id) {
        this.id = id;
    }

    public void setReportado_uuid(String reportado_uuid) {
        this.reportadoUuid = reportado_uuid;
    }

    public void setReportador_uuid(String reportador_uuid) {
        this.reportadorUuid = reportador_uuid;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateEliminated(Date dateEliminated) {
        this.dateEliminated = dateEliminated;
    }

    public void setMensajeUuid(String mensajeUuid) {
        this.mensajeUuid = mensajeUuid;
    }
}
