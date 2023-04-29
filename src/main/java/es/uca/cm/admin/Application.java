package es.uca.cm.admin;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.*;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import es.uca.cm.admin.openAi.DALL.DALLEController;
import es.uca.cm.admin.openAi.GPT.GPTController;
import es.uca.cm.admin.webUser.WebUser;
import es.uca.cm.admin.webUser.WebUserRepository;
import es.uca.cm.admin.webUser.WebUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
public class Application implements AppShellConfigurator, CommandLineRunner {

    @Autowired
    private WebUserService webUserRepository;

    public Application() throws IOException, FirebaseAuthException {
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }



    @Override
    public void run(String... args) throws Exception {

        if(webUserRepository.count() == 0) {
            WebUser empleado = new WebUser("pedro.garciaromera@alum.uca.es", webUserRepository.Cifrar("pruebauca"));
            webUserRepository.save(empleado);
        }
    }
    Iterable<ExportedUserRecord> record = getAllUsersFirebase();
}
