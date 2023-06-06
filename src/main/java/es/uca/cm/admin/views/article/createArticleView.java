package es.uca.cm.admin.views.article;
import com.vaadin.flow.component.notification.Notification;
import es.uca.cm.admin.Firebase.StorageService;
import es.uca.cm.admin.openAi.DALL.DALLEController;
import es.uca.cm.admin.openAi.GPT.GPTController;

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
import org.springframework.beans.factory.annotation.Autowired;
import es.uca.cm.admin.views.article.articleService.Article;
import es.uca.cm.admin.views.article.articleService.ArticleService;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@PermitAll
@PageTitle("Creación de artículo")
@Route(value = "create/articulo", layout = MainLayout.class)
public class createArticleView extends VerticalLayout {
    private Tabs tabs = new Tabs();
    private Tab tabNuevo = new Tab(getTranslation("article.create"));
    private Tab tabModificar = new Tab(getTranslation("article.generate"));

    private H1 hNuevoUsuario = new H1(getTranslation("article.create"));
    HorizontalLayout hlAccionesNuevoUsuario = new HorizontalLayout();
    private H1 hEliminarUsuario = new H1(getTranslation("article.createGPT"));
    private FormLayout formNuevoUsuario = new FormLayout();
    private FormLayout formEliminarUsuario = new FormLayout();
    private VerticalLayout vlNuevoUsuario = new VerticalLayout();
    private VerticalLayout vlModificarUsuario = new VerticalLayout();
    private VerticalLayout vlDashboard = new VerticalLayout();

    private Button btnSave = new Button(getTranslation("article.create"));
    private Button btnVaciar = new Button(getTranslation("article.clear"));

    private Button btnSaveGPT = new Button(getTranslation("article.create"));
    private Button btnGenerate = new Button(getTranslation("article.generate"));

    private HorizontalLayout hlAviso = new HorizontalLayout();

    private ConfirmDialog cdlogNuevoUsuario = new ConfirmDialog();
    private FormLayout fFormNuevoUsuario = new FormLayout();
    private Paragraph plog = new Paragraph();

    private TextField prompt = new TextField(getTranslation("article.gpt.prompt"));
    private TextField txtTituloGPT = new TextField(getTranslation("article.titles"));
    private TextArea txtDescripcionGPT = new TextArea(getTranslation("article.description"));
    private TextArea txtCuerpoArticuloGPT = new TextArea(getTranslation("article.body.view"));
    private ComboBox<String> cmbCategoriasGPT = new ComboBox<>(getTranslation("article.category"));

    private Image imgImagenPortadaGPT = new Image("https://via.placeholder.com/150.png", "Imagen de marcador de posición");

    @Autowired
    private ArticleService articleService;
    public createArticleView(ArticleService articleService) {
        this.articleService = articleService;
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
        TextField txtTitulo = new TextField(getTranslation("article.titles"));
        TextArea txtDescripcion = new TextArea(getTranslation("article.description"));
        TextArea txtCuerpoArticulo = new TextArea(getTranslation("article.body.view"));
        ComboBox<String> cmbCategorias = new ComboBox<>(getTranslation("article.category"));
        cmbCategorias.setItems(getTranslation("article.violencia.genero"), getTranslation("article.igualdad"), getTranslation("article.violencia.sexual"));
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        Image imgImagenPortada = new Image();

        ComboBox<String> selector = new ComboBox<>(getTranslation("article.ambito.view"));
        selector.setItems(getTranslation("article.create.nacional"), getTranslation("article.create.comunidad"), getTranslation("article.create.ciudad"));
        TextField txtNombre = new TextField(getTranslation("article.name.ambito"));
        selector.addValueChangeListener(event -> {
            String selectedOption = event.getValue();
            if (getTranslation("article.create.comunidad").equals(selectedOption) || getTranslation("article.create.ciudad").equals(selectedOption)) {
                formNuevoUsuario.add(txtNombre);
            } else {
                formNuevoUsuario.remove(txtNombre);
            }
        });

        // Configurar opciones de subida de archivo
        upload.setMaxFileSize(20 * 1024 * 1024); // Tamaño máximo de archivo: 5 MB
        upload.setAcceptedFileTypes("image/*"); // Tipos de archivo aceptados: JPEG y PNG

        // Configurar acción de subida de archivo
        upload.addSucceededListener(event -> {
            //imgImagenPortada.setSrc(buffer.getFileData().getStream());
            imgImagenPortada.setVisible(true);
        });

        formNuevoUsuario.add(
               txtTitulo,txtDescripcion, txtCuerpoArticulo, cmbCategorias, upload, imgImagenPortada, selector
        );

        fFormNuevoUsuario.setColspan(txtTitulo, 2);
        fFormNuevoUsuario.setColspan(txtDescripcion, 2);
        fFormNuevoUsuario.setColspan(txtCuerpoArticulo, 2);
        fFormNuevoUsuario.setColspan(cmbCategorias, 2);
        fFormNuevoUsuario.setColspan(upload, 2);
        fFormNuevoUsuario.setColspan(imgImagenPortada, 2);
        vlNuevoUsuario.add(hNuevoUsuario, new Hr(), formNuevoUsuario, hlAccionesNuevoUsuario);

        hlAviso.setAlignItems(FlexComponent.Alignment.CENTER);
        hlAviso.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hlAviso.setWidth("100%");

        Button btnUsuario = new Button(getTranslation("dashboardUser.addUser"));
        Button btnDelete = new Button("dashboardUser.deleteUser");

        Button btnDeleteUser = new Button(getTranslation("article.reloadGPT"));
        Button btnModificar = new Button(getTranslation("article.reloadDALL"));
        btnDeleteUser.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnModificar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        cmbCategoriasGPT.setItems(getTranslation("article.violencia.genero"), getTranslation("article.igualdad"), getTranslation("article.violencia.sexual"));
        txtTituloGPT.setReadOnly(true);
        txtDescripcionGPT.setReadOnly(true);
        txtCuerpoArticuloGPT.setReadOnly(true);
        Div marco = new Div();

        // Establecer el ancho y alto del marco
        marco.getStyle().set("width", "200px");
        marco.getStyle().set("height", "200px");

        // Establecer el fondo y el borde del marco usando CSS
        marco.getStyle().set("background-color", "gray");
        marco.getStyle().set("border", "2px solid black");


        // Establecer el ancho y alto de la imagen para que se ajuste al marco
        imgImagenPortadaGPT.setWidth("100%");
        imgImagenPortadaGPT.setHeight("100%");

        marco.add(imgImagenPortadaGPT);

        btnSaveGPT.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnGenerate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        TextField txtNombreGPT = new TextField(getTranslation("article.name.ambito"));
        ComboBox<String> selectorGPT = new ComboBox<>(getTranslation("article.ambito.view"));
        selectorGPT.setItems(getTranslation("article.create.nacional"), getTranslation("article.create.comunidad"), getTranslation("article.create.ciudad"));
        selectorGPT.addValueChangeListener(event -> {
            String selectedOption = event.getValue();
            if (getTranslation("article.create.comunidad").equals(selectedOption) || getTranslation("article.create.ciudad").equals(selectedOption)) {
                formEliminarUsuario.addComponentAtIndex(3, txtNombreGPT);
            } else {
                formEliminarUsuario.remove(txtNombreGPT);
            }
        });

        formEliminarUsuario.add(prompt, cmbCategoriasGPT, selectorGPT,txtTituloGPT,txtDescripcionGPT, txtCuerpoArticuloGPT, btnDeleteUser, btnModificar,
                btnSaveGPT);
        fFormNuevoUsuario.setColspan(prompt, 2);
        fFormNuevoUsuario.setColspan(txtTituloGPT, 2);
        fFormNuevoUsuario.setColspan(txtDescripcionGPT, 2);
        fFormNuevoUsuario.setColspan(txtCuerpoArticuloGPT, 2);
        fFormNuevoUsuario.setColspan(cmbCategoriasGPT, 2);

        vlModificarUsuario.add(marco, formEliminarUsuario);

        btnSaveGPT.addClickListener(e -> {
            if(prompt.getValue().isEmpty()) {
                cdlogNuevoUsuario.add(new H3("Error"), new Hr(), new Paragraph(getTranslation("article.error.tematica")));
                cdlogNuevoUsuario.open();
            }else if(txtTituloGPT.getValue().isEmpty() || txtDescripcionGPT.getValue().isEmpty() || txtCuerpoArticuloGPT.getValue().isEmpty()){
                cdlogNuevoUsuario.add(new H3("Error"), new Hr(), new Paragraph(getTranslation("article.error.rellenado")));
                cdlogNuevoUsuario.open();
            }else if(cmbCategoriasGPT.getValue().isEmpty()){
                cdlogNuevoUsuario.add(new H3("Error"), new Hr(), new Paragraph(getTranslation("article.error.category")));
                cdlogNuevoUsuario.open();
            }else if(imgImagenPortadaGPT.getSrc() == "https://via.placeholder.com/150.png" ) {
                cdlogNuevoUsuario.add(new H3("Error"), new Hr(), new Paragraph(getTranslation("article.error.imagen")));
                cdlogNuevoUsuario.open();
            }else if(selectorGPT.getValue() == null || (selectorGPT.getValue().equals(getTranslation("article.create.comunidad")) && txtNombreGPT.getValue().isEmpty()) ||
                    (selectorGPT.getValue().equals(getTranslation("article.create.ciudad")) && txtNombreGPT.getValue().isEmpty())) {
                cdlogNuevoUsuario.add(new H3("Error"), new Hr(), new Paragraph(getTranslation("article.error.ambito")));
                cdlogNuevoUsuario.open();
            }else {
                StorageService firebaseStorageService = new StorageService();
                try {
                    URL imageUrl = new URL(imgImagenPortadaGPT.getSrc());
                    InputStream inputStream = imageUrl.openStream();
                    String fileName = new String();
                    UUID uuid = UUID.randomUUID();
                    fileName = uuid.toString() + ".png";

                    String url = firebaseStorageService.uploadImage(inputStream, fileName);

                    if(selectorGPT.getValue().equals(getTranslation("article.create.comunidad"))){

                        if ( cmbCategoriasGPT.equals(getTranslation("article.violencia.genero")))  articleService.save(new Article(txtTituloGPT.getValue(), txtDescripcionGPT.getValue(), txtCuerpoArticuloGPT.getValue(), url, "Violencia de genero", null , txtNombreGPT.getValue()));
                        else articleService.save(new Article(txtTituloGPT.getValue(), txtDescripcionGPT.getValue(), txtCuerpoArticuloGPT.getValue(), url, cmbCategoriasGPT.getValue(), null, txtNombreGPT.getValue()));

                    }else if (selectorGPT.getValue().equals(getTranslation("article.create.ciudad"))){

                        if ( cmbCategoriasGPT.equals(getTranslation("article.violencia.genero")))  articleService.save(new Article(txtTituloGPT.getValue(), txtDescripcionGPT.getValue(), txtCuerpoArticuloGPT.getValue(), url, "Violencia de genero", txtNombreGPT.getValue(), null));
                        else articleService.save(new Article(txtTituloGPT.getValue(), txtDescripcionGPT.getValue(), txtCuerpoArticuloGPT.getValue(), url, cmbCategoriasGPT.getValue(), txtNombreGPT.getValue(), null));

                    }else{

                        if ( cmbCategoriasGPT.equals(getTranslation("article.violencia.genero")))  articleService.save(new Article(txtTituloGPT.getValue(), txtDescripcionGPT.getValue(), txtCuerpoArticuloGPT.getValue(), url, "Violencia de genero",null , null));
                        else articleService.save(new Article(txtTituloGPT.getValue(), txtDescripcionGPT.getValue(), txtCuerpoArticuloGPT.getValue(), url, cmbCategoriasGPT.getValue(), null, null));
                    }

                    cdlogNuevoUsuario.add(new H3("Éxito"), new Hr(), new Paragraph("Artículo creado correctamente"));
                    clearData();
                    cdlogNuevoUsuario.open();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    cdlogNuevoUsuario.add(new H3("Error"), new Hr(), new Paragraph("No se ha podido crear el artículo"));
                    cdlogNuevoUsuario.open();
                }
            }
        });

        btnGenerate.addClickListener(e -> {
            if (prompt.getValue().isEmpty()) {
                cdlogNuevoUsuario.add(new H3("Error"), new Hr(), new Paragraph("Rellena el campo de la temática para generar el artículo"));
                cdlogNuevoUsuario.open();
            } else {
                try {
                    generateGPT();
                    generateDALL();
                } catch (Exception ex) {
                    cdlogNuevoUsuario.add(new H3("Error"), new Hr(), new Paragraph("Error al conectar con OpenAI, intentelo de nuevo más tarde"));
                    cdlogNuevoUsuario.open();
                }
            }
        });
        btnModificar.addClickListener(e -> {
            try {
                generateDALL();
            } catch (Exception ex) {
                Notification notification = new Notification("Error con la conexión con OpenAi", 3000);
                notification.setPosition(Notification.Position.BOTTOM_END);
                notification.open();
                throw new RuntimeException(ex);
            }
        });
        btnDeleteUser.addClickListener(event -> {
            try {
                generateGPT();
            } catch (Exception e) {
                Notification notification = new Notification("Error con la conexión con OpenaAi", 3000);
                notification.setPosition(Notification.Position.BOTTOM_END);
                notification.open();
                throw new RuntimeException(e);
            }
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
            if (txtTitulo.getValue().isEmpty() || txtDescripcion.getValue().isEmpty() || txtCuerpoArticulo.getValue().isEmpty() || cmbCategorias.getValue().isEmpty() ||
            selector.getValue() == null || (selector.getValue().equals(getTranslation("article.create.comunidad")) && txtNombre.getValue().isEmpty()) || (selector.getValue().equals(getTranslation("article.create.ciudad")) && txtNombre.getValue().isEmpty()) ) {
                cdlogNuevoUsuario.add(new H3("Error"), new Hr(), new Paragraph("Debes rellenar todos los campos"));
                cdlogNuevoUsuario.open();
            } else {
                StorageService firebaseStorageService = new StorageService();
                try {
                    String url = firebaseStorageService.uploadImage(buffer.getInputStream(), buffer.getFileData().getFileName());


                    if(selector.getValue().equals(getTranslation("article.create.comunidad"))){

                        if ( cmbCategorias.equals(getTranslation("article.violencia.genero")))  articleService.save(new Article(txtTitulo.getValue(), txtDescripcion.getValue(), txtCuerpoArticulo.getValue(), url, "Violencia de genero", null , txtNombre.getValue()));
                        else articleService.save(new Article(txtTitulo.getValue(), txtDescripcion.getValue(), txtCuerpoArticulo.getValue(), url, cmbCategorias.getValue(), null, txtNombre.getValue()));

                    }else if (selector.getValue().equals(getTranslation("article.create.ciudad"))){

                        if ( cmbCategorias.equals(getTranslation("article.violencia.genero")))  articleService.save(new Article(txtTitulo.getValue(), txtDescripcion.getValue(), txtCuerpoArticulo.getValue(), url, "Violencia de genero", txtNombre.getValue(), null));
                        else articleService.save(new Article(txtTitulo.getValue(), txtDescripcion.getValue(), txtCuerpoArticulo.getValue(), url, cmbCategorias.getValue(), txtNombre.getValue(), null));

                    }else{

                        if ( cmbCategorias.equals(getTranslation("article.violencia.genero")))  articleService.save(new Article(txtTitulo.getValue(), txtDescripcion.getValue(), txtCuerpoArticulo.getValue(), url, "Violencia de genero",null , null));
                        else articleService.save(new Article(txtTitulo.getValue(), txtDescripcion.getValue(), txtCuerpoArticulo.getValue(), url, cmbCategorias.getValue(), null, null));
                    }

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
    }

    private void clearData() {
        formNuevoUsuario.getChildren().forEach(child -> child.getElement().setProperty("value", ""));
    }

    private void generateGPT() throws Exception {
        GPTController chatGPTController = new GPTController();
        String article = chatGPTController.GPT_3(prompt.getValue());

       // System.out.println(article);

        String titulo = "";
        String descripcion = "";
        String cuerpo = "";

        int tituloIndex = article.indexOf("Título:");
        int descripcionIndex = article.indexOf("Descripción:");
        int cuerpoIndex = article.indexOf("Cuerpo de artículo:");

        if(cuerpoIndex == -1) cuerpoIndex = article.indexOf("Cuerpo del artículo:");



        if (tituloIndex != -1 && descripcionIndex != -1 && cuerpoIndex != -1) {
            titulo = article.substring(tituloIndex + 8, descripcionIndex).trim();
            descripcion = article.substring(descripcionIndex + 13, cuerpoIndex).trim();
            cuerpo = article.substring(cuerpoIndex + 19).trim();
        }
        if(tituloIndex == -1 && descripcionIndex != -1 && cuerpoIndex != -1){
            cuerpo = article;
        }

        txtTituloGPT.setValue(titulo);
        txtDescripcionGPT.setValue(descripcion);
        txtCuerpoArticuloGPT.setValue(cuerpo);
        txtTituloGPT.setReadOnly(false);
        txtDescripcionGPT.setReadOnly(false);
        txtCuerpoArticuloGPT.setReadOnly(false);
    }

    private void generateDALL() throws Exception {
        DALLEController chatDALLController = new DALLEController();
        String urlImage = chatDALLController.DALLE(prompt.getValue());


        imgImagenPortadaGPT.setSrc(urlImage);
    }
}