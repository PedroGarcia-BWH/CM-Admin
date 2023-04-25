package es.uca.cm.admin.views.article;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.cm.admin.Application;
import es.uca.cm.admin.views.MainLayout;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.formlayout.FormLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@PermitAll
@PageTitle("Creación de artículo")
@Route(value = "create/articulo", layout = MainLayout.class)
public class createArticleView extends VerticalLayout {
    private Tabs tabs = new Tabs();
    private Tab tabNuevo = new Tab(getTranslation("article.create"));
    private Tab tabModificar = new Tab(getTranslation("article.generate"));

    private H1 hNuevoUsuario = new H1(getTranslation("article.create"));
    HorizontalLayout hlAccionesNuevoUsuario = new HorizontalLayout();
    private H1 hEliminarUsuario = new H1(getTranslation("dashboardUser.modify"));
    HorizontalLayout hlAccionesModificarUsuario = new HorizontalLayout();
    private FormLayout formNuevoUsuario = new FormLayout();
    private FormLayout formEliminarUsuario = new FormLayout();
    private VerticalLayout vlNuevoUsuario = new VerticalLayout();
    private VerticalLayout vlModificarUsuario = new VerticalLayout();

    private VerticalLayout vlDashboard = new VerticalLayout();

    private TextField tfNameMod = new TextField(getTranslation("dashboardUser.name"));
    private PasswordField tfPasswordMod = new PasswordField(getTranslation("dashboardUser.password"));
    private DatePicker dpBirthDateMod = new DatePicker(getTranslation("dashboardUser.birth"));


    private Button btnSave = new Button(getTranslation("article.create"));
    private Button btnVaciar = new Button(getTranslation("article.clear"));
    private HorizontalLayout hlAviso = new HorizontalLayout();

    private ConfirmDialog cdlogNuevoUsuario = new ConfirmDialog();
    private FormLayout fFormNuevoUsuario = new FormLayout();
    private Paragraph plog = new Paragraph();

    private ComboBox<String> cbUsuario = new ComboBox<>(getTranslation("dashboardUser.DNI"));
    public createArticleView() {
        this.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        this.setAlignItems(FlexComponent.Alignment.CENTER);

        fFormNuevoUsuario.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        fFormNuevoUsuario.add(new Hr(), plog);

        tabs.add(tabNuevo, tabModificar);

        vlDashboard.setWidthFull();
        vlDashboard.setPadding(false);
        vlDashboard.setMargin(false);

        hNuevoUsuario.setClassName("title");
        hEliminarUsuario.setClassName("title");
        vlNuevoUsuario.setClassName("box");
        vlModificarUsuario.setClassName("box");
        vlNuevoUsuario.setWidth("70vw");
        vlModificarUsuario.setWidth("70vw");
        vlNuevoUsuario.setPadding(true);
        vlModificarUsuario.setPadding(true);

        formNuevoUsuario.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("500px", 4));

        formEliminarUsuario.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        hlAccionesNuevoUsuario.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        hlAccionesNuevoUsuario.setWidthFull();
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        hlAccionesNuevoUsuario.add(btnVaciar, btnSave);
        TextField txtTitulo = new TextField("Título");
        TextField txtDescripcion = new TextField("Descripción");
        TextArea txtCuerpoArticulo = new TextArea("Cuerpo de Artículo");
        ComboBox<String> cmbCategorias = new ComboBox<>("Categorías");
        cmbCategorias.setItems("Violencia de género", "Igualdad", "Violencia Sexual");
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        Image imgImagenPortada = new Image();

        // Configurar opciones de subida de archivo
        upload.setMaxFileSize(5 * 1024 * 1024); // Tamaño máximo de archivo: 5 MB
        upload.setAcceptedFileTypes("image/jpeg", "image/png"); // Tipos de archivo aceptados: JPEG y PNG

        // Configurar acción de subida de archivo
        upload.addSucceededListener(event -> {
            //imgImagenPortada.setSrc(buffer.getFileData().getStream());
            imgImagenPortada.setVisible(true);
        });

        formNuevoUsuario.add(
               txtTitulo,txtDescripcion, txtCuerpoArticulo, cmbCategorias, upload, imgImagenPortada
        );

        fFormNuevoUsuario.setColspan(txtTitulo, 2);
        fFormNuevoUsuario.setColspan(txtDescripcion, 2);
        fFormNuevoUsuario.setColspan(txtCuerpoArticulo, 2);
        fFormNuevoUsuario.setColspan(cmbCategorias, 2);
        fFormNuevoUsuario.setColspan(upload, 2);
        fFormNuevoUsuario.setColspan(imgImagenPortada, 2);
        vlNuevoUsuario.add(hNuevoUsuario, new Hr(), formNuevoUsuario, hlAccionesNuevoUsuario);

        formEliminarUsuario.add();
        vlModificarUsuario.add(hEliminarUsuario, new Hr(), formEliminarUsuario);

        hlAviso.setAlignItems(FlexComponent.Alignment.CENTER);
        hlAviso.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hlAviso.setWidth("100%");

        Button btnUsuario = new Button(getTranslation("dashboardUser.addUser"));
        Button btnDelete = new Button("dashboardUser.deleteUser");

        Button btnDeleteUser = new Button(getTranslation("card.delete"));
        Button btnModificar = new Button(getTranslation("dashboardUser.modifyUser"));
        btnDeleteUser.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnDeleteUser.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnModificar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        hlAccionesModificarUsuario.add(btnDeleteUser, btnModificar);
        hlAccionesModificarUsuario.setWidthFull();
        hlAccionesModificarUsuario.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        List<String> DNIs = new ArrayList<>();
        /*for (Usuario usuario : usuarioService.findAll()) {
            DNIs.add(usuario.getDNI());
        }*/
        cbUsuario.setItems(DNIs);

        FormLayout hlDatosUsuario = new FormLayout();
        hlDatosUsuario.setResponsiveSteps(new FormLayout.ResponsiveStep("0px", 1), new FormLayout.ResponsiveStep("500px", 3));
        hlDatosUsuario.add( tfNameMod, dpBirthDateMod);
        tfNameMod.setReadOnly(true);
        dpBirthDateMod.setReadOnly(true);
        tfNameMod.setVisible(false);
        tfPasswordMod.setVisible(false);
        dpBirthDateMod.setVisible(false);
        vlModificarUsuario.add(cbUsuario,
                hlDatosUsuario,
                tfPasswordMod,
                hlAccionesModificarUsuario);

        cbUsuario.addValueChangeListener( e -> {

        });
        btnModificar.addClickListener(e -> {


        });
        btnDeleteUser.addClickListener(event -> {

        });

        tabs.setWidthFull();
        tabs.addThemeVariants(TabsVariant.LUMO_CENTERED);
        tabs.addSelectedChangeListener(e -> {
            removeAll();
            if(tabNuevo.isSelected())
                add(tabs, vlNuevoUsuario);
            else
                add(tabs, vlModificarUsuario);
        });

        add(tabs, vlNuevoUsuario);


        btnSave.addClickListener(e -> {
            if (txtTitulo.getValue().isEmpty() || txtDescripcion.getValue().isEmpty() || txtCuerpoArticulo.getValue().isEmpty() || cmbCategorias.getValue().isEmpty()) {
                cdlogNuevoUsuario.add(new H3("Error"), new Hr(), new Paragraph("Debes rellenar todos los campos"));
                cdlogNuevoUsuario.open();
            } else {
                Application.FirebaseStorageService firebaseStorageService = new Application.FirebaseStorageService();
                try {
                    String url = firebaseStorageService.uploadImage(buffer.getInputStream(), buffer.getFileData().getFileName());
                    cdlogNuevoUsuario.add(new H3("Éxito"), new Hr(), new Paragraph("Artículo creado correctamente"));
                    clearData();
                    cdlogNuevoUsuario.open();
                } catch (IOException ex) {
                    cdlogNuevoUsuario.add(new H3("Error"), new Hr(), new Paragraph("No se ha podido crear el artículo"));
                    cdlogNuevoUsuario.open();
                }

            }
        });

        btnVaciar.addClickListener(e -> {
            clearData();
        });

        btnDelete.addClickListener(e -> {
        });
    }

    private void clearData() {
        formNuevoUsuario.getChildren().forEach(child -> child.getElement().setProperty("value", ""));
    }
}