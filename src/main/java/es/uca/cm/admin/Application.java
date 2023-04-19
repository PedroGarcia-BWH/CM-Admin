package es.uca.cm.admin;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import es.uca.cm.admin.webUser.WebUser;
import es.uca.cm.admin.webUser.WebUserRepository;
import es.uca.cm.admin.webUser.WebUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

}
