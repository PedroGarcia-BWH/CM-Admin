package es.uca.cm.admin.Firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {

    public static  Iterable<ExportedUserRecord> getAllUsersFirebase() throws IOException, FirebaseAuthException, FirebaseAuthException {
        // Obtener las credenciales de Firebase desde el archivo
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                new ClassPathResource("conexion-morada-firebase-adminsdk-yfw4e-42a173e4d3.json").getInputStream());


        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();
            FirebaseApp.initializeApp(firebaseOptions);
        }

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Obtener la lista de todos los usuarios
        ListUsersPage page = firebaseAuth.listUsers(null);
        Iterable<ExportedUserRecord> users = page.getValues();

        // Mostrar los datos de cada usuario
        /*for (UserRecord user : users) {
            System.out.println("User ID: " + user.getUid());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Phone number: " + user.getPhoneNumber());
            System.out.println("Display name: " + user.getDisplayName());
            System.out.println("Creation time: " + user.getUserMetadata().getCreationTimestamp());
            System.out.println("Last sign-in time: " + user.getUserMetadata().getLastSignInTimestamp());
            System.out.println("Is email verified: " + user.isEmailVerified());
            System.out.println("Is disabled: " + user.isDisabled());
            System.out.println("PhotoURL: " + user.getPhotoUrl());
        }*/

        return users;
    }

    public static boolean toggleUserStatus(String uid) throws IOException, FirebaseAuthException {
        // Obtener las credenciales de Firebase desde el archivo
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                new ClassPathResource("conexion-morada-firebase-adminsdk-yfw4e-42a173e4d3.json").getInputStream());


        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();
            FirebaseApp.initializeApp(firebaseOptions);
        }

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Obtener el usuario
        UserRecord userRecord = firebaseAuth.getUser(uid);

        // Cambiar el estado del usuario
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid)
                .setDisabled(!userRecord.isDisabled());
        UserRecord updatedUserRecord = firebaseAuth.updateUser(request);

        return updatedUserRecord.isDisabled();
    }
}
