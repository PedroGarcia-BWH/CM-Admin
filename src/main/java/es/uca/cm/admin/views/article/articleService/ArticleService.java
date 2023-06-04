package es.uca.cm.admin.views.article.articleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArticleService {
    @Autowired
    private ArticleRespository articleRepository;

    public ArticleService() {}

    public ArticleService(ArticleRespository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findByCityIsNullAndComunidadIsNullAndEliminationDateIsNull() {
        return articleRepository.findByCityIsNullAndComunidadIsNullAndEliminationDateIsNull();
    }

    public List<Article> findByEliminationDateIsNull() {
        return articleRepository.findByEliminationDateIsNull();
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public Optional<Article> findById(UUID id) {
        return articleRepository.findById(id);
    }

    public boolean deleteById(UUID id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            article.get().setEliminationDate(new Date());
            articleRepository.save(article.get());
            return true;
        }
        return false;
    }

    public List<Article> findByCityAndAndEliminationDateIsNull(String city) {
        return articleRepository.findByCityAndAndEliminationDateIsNull(city);
    }

    public List<Article> findByComunidadAndAndEliminationDateIsNull(String comunidad) {
        return articleRepository.findByComunidadAndAndEliminationDateIsNull(comunidad);
    }

    public List<Article> findByTitleContainingOrDescriptionContainingOrBodyContainingAndComunidadIsNullAndCityIsNull(String query) {
        return articleRepository.findByTitleContainingAndCityIsNullAndComunidadIsNull(query);
    }

    public List<Article> findByTitleContainingOrDescriptionContainingOrBodyContainingAndComunidad(String query, String comunidad) {
        return articleRepository.findByTitleContainingAndComunidad(query, comunidad);
    }

    public List<Article> findByTitleContainingOrDescriptionContainingOrBodyContainingAndCity(String query, String city) {
        return articleRepository.findByTitleContainingAndCity(query, city);
    }

}
