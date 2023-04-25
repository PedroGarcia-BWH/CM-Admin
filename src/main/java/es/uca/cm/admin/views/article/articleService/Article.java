package es.uca.cm.admin.views.article.articleService;


import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
public class Article {

    @Id
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

    public Article(){}

    public Article(String title, String description, String body, String urlFrontPage, String category) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.urlFrontPage = urlFrontPage;
        this.creationDate = new Date();
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

    @Override
    public String toString() {
        return "Article{" +
                "Id=" + Id +
                ", title='" + title + '\'' +
                ", description='" + description ;
    }
}
