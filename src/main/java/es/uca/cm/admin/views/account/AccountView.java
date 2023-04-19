package es.uca.cm.admin.views.account;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.cm.admin.security.AuthenticatedUser;
import es.uca.cm.admin.views.MainLayout;
import es.uca.cm.admin.webUser.WebUser;
import es.uca.cm.admin.webUser.WebUserService;
import jakarta.annotation.security.PermitAll;


@PermitAll
@PageTitle("Perfil")
@Route(value = "account", layout = MainLayout.class)
public class AccountView extends VerticalLayout {

    private H1 hPerfil = new H1(getTranslation("user.home"));

    private H2 hPass = new H2(getTranslation("user.changePassword"));

    private Paragraph pPass= new Paragraph(getTranslation("user.newPassword"));

    private Paragraph pPassRepeat = new Paragraph(getTranslation("user.confirmPassword"));

    private TextField tfPass = new TextField();
    private TextField tfPassRepeat = new TextField();

    private HorizontalLayout hlPass = new HorizontalLayout();

    private HorizontalLayout hlPassRepeat = new HorizontalLayout();

    private Button btnChange = new Button(getTranslation("user.change"));
    private HorizontalLayout hlAviso = new HorizontalLayout();


    private WebUserService usuarioService;

    private AuthenticatedUser authenticatedUser;

    public AccountView(WebUserService usuarioService, AuthenticatedUser authenticatedUser) {
        this.usuarioService = usuarioService;
        this.authenticatedUser = authenticatedUser;
        hlPass.add(pPass, tfPass);
        hlPassRepeat.add(pPassRepeat, tfPassRepeat);

        add(hPerfil, hPass,hlPass, hlPassRepeat, btnChange);

        btnChange.addClickListener(e -> {
            if(tfPass.getValue().equals(tfPassRepeat.getValue()) && tfPass.getValue() != null && tfPassRepeat.getValue() != null){
                WebUser usuario = authenticatedUser.get().get();
                usuarioService.changePass(usuario, tfPass.getValue());
                hlAviso.getStyle().set("font-size", "14px");
                hlAviso.getStyle().set("background-color", "hsla(145, 76%, 44%, 0.22)");
                hlAviso.getStyle().set("border-radius", "var(--lumo-border-radius-m)");
                hlAviso.add(new Icon(VaadinIcon.CHECK), new Paragraph(getTranslation("user.success")));
            }else {
                hlAviso.getStyle().set("font-size", "14px");
                hlAviso.getStyle().set("background-color", "hsla(0, 76%, 44%, 0.22)");
                hlAviso.getStyle().set("border-radius", "var(--lumo-border-radius-m)");
                hlAviso.add(new Icon(VaadinIcon.CLOSE), new Paragraph(getTranslation("user.error")));
            }
            add(hlAviso);
        });
    }
}
