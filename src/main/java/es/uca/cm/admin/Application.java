package es.uca.cm.admin;

import com.google.firebase.auth.*;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

import es.uca.cm.admin.components.emailService.EmailService;
import es.uca.cm.admin.webUser.WebUser;

import es.uca.cm.admin.webUser.WebUserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;


import static es.uca.cm.admin.Firebase.AuthService.getAllUsersFirebase;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "conexnmoradaadministrador")
@PWA(name = "Conexión Morada Administrador", shortName = "Conexión Morada Administrador", offlineResources = {"images/logo.png"})

public class Application implements AppShellConfigurator, CommandLineRunner {

    @Autowired
    private WebUserService webUserRepository;

    @Autowired
    private EmailService emailService;

    public Application() throws IOException, FirebaseAuthException {
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }



    @Override
    public void run(String... args) throws Exception {

        if(webUserRepository.count() == 0) {
            WebUser empleado = new WebUser("admin@uca.es", "admin");
            webUserRepository.save(empleado);
        }
        //emailService.sendEmail("pedro.garciaromera@alum.uca.es", "Conexión Morada", "Bienvenido a Conexión Morada");

    }
    //Iterable<ExportedUserRecord> record = getAllUsersFirebase();

}
