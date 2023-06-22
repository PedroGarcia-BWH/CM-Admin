package es.uca.cm.admin.webUser;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

@Entity
public class WebUser {
    @Id
    @GeneratedValue
    @Column(length=16)
    private UUID Id;

    @NotNull
    @Column(unique=true)
    private String email;

    @NotNull
    @Column(name = "password")
    private String pass;

    @Column
    private Date dateDeleted;

    public WebUser() {

    }

    public Date getDateDeleted(){ return this.dateDeleted; }
    public void  setDateDeleted(Date date){ this.dateDeleted = date; }
    @NotNull
    @Column
    private Date dateCreated;

    @Version
    private Integer version;

    public WebUser(String email, String pass) {
        this.email = email;
        this.pass = pass;
        this.dateCreated = new Date();
    }

    public void setId(UUID id) {
        Id = id;
    }
    public Integer getVersion(){ return this.version; }
    public void setVersion(Integer version){ this.version = version; }
    public Date getDateCreated(){ return this.dateCreated; }
    public void  setDateCreated(Date date){ this.dateCreated = date; }
    public String getPass(){ return this.pass; }
    public void setPass (String pass){ this.pass = pass; }
    public String getEmail(){ return this.email; }
    public void setEmail (String email){ this.email = email; }

    public UUID getId(){ return this.Id; }
}
