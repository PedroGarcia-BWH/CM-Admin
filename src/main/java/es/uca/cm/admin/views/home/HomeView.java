package es.uca.cm.admin.views.home;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
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
        vlDashboard.add(dashBoard, hDay);
        add(vlDashboard);

    }
}
