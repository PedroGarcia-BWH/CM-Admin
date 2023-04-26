package es.uca.cm.admin;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
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

    @Service
    public static class FirebaseStorageService {
        public String uploadImage(InputStream inputStream, String nombreArchivo) throws IOException {
            // Obtener las credenciales de Firebase desde el archivo
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                    new ClassPathResource("conexion-morada-firebase-adminsdk-yfw4e-42a173e4d3.json").getInputStream());

            // Configurar las opciones de almacenamiento de Firebase
            StorageOptions storageOptions = StorageOptions.newBuilder()
                    .setCredentials(googleCredentials)
                    .build();

            // Obtener una instancia del servicio de almacenamiento de Firebase
            Storage storage = storageOptions.getService();

            // Crear un nuevo blob con el nombre de archivo y la carpeta deseada
            String carpeta = "portadaArticulos"; // Nombre de la carpeta
            String rutaArchivo = carpeta + "/" + nombreArchivo; // Nombre del archivo incluyendo la carpeta
            BlobId blobId = BlobId.of("conexion-morada.appspot.com", rutaArchivo); // Nombre del bucket predeterminado
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("image/jpeg") // Cambiar "image/jpeg" a "image/png" si es PNG
                    .build();

            // Guardar el archivo en Firebase Storage
            Blob blob = storage.create(blobInfo, inputStream);

            // Obtener la URL pública de la imagen guardada
            String url = blob.getMediaLink();

            return url;
        }
    }


    @Override
    public void run(String... args) throws Exception {

        if(webUserRepository.count() == 0) {
            WebUser empleado = new WebUser("pedro.garciaromera@alum.uca.es", webUserRepository.Cifrar("pruebauca"));
            webUserRepository.save(empleado);
        }
        DALLEController dalleController = new DALLEController();
        dalleController.DALLE("prueba");
    }

}
