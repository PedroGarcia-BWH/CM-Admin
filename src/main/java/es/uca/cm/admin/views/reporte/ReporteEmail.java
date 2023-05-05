package es.uca.cm.admin.views.reporte;

import es.uca.cm.admin.components.emailService.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReporteEmail {
    @Autowired
    private EmailService emailService;

    ReporteEmail() {}

    public void sendAvisoReportado(String to, String motivo, String resolucion) {
        String cuerpo = "Usted ha sido reportado por un usuario de Conexión Morada Social por el siguiente motivo: " + motivo + ".\n" +
                "La resolución de este reporte ha sido: " + resolucion + ".\n" +
                "AVISO IMPORTANTE: Si usted recibe 3 reportes, su cuenta será baneada de forma permanente.\n" +
                "Si usted considera que este reporte es injusto, puede ponerse en contacto con el administrador de Conexión Morada Social a través nuestro correo electrónico.";
        emailService.sendEmail(to, "Aviso de infración en Conexión Morada Social", cuerpo);
    }

    public void sendBaneoReportado(String to, String motivo, String resolucion) {
        String cuerpo = "Usted ha sido baneado de Conexión Morada Social por el siguiente motivo: " + motivo + ".\n" +
                "La resolución de este reporte ha sido: " + resolucion + ".\n" +
                "AVISO IMPORTANTE: Esta acción es irreversible y no apelable.\n";
        emailService.sendEmail(to, "Aviso de suspensión de su cuenta en Conexión Morada", cuerpo);
    }

    public void sendAvisoReportador(String to, String motivo, String resolucion) {
        String cuerpo = "Usted ha reportado a un usuario de Conexión Morada Social por el siguiente motivo: " + motivo + ".\n" +
                "La resolución de este reporte ha sido: " + resolucion + ".\n" +
                "Muchas gracias por su colaboración en contribuir en crear una comunidad más sana y respetuosa con los usuarios.\n" +
                "El equipo de Conexión Morada Social.";
        emailService.sendEmail(to, "Resultado de su reporte en Conexión Morada Social", cuerpo);
    }

}
