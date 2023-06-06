package es.uca.cm.admin.views.home;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import es.uca.cm.admin.security.AuthenticatedUser;
import es.uca.cm.admin.views.MainLayout;
import es.uca.cm.admin.webUser.WebUser;
import es.uca.cm.admin.webUser.WebUserService;
import jakarta.annotation.security.PermitAll;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@PermitAll
@PageTitle("Home")
@Route(value = "Home", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {
    private VerticalLayout vlDashboard = new VerticalLayout();

    private H2 hDay = new H2();

    private H1 dashBoard = new H1(getTranslation("dashboard.title"));

    private Date date = new Date();

    private Calendar calendar = GregorianCalendar.getInstance();


    private AuthenticatedUser user;


    public HomeView(AuthenticatedUser user) {
        this.user = user;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if(hour >=8 && hour < 14) hDay.setText(getTranslation("dashboard.morning") + user.get().get().getEmail());

        if(hour >= 14 && hour < 22) hDay.setText(getTranslation("dashboard.afternoon") + user.get().get().getEmail());

        if(hour >= 22 || hour < 8) hDay.setText(getTranslation("dashboard.evening") + user.get().get().getEmail() + "?");
        H3 normas =  new H3("Normas de la Comunidad");
        Text text = new Text ("Bienvenido/a a nuestra red social. Para garantizar un entorno seguro y respetuoso para todos los usuarios, te pedimos que sigas estas normas de la comunidad:");
        Text text1 = new Text("Respeto mutuo: Trata a todos los usuarios con cortesía y respeto. No toleramos ningún tipo de lenguaje ofensivo, discriminatorio o irrespetuoso hacia otros miembros de la comunidad.");
        Text text2 = new Text("Privacidad y seguridad: Respeta la privacidad de los demás. No publiques información personal de otros usuarios sin su consentimiento. Además, no compartas información personal sensible, como contraseñas o datos bancarios, en ningún contexto.");
        Text text3 = new Text("No al acoso: No toleramos el acoso, el bullying o el comportamiento intimidante hacia otros usuarios. No realices comentarios ofensivos, difamatorios o que puedan dañar la reputación de alguien.");
        Text text4 = new Text("No al spam: No publiques contenido promocional o comercial, ni realices publicaciones repetitivas o irrelevantes. No utilices la plataforma para enviar mensajes no solicitados o no deseados a otros usuarios.");
        Text text5 = new Text("Respeto a la diversidad: Valora la diversidad de opiniones, culturas y creencias. No promuevas ni compartas contenido que incite al odio, la discriminación o la violencia contra individuos o grupos por motivos de raza, religión, género, orientación sexual u otros.");
        Text text6 = new Text( "Reporta y denuncia: Si encuentras contenido inapropiado o comportamiento que viole estas normas, repórtalo a los administradores de la plataforma. Ayúdanos a mantener la comunidad segura y agradable para todos.");
        Text text7 = new Text( "Recuerda que el incumplimiento de estas normas puede resultar en la eliminación de contenido, la suspensión temporal o permanente de la cuenta, o cualquier otra acción que consideremos necesaria para preservar la integridad de la comunidad.");
        Text text8 = new Text("Gracias por ser parte de nuestra red social y contribuir a un entorno positivo y respetuoso.");
        vlDashboard.add(dashBoard, hDay, normas, text, text1, text2, text3, text4, text5, text6, text7, text8);

        add(vlDashboard);

    }
}
