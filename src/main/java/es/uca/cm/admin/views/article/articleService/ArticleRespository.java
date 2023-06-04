package es.uca.cm.admin.views.article.articleService;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleRespository extends JpaRepository<Article, UUID> {
    public List<Article> findByCityIsNullAndComunidadIsNullAndEliminationDateIsNull();

    public  List<Article> findByEliminationDateIsNull();

    public List<Article> findByCityAndAndEliminationDateIsNull(String city);

    public List<Article> findByComunidadAndAndEliminationDateIsNull(String comunidad);

    /*@Query(value = "SELECT * FROM article WHERE (title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR body LIKE '%' || :query || '%') AND comunidad = :comunidad", nativeQuery = true)
    List<Article> findByTitleContainingOrDescriptionContainingOrBodyContainingAndComunidad(String query, String comunidad);*/

    List<Article> findByTitleContainingAndComunidad(String query, String comunidad);

    List<Article> findByTitleContainingAndCity(String query, String city);

    List<Article> findByTitleContainingAndCityIsNullAndComunidadIsNull(String query);

    @Query(value = "SELECT * FROM article WHERE (title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR body LIKE '%' || :query || '%') AND city = :city", nativeQuery = true)
    List<Article> findByTitleContainingOrDescriptionContainingOrBodyContainingAndCity(String query, String city);

    @Query(value = "SELECT * FROM article WHERE (title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR body LIKE '%' || :query || '%') AND comunidad IS NULL AND city IS NULL", nativeQuery = true)
    List<Article> findByTitleContainingOrDescriptionContainingOrBodyContainingAndComunidadIsNullAndCityIsNull(String query);
}
