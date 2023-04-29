package es.uca.cm.admin.views.user;

import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuthException;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.cm.admin.views.MainLayout;
import jakarta.annotation.security.PermitAll;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static es.uca.cm.admin.Firebase.AuthService.getAllUsersFirebase;


@PermitAll
@PageTitle("Gestíon de usuarios")
@Route(value = "usuarios", layout = MainLayout.class)
public class UsersView extends HorizontalLayout {
    private VerticalLayout vlGrid = new VerticalLayout();

    private H1 hGrid = new H1(getTranslation("article.gestion"));

    private VerticalLayout vlMain = new VerticalLayout();


    private Grid<ExportedUserRecord> gridArticle = new Grid<>(ExportedUserRecord.class, false);



    public UsersView() throws IOException, FirebaseAuthException {
        setJustifyContentMode(JustifyContentMode.CENTER);
        this.setAlignItems(FlexComponent.Alignment.CENTER);
        setWidthFull();
        gridArticle.addColumn(ExportedUserRecord::getUid).setHeader(getTranslation("user.id")).setAutoWidth(true).setSortable(true);
        gridArticle.addColumn(ExportedUserRecord::getEmail).setHeader(getTranslation("user.email")).setAutoWidth(true).setSortable(true);
        gridArticle.addColumn(ExportedUserRecord::getDisplayName).setHeader(getTranslation("user.name")).setAutoWidth(true).setSortable(true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

// Agregar columnas al grid
        gridArticle.addColumn(record -> {
                    LocalDateTime creationTime = LocalDateTime.ofEpochSecond(record.getUserMetadata().getCreationTimestamp() / 1000, 0, ZoneOffset.UTC);
                    return creationTime.format(formatter);
                })
                .setHeader(getTranslation("user.creationDate"))
                .setAutoWidth(true)
                .setSortable(true);

        gridArticle.addColumn(record -> {
                    LocalDateTime lastSignInTime = LocalDateTime.ofEpochSecond(record.getUserMetadata().getLastSignInTimestamp() / 1000, 0, ZoneOffset.UTC);
                    return lastSignInTime.format(formatter);
                })
                .setHeader(getTranslation("user.lastLogin"))
                .setAutoWidth(true)
                .setSortable(true);
        gridArticle.addColumn(ExportedUserRecord::isEmailVerified).setHeader(getTranslation("user.verified")).setAutoWidth(true).setSortable(true);
        gridArticle.addColumn(ExportedUserRecord::isDisabled).setHeader(getTranslation("user.disabled")).setAutoWidth(true).setSortable(true);

        TextField searchField = new TextField();
        searchField.setWidth("30%");
        searchField.setPlaceholder(getTranslation("article.search"));
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        vlMain.add(hGrid, new Hr(), searchField);
        vlMain.add(gridArticle);


        add(vlMain);

        List<ExportedUserRecord> userList = (List<ExportedUserRecord>) getAllUsersFirebase();
        ListDataProvider<ExportedUserRecord> dataProvider = new ListDataProvider<>(userList);
        GridListDataView<ExportedUserRecord> dataView = gridArticle.setItems(dataProvider);

        searchField.addValueChangeListener(e -> dataView.refreshAll());

        /*dataView.addFilter(article -> {
            String searchTerm = searchField.getValue().trim();
            if (searchTerm.isEmpty()) return true;

            boolean matchesTitle = article.getTitle().toLowerCase().contains(searchTerm.toLowerCase());
            boolean matchesDescription = article.getDescription().toLowerCase().contains(searchTerm.toLowerCase());


            return matchesTitle || matchesDescription;
        });*/


    }

}