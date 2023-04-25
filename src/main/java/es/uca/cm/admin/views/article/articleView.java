package es.uca.cm.admin.views.article;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import es.uca.cm.admin.views.MainLayout;
import es.uca.cm.admin.webUser.WebUser;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@PermitAll
@PageTitle("Gestíon de artículos")
@Route(value = "articulos", layout = MainLayout.class)
public class articleView extends HorizontalLayout {
    private VerticalLayout vlGrid = new VerticalLayout();
    private VerticalLayout vlInfo = new VerticalLayout();
    private VerticalLayout vlSeparator = new VerticalLayout();

    private H1 hGrid = new H1(getTranslation("article.gestion"));

    private H2 hInfo = new H2(getTranslation("dashboard.search"));
    private HorizontalLayout hlBuscador = new HorizontalLayout();

    private ComboBox<String> cbUsuario = new ComboBox<>(getTranslation("account.home"));
    private Paragraph pNumeroCuenta = new Paragraph(getTranslation("account.number"));
    private Grid<WebUser> gridCuenta = new Grid<>(WebUser.class, false);

    private Button btnAdd = new Button(getTranslation("article.add"));

    private Button btnDelete = new Button(getTranslation("account.delete"));

    private VerticalLayout hlAdd = new VerticalLayout();


    public articleView() {
        hGrid.setClassName("title");
        hInfo.setClassName("title");

        vlGrid.setWidth("70%");
        vlSeparator.setWidth("2px");
        vlInfo.setWidth("30%");

        vlSeparator.getStyle().set("background-color", "var(--lumo-contrast-10pct)");
        vlSeparator.getStyle().set("padding", "0");

        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        vlGrid.add(hGrid,
                new Hr(),
                gridCuenta,
                btnAdd);

        vlInfo.setAlignItems(FlexComponent.Alignment.CENTER);
        hlBuscador.setAlignItems(FlexComponent.Alignment.END);
        pNumeroCuenta.getStyle().set("fontWeight", "600");
        /*Grid cliente*/

        gridCuenta.addColumn(WebUser::getEmail).setHeader("Número de tarjeta").setAutoWidth(true).setSortable(true);
        gridCuenta.addColumn(WebUser::getEmail).setHeader("Fecha de creación").setAutoWidth(true).setSortable(true);
        gridCuenta.addColumn(WebUser::getEmail).setHeader("Saldo").setSortable(true);
        gridCuenta.addColumn(WebUser::getEmail).setHeader("DNI Cliente").setAutoWidth(true).setSortable(true);
        //actualizamos el UI
        updateUI();
        //comboBox
        List<String> aCuentas = new ArrayList<>();
        /*for (Cuenta cuenta : cuentaService.loadCuentas()) {
            aCuentas.add(cuenta.getNumeroCuenta());
        }*/
        cbUsuario.setItems(aCuentas);


        hlBuscador.add(pNumeroCuenta,
                cbUsuario);
        vlInfo.add(hInfo,
                new Hr(),
                hlBuscador,
                btnDelete);

        setHeight("100%");
        add(vlGrid,
                vlSeparator,
                vlInfo);

        btnAdd.addClickListener(e -> {
            UI.getCurrent().getPage().setLocation("/create/articulo");
        });

        btnDelete.addClickListener(e -> {
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader(getTranslation("confirm.title"));
            dialog.setText(getTranslation("confirm.body"));

            dialog.setCancelable(true);

            dialog.setCancelText(getTranslation("confirm.no"));

            dialog.setConfirmText(getTranslation("confirm.yes"));
            dialog.addConfirmListener(event ->  {
                if(cbUsuario.getValue() == null){
                    Notification notification = new Notification();
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);

                    Div text = new Div(new Text(getTranslation("account.accountError")));

                    Button closeButton = new Button(new Icon("lumo", "cross"));
                    closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                    closeButton.getElement().setAttribute("aria-label", "Close");
                    closeButton.addClickListener(ee -> {
                        notification.close();
                    });

                    HorizontalLayout layout = new HorizontalLayout(text, closeButton);
                    layout.setAlignItems(FlexComponent.Alignment.CENTER);

                    notification.add(layout);
                    notification.open();
                }
                else {

                }
                ;});

            dialog.open();
        });
    }

    private void updateUI(){
    }


}