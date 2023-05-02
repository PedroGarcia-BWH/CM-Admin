package es.uca.cm.admin.views.user;

import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuthException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
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
import static es.uca.cm.admin.Firebase.AuthService.toggleUserStatus;


@PermitAll
@PageTitle("Gestíon de usuarios")
@Route(value = "usuarios", layout = MainLayout.class)
public class UsersView extends HorizontalLayout {
    private VerticalLayout vlGrid = new VerticalLayout();

    private H1 hGrid = new H1(getTranslation("user.gestion"));

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
        gridArticle.addColumn(userRecord -> userRecord.isEmailVerified() ? "X" : "✔")
                .setHeader(getTranslation("user.verified"))
                .setAutoWidth(true)
                .setSortable(false);
        gridArticle.addColumn(userRecord -> userRecord.isDisabled() ? "X" : "✔")
                .setHeader(getTranslation("user.disabled"))
                .setAutoWidth(true)
                .setSortable(false);
        gridArticle.addComponentColumn(userRecord -> {
                    if (userRecord.isDisabled()) {
                        Button enableButton = new Button("Habilitar");
                        enableButton.addClickListener(e -> {
                            try {
                                toggleUserStatus(userRecord.getUid());
                                refreshUI();
                                Notification.show("El usuario ha sido habilitado correctamente",
                                        3000, Notification.Position.BOTTOM_CENTER);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            } catch (FirebaseAuthException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                        return enableButton;
                    } else {
                        Button disableButton = new Button("Deshabilitar");
                        disableButton.addClickListener(e -> {
                            try {
                                toggleUserStatus(userRecord.getUid());
                                refreshUI();
                                Notification.show("El usuario ha sido desactivado correctamente",
                                        3000, Notification.Position.BOTTOM_CENTER);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            } catch (FirebaseAuthException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                        return disableButton;
                    }
                })
                .setHeader("Acción")
                .setAutoWidth(true)
                .setSortable(false);

        TextField searchField = new TextField();
        searchField.setWidth("30%");
        searchField.setPlaceholder(getTranslation("user.search"));
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        vlMain.add(hGrid, new Hr(), searchField);
        vlMain.add(gridArticle);


        add(vlMain);

        List<ExportedUserRecord> userList = (List<ExportedUserRecord>) getAllUsersFirebase();
        ListDataProvider<ExportedUserRecord> dataProvider = new ListDataProvider<>(userList);
        GridListDataView<ExportedUserRecord> dataView = gridArticle.setItems(dataProvider);

        searchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(user -> {
            String searchTerm = searchField.getValue().trim();
            if (searchTerm.isEmpty()) return true;

            boolean matchesEmail = user.getEmail().toLowerCase().contains(searchTerm.toLowerCase());
            boolean matchesId= user.getUid().toLowerCase().contains(searchTerm.toLowerCase());
            //boolean matchesName = user.getDisplayName().toLowerCase().contains(searchTerm.toLowerCase());


            return matchesEmail || matchesId;
        });
    }

    private void refreshUI() throws IOException, FirebaseAuthException {
        List<ExportedUserRecord> userList = (List<ExportedUserRecord>) getAllUsersFirebase();
        ListDataProvider<ExportedUserRecord> dataProvider = new ListDataProvider<>(userList);
        GridListDataView<ExportedUserRecord> dataView = gridArticle.setItems(dataProvider);
    }

}