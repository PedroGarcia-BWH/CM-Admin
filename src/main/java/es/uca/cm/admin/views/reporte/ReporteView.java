package es.uca.cm.admin.views.reporte;


import com.google.firebase.auth.FirebaseAuthException;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import es.uca.cm.admin.Firebase.AuthService;
import es.uca.cm.admin.hilo.Hilo;
import es.uca.cm.admin.hilo.HiloService;
import es.uca.cm.admin.views.MainLayout;
import es.uca.cm.admin.views.article.articleService.Article;
import es.uca.cm.admin.views.reporte.reporteService.Reporte;
import es.uca.cm.admin.views.reporte.reporteService.ReporteService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static es.uca.cm.admin.Firebase.StorageService.getImage;
import static es.uca.cm.admin.Firebase.StorageService.getPerfilUser;

@PermitAll
@PageTitle("Gestión de reportes")
@Route(value = "reportes", layout = MainLayout.class)
public class ReporteView extends VerticalLayout {

    private Tabs tabs = new Tabs();

    private Tab tabAbierto = new Tab(getTranslation("reporte.gestion"));
    private Tab tabCerrado = new Tab(getTranslation("reporte.gestionClose"));

    private H1 hArticleOpen = new H1(getTranslation("reporte.gestion"));
    private H1 hArticleClosed = new H1(getTranslation("reporte.gestionClose"));


    private VerticalLayout vlAbierto= new VerticalLayout();
    private VerticalLayout vlCerrado = new VerticalLayout();

    private Grid<Reporte> gridAbierto = new Grid<>(Reporte.class, false);

    private Grid<Reporte> gridCerrado = new Grid<>(Reporte.class, false);

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private HiloService hiloService;

    @Autowired
    private ReporteEmail reporteEmail;
    @Autowired
    private AuthService authService ;

    private TextField searchFieldAbierto = new TextField();

    public ReporteView(ReporteService reporteService, HiloService hiloService, AuthService authService, ReporteEmail reporteEmail) {
        this.reporteService = reporteService;
        this.hiloService = hiloService;
        this.authService = authService;
        this.reporteEmail = reporteEmail;

        gridAbierto.addColumn(Reporte::getId).setHeader(getTranslation("reporte.id")).setAutoWidth(true).setSortable(true);
        gridAbierto.addColumn(Reporte::getMotivo).setHeader(getTranslation("reporte.motivo")).setAutoWidth(true).setSortable(true);
        gridAbierto.addColumn(Reporte::getDescripcion).setHeader(getTranslation("reporte.descripcion")).setAutoWidth(true).setSortable(true);
        gridAbierto.addColumn(Reporte::getReportado_uuid).setHeader(getTranslation("reporte.reportado")).setAutoWidth(true).setSortable(true);
        gridAbierto.addColumn(Reporte::getReportador_uuid).setHeader(getTranslation("reporte.reportador")).setAutoWidth(true).setSortable(true);
        gridAbierto.addComponentColumn(reporte -> {
                    Optional<Hilo> hilo = hiloService.findById(UUID.fromString(reporte.getMensajeUuid()));
                    if(reporte.getMotivo().contains("foto") || reporte.getDescripcion().toLowerCase().contains("foto")){
                        Button button = new Button(getTranslation("reporte.photo"));
                        button.addClickListener(event -> {
                            try {
                                elementImageDialog(reporte.getReportado_uuid());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        return button;
                    }
                    if(hilo.isPresent()){
                        Button button = new Button(getTranslation("reporte.message"));
                        button.addClickListener(event -> elementMensajeDialog(hilo.get().getMensaje()));
                        return button;
                    }
                    return null;
                })
                .setAutoWidth(true).setHeader(getTranslation("reporte.mensaje"))
                .setSortable(false);
        gridAbierto.addColumn(Reporte::getDateCreated).setHeader(getTranslation("reporte.dateCreated")).setAutoWidth(true).setSortable(true);
        gridAbierto.addComponentColumn(reporte -> {
                    Button button = new Button(getTranslation("reporte.resolution"));
                    button.addClickListener(event -> resolutionDialog(reporte.getId().toString(), reporte.getMotivo(), reporte.getReportado_uuid(), reporte.getReportador_uuid()));
                    return button;
                })
                .setAutoWidth(true)
                .setHeader(getTranslation("reporte.accion"))
                .setSortable(false);



        gridCerrado.addColumn(Reporte::getId).setHeader(getTranslation("reporte.id")).setAutoWidth(true).setSortable(true);
        gridCerrado.addColumn(Reporte::getMotivo).setHeader(getTranslation("reporte.motivo")).setAutoWidth(true).setSortable(true);
        gridCerrado.addColumn(Reporte::getDescripcion).setHeader(getTranslation("reporte.descripcion")).setAutoWidth(true).setSortable(true);
        gridCerrado.addColumn(Reporte::getReportado_uuid).setHeader(getTranslation("reporte.reportado")).setAutoWidth(true).setSortable(true);
        gridCerrado.addColumn(Reporte::getReportador_uuid).setHeader(getTranslation("reporte.reportador")).setAutoWidth(true).setSortable(true);
        gridCerrado.addColumn(Reporte::getMensajeUuid).setHeader(getTranslation("reporte.mensaje"));
        gridCerrado.addColumn(Reporte::getDateCreated).setHeader(getTranslation("reporte.dateCreated")).setAutoWidth(true).setSortable(true);
        gridCerrado.addColumn(Reporte::getDateEliminated).setHeader(getTranslation("reporte.dateClosed")).setAutoWidth(true).setSortable(true);

        TextField searchFieldCerrado = new TextField();
        searchFieldCerrado.setWidth("30%");
        searchFieldCerrado.setPlaceholder(getTranslation("reporte.search"));
        searchFieldCerrado.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchFieldCerrado.setValueChangeMode(ValueChangeMode.EAGER);


        searchFieldAbierto.setWidth("30%");
        searchFieldAbierto.setPlaceholder(getTranslation("reporte.search"));
        searchFieldAbierto.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchFieldAbierto.setValueChangeMode(ValueChangeMode.EAGER);

        refreshUI();

        List<Reporte> reportesCerrados = reporteService.findByEliminationDateIsNotNull();


        GridListDataView<Reporte> dataViewCerrados = gridCerrado.setItems(reportesCerrados);

        searchFieldCerrado.addValueChangeListener(e -> {
                dataViewCerrados.refreshAll();
        });



        dataViewCerrados.addFilter(reporte -> {
            String searchTerm = searchFieldCerrado.getValue().trim();
            if (searchTerm.isEmpty()) return true;

            /*boolean matchesTitle = article.getTitle().toLowerCase().contains(searchTerm.toLowerCase());
            boolean matchesDescription = article.getDescription().toLowerCase().contains(searchTerm.toLowerCase());*/

            return true;
        });

        vlAbierto.add(hArticleOpen,  searchFieldAbierto, gridAbierto);
        vlCerrado.add(hArticleClosed, searchFieldCerrado, gridCerrado);

        tabs.add(tabAbierto, tabCerrado);
        tabs.setWidthFull();
        tabs.addThemeVariants(TabsVariant.LUMO_CENTERED);
        tabs.addSelectedChangeListener(e -> {
            removeAll();
            if(tabAbierto.isSelected())
                add(tabs, vlAbierto);
            else
                add(tabs, vlCerrado);
        });

        add(tabs, vlAbierto);
    }

    private void elementImageDialog(String uuid) throws IOException {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.setWidth("900px");
        dialog.setHeight("450px");
        dialog.setResizable(true);
        dialog.setDraggable(true);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        H2 title = new H2(getTranslation("reporte.imagen"));

        Image image = new Image();
        image.setSrc(getPerfilUser(uuid));
        if(image.getSrc().equals("")){
            StreamResource resource = new StreamResource("account_profile_user_icon_190494.png", () -> getClass().getResourceAsStream("/account_profile_user_icon_190494.png"));
            image.setHeight("50px");
            image.setWidth("50px");
            image.setSrc(resource);
        }
        image.setWidth("100%");
        image.setHeight("100%");

        Button closeButton = new Button(getTranslation("reporte.close"), event -> dialog.close());
        closeButton.setWidth("100%");
        closeButton.setHeight("100%");

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(title, image, closeButton);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        dialog.add(verticalLayout);
        dialog.open();
    }

    private void elementMensajeDialog(String mensaje) {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.setWidth("600px");
        dialog.setHeight("300px");
        dialog.setResizable(true);
        dialog.setDraggable(true);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        // Creamos un elemento H2 con el título "Mensaje Reportado"
        H2 title = new H2(getTranslation("reporte.mensaje.reportado"));
        dialog.add(title);

        // Creamos un componente de texto con el mensaje y lo agregamos al layout vertical
        TextArea textArea = new TextArea();
        textArea.setValue(mensaje);
        textArea.setReadOnly(true);
        VerticalLayout verticalLayout = new VerticalLayout(textArea);

        // Agregamos el botón de cerrar al layout vertical
        Button closeButton = new Button(getTranslation("reporte.close"), event -> dialog.close());
        closeButton.setWidth("100%");
        verticalLayout.add(closeButton);

        // Ajustamos el layout vertical
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        verticalLayout.setSizeFull();

        // Agregamos el layout vertical al diálogo y lo abrimos
        dialog.add(verticalLayout);
        dialog.open();
    }

    private void resolutionDialog(String reporte_id, String motivo, String reportado_uuid, String reportador_uuid) {
        // Creamos los checkboxes
        Checkbox enviarAvisoCheckbox = new Checkbox(getTranslation("reporte.warning"));
        Checkbox banearCheckbox = new Checkbox(getTranslation("reporte.ban"));
        Checkbox cerrarReporteCheckbox = new Checkbox(getTranslation("reporte.close.report"));
        TextArea textArea = new TextArea(getTranslation("reporte.resolution.title"));
        textArea.setPlaceholder(getTranslation("reporte.resloution.here"));

        Dialog dialog = new Dialog();

        // Creamos el botón para realizar las acciones
        Button realizarAccionesButton = new Button(getTranslation("reporte.doaction"), event -> {
            // Determinamos qué acción/es realizar
            int actionType = getActionType(enviarAvisoCheckbox.getValue(), banearCheckbox.getValue(), cerrarReporteCheckbox.getValue());
            switch (actionType) {
                case 1:
                    try {
                        enviarAviso(reporte_id,reportado_uuid, reportador_uuid, motivo, textArea.getValue());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 2:
                    try {
                        banear(reporte_id, reportado_uuid ,reportador_uuid, motivo, textArea.getValue());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (FirebaseAuthException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 3:
                    try {
                        cerrarReporte(reporte_id,motivo,textArea.getValue(), authService.getEmailById(reportado_uuid));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }
            refreshUI();
            dialog.close();
        });

        // Creamos el diálogo y lo abrimos
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.setWidth("900px");
        dialog.setHeight("600px");
        dialog.setResizable(true);
        dialog.setDraggable(true);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        // Creamos el layout vertical y añadimos los componentes
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(new H2(getTranslation("reporte.resolution")), enviarAvisoCheckbox, banearCheckbox, cerrarReporteCheckbox, textArea, realizarAccionesButton);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        verticalLayout.setSizeFull();

        // Creamos el diálogo y lo abrimos
        dialog.add(verticalLayout);
        dialog.open();
    }

    private int getActionType(boolean enviarAviso, boolean banear, boolean cerrarReporte) {
        if (enviarAviso) {
            return 1;
        } else if (banear) {
            return 2;
        } else if (cerrarReporte) {
            return 3;
        } else {
            return 0;
        }
    }

    private void banear(String reporte_id, String reportado_uuid, String reportador_uuid, String motivo, String resolucion) throws IOException, FirebaseAuthException {
        reporteService.closeReporte(reporte_id);
        authService.toggleUserStatus(reportado_uuid);
        reporteEmail.sendAvisoReportado(authService.getEmailById(reportado_uuid), motivo, resolucion);
        reporteEmail.sendAvisoReportador(authService.getEmailById(reportador_uuid), motivo, resolucion);
    }

    private void enviarAviso(String reporte_id,  String reportado_uuid, String reportador_uuid, String motivo, String resolucion) throws IOException {
        reporteService.closeReporte(reporte_id);
        reporteEmail.sendAvisoReportado(authService.getEmailById(reportado_uuid), motivo, resolucion);
        reporteEmail.sendAvisoReportador(authService.getEmailById(reportador_uuid), motivo, resolucion);
    }

    private void cerrarReporte(String reporte_id, String motivo, String resolucion, String email_reportador) {
        reporteService.closeReporte(reporte_id);
        reporteEmail.sendAvisoReportador(email_reportador, motivo, resolucion);
    }

    private void refreshUI(){
        List<Reporte> reportesActivos = reporteService.findByEliminationDateIsNull();

        GridListDataView<Reporte> dataViewActivos = gridAbierto.setItems(reportesActivos);

        searchFieldAbierto.addValueChangeListener(e -> {
            dataViewActivos.refreshAll();
        });

        dataViewActivos.addFilter(reporte -> {
            String searchTerm = searchFieldAbierto.getValue().trim();
            if (searchTerm.isEmpty()) return true;

            /*boolean matchesTitle = article.getTitle().toLowerCase().contains(searchTerm.toLowerCase());
            boolean matchesDescription = article.getDescription().toLowerCase().contains(searchTerm.toLowerCase());*/


            return true;
        });
    }

}
