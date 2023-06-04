package es.uca.cm.admin.views.article.articleService;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.UUID;

@Entity
public class Article {

    @jakarta.persistence.Id
    @GeneratedValue
    @Column(length=16)
    private UUID Id;

    @Column
    private String title;

    @Column
    private String description;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column
    private String urlFrontPage;

    @Column
    private Date creationDate;

    @Column
    private Date eliminationDate;

    @Column
    private String Category;

    @Column
    private String city;

    @Column
    private String comunidad;

    public Article(){}

    public Article(String title, String description, String body, String urlFrontPage) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.urlFrontPage = urlFrontPage;
        this.creationDate = new Date();
        city = null;
        comunidad = null;
    }

    public Article(String title, String description, String body, String urlFrontPage,String city, String comunidad) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.urlFrontPage = urlFrontPage;
        this.creationDate = new Date();
        this.city = city;
        this.comunidad = comunidad;
    }

    public Article(String title, String description, String body, String urlFrontPage,String category, String city, String comunidad) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.urlFrontPage = urlFrontPage;
        this.creationDate = new Date();
        this.city = city;
        this.comunidad = comunidad;
        this.Category = category;
    }


    //getters
    public UUID getId() {
        return Id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getBody() {
        return body;
    }
    public String getUrlFrontPage() {
        return urlFrontPage;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public Date getEliminationDate() {
        return eliminationDate;
    }

    public String getCategory() {
        return Category;
    }
    //setters
    public void setId(UUID id) {
        Id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public void setUrlFrontPage(String urlFrontPage) {
        this.urlFrontPage = urlFrontPage;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public void setEliminationDate(Date eliminationDate) {
        this.eliminationDate = eliminationDate;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getCity() {
        return city;
    }

    public String getComunidad() {
        return comunidad;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setComunidad(String comunidad) {
        this.comunidad = comunidad;
    }

    @Override
    public String toString() {
        return "Article{" +
                "Id=" + Id +
                ", title='" + title + '\'' +
                ", description='" + description ;
    }
}
