package es.uca.cm.admin.Firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
public class StorageService {
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

    public static String getImage(String url) throws IOException {
        // Obtener las credenciales de Firebase desde el archivo
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                new ClassPathResource("conexion-morada-firebase-adminsdk-yfw4e-42a173e4d3.json").getInputStream());

        // Configurar las opciones de almacenamiento de Firebase
        StorageOptions storageOptions = StorageOptions.newBuilder()
                .setCredentials(googleCredentials)
                .build();

        // Obtener una instancia del servicio de almacenamiento de Firebase
        Storage storage = storageOptions.getService();
        url = url.replace("%2F", "/");
        // Obtener el nombre del archivo de la URL
        String nombreArchivo = url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('?'));

        // Crear una referencia al archivo en Firebase Storage
        String carpeta = "portadaArticulos";
        String rutaArchivo = carpeta + "/" + nombreArchivo;
        //System.out.println(rutaArchivo);
        BlobId blobId = BlobId.of("conexion-morada.appspot.com", rutaArchivo);
        Blob blob = storage.get(blobId);
        if(blob == null) return "";
        // Crear una URL pública con token de autenticación
        long duracionToken = 3600; // Duración en segundos
        String urlConToken = blob.signUrl(duracionToken, TimeUnit.SECONDS)
                .toString();

        return urlConToken;
    }
}