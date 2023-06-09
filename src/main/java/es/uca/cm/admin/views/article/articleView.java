package es.uca.cm.admin.views.article;

import com.google.firebase.auth.FirebaseAuthException;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
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
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import es.uca.cm.admin.views.MainLayout;
import es.uca.cm.admin.views.article.articleService.Article;
import es.uca.cm.admin.views.article.articleService.ArticleService;
import es.uca.cm.admin.webUser.WebUser;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static es.uca.cm.admin.Firebase.AuthService.toggleUserStatus;
import static es.uca.cm.admin.Firebase.StorageService.getImage;


@PermitAll
@PageTitle("Gestión de artículos")
@Route(value = "articulos", layout = MainLayout.class)
public class articleView extends HorizontalLayout {
    private VerticalLayout vlGrid = new VerticalLayout();

    private H1 hGrid = new H1(getTranslation("article.gestion"));
    private Button btnAdd = new Button(getTranslation("article.add"));

    private VerticalLayout vlMain = new VerticalLayout();

    @Autowired
    private ArticleService _articleService;


    private Grid<Article> gridArticle = new Grid<>(es.uca.cm.admin.views.article.articleService.Article.class, false);



    public articleView(ArticleService articleService) {
        _articleService = articleService;
        setJustifyContentMode(JustifyContentMode.CENTER);
        this.setAlignItems(FlexComponent.Alignment.CENTER);
        setWidthFull();
        gridArticle.addComponentColumn(article -> {
                    Image image;
                    if(article.getUrlFrontPage().contains("storage.googleapis.com")){
                        try {
                            image = new Image(getImage(article.getUrlFrontPage()), "alt text");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        image = new Image(article.getUrlFrontPage(), "alt text");
                    }
                    image.setWidth("100px");
                    image.setHeight("100px");
                    return image;
                })
                .setAutoWidth(true)
                .setSortable(false);
        gridArticle.addColumn(es.uca.cm.admin.views.article.articleService.Article::getTitle).setHeader(getTranslation("article.titles")).setAutoWidth(true).setResizable(true).setSortable(true);
        gridArticle.addColumn(es.uca.cm.admin.views.article.articleService.Article::getDescription).setHeader(getTranslation("article.description")).setAutoWidth(false).setResizable(true).setSortable(true);
        gridArticle.addColumn(es.uca.cm.admin.views.article.articleService.Article::getCategory).setHeader(getTranslation("article.category")).setAutoWidth(true).setSortable(true);
        gridArticle.addColumn(es.uca.cm.admin.views.article.articleService.Article::getCreationDate).setHeader(getTranslation("article.date")).setAutoWidth(true).setSortable(true);
        gridArticle.addComponentColumn(article -> {
                    HorizontalLayout buttonLayout = new HorizontalLayout();

                    Button deleteButton = new Button(getTranslation("article.delete"));
                    deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
                    deleteButton.addClickListener(e -> {
                        boolean deleted = articleService.deleteById(article.getId());
                        if (deleted) {
                            Notification.show(getTranslation("article.delete.notification"), 3000, Notification.Position.BOTTOM_CENTER);
                        } else {
                            Notification.show(getTranslation("article.delete.notification.error"), 3000, Notification.Position.BOTTOM_CENTER);
                        }
                        refreshUI();
                    });
                    buttonLayout.add(deleteButton);

                    Button verButton = new Button(getTranslation("article.view"));
                    verButton.addClickListener(e -> {
                        Dialog dialog = new Dialog();
                        dialog.setCloseOnEsc(false);
                        dialog.setCloseOnOutsideClick(false);

                        VerticalLayout layout = new VerticalLayout();
                        H2 description = new H2(getTranslation("article.description.view"));
                        Label descriptionLabel = new Label(article.getDescription());
                        H2 body = new H2(getTranslation("article.body.view"));
                        Label bodyLabel = new Label( article.getBody());
                        H2 portada = new H2(getTranslation("article.image.view"));

                        Image image;
                        if(article.getUrlFrontPage().contains("storage.googleapis.com")){
                            try {
                                image = new Image(getImage(article.getUrlFrontPage()), "alt text");
                            } catch (IOException exc) {
                                throw new RuntimeException(exc);
                            }
                        } else {
                            image = new Image(article.getUrlFrontPage(), "alt text");
                        }
                        image.setWidth("100px");
                        image.setHeight("100px");
                        H2 creationDate = new H2(getTranslation("article.date.view"));
                        Label creationDateLabel = new Label(article.getCreationDate().toString());
                        H2 category = new H2(getTranslation("article.category.view"));
                        Label categoryLabel = new Label(article.getCategory());

                        H2 ambito = new H2(getTranslation("article.ambito.view"));
                        Label ambitoLabel;
                        if(article.getCity() == null && article.getComunidad() == null) {
                            ambitoLabel = new Label(getTranslation("article.nacional"));
                        }else if(article.getCity() != null) {
                            ambitoLabel = new Label(getTranslation("article.ciudad") + article.getCity());
                        }else {
                            ambitoLabel = new Label(getTranslation("article.comunidad") + article.getComunidad());
                        }

                        Button closeButton = new Button(getTranslation("reporte.close"));
                        closeButton.addClickListener(ex -> dialog.close());

                        layout.add(new H1(getTranslation("article.title.view") + article.getTitle()), description, descriptionLabel, body, bodyLabel, portada, image,
                                 category, categoryLabel, ambito, ambitoLabel, creationDate, creationDateLabel, closeButton);
                        dialog.add(layout);

                        dialog.open();
                    });
                    buttonLayout.add(verButton);

                    return buttonLayout;
                })
                .setHeader(getTranslation("reporte.accion"))
                .setAutoWidth(true)
                .setSortable(false);

        TextField searchField = new TextField();
        searchField.setWidth("30%");
        searchField.setPlaceholder(getTranslation("article.search"));
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        vlMain.add(hGrid, new Hr(), searchField);
        vlMain.add(gridArticle, btnAdd);


        add(vlMain);

        btnAdd.addClickListener(e -> {
            UI.getCurrent().getPage().setLocation("/create/articulo");
        });

        List<Article> articles = _articleService.findByEliminationDateIsNull();

        //Image img = new Image(articles.get(0).getUrlFrontPage(), "Front page image");
        //add(img);
        GridListDataView<Article> dataView = gridArticle.setItems(articles);

        searchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(article -> {
            String searchTerm = searchField.getValue().trim();
            if (searchTerm.isEmpty()) return true;

            boolean matchesTitle = article.getTitle().toLowerCase().contains(searchTerm.toLowerCase());
            boolean matchesDescription = article.getDescription().toLowerCase().contains(searchTerm.toLowerCase());


            return matchesTitle || matchesDescription;
        });


    }

    private void refreshUI(){
        List<Article> articles = _articleService.findByEliminationDateIsNull();

        //Image img = new Image(articles.get(0).getUrlFrontPage(), "Front page image");
        //add(img);
        GridListDataView<Article> dataView = gridArticle.setItems(articles);
    }

}