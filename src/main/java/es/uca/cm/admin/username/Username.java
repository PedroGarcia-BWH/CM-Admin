package es.uca.cm.admin.username;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;


@Entity
public class Username {
    @jakarta.persistence.Id
    @GeneratedValue
    @Column(length=16)
    private UUID Id;

    @Column(unique=true)
    @NotNull
    private String username;

    @Column(unique=true)
    @NotNull
    private String firebaseId;

    @Column(unique=true)
    @NotNull
    private String firebaseToken;

    public Username() {}

    public Username(String username, String firebaseId, String firebaseToken) {
        this.username = username;
        this.firebaseId = firebaseId;
        this.firebaseToken = firebaseToken;
    }

    public Username(String username) {
        this.username = username;
    }

    public UUID getId() {
        return Id;
    }
    public void setId(UUID id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
