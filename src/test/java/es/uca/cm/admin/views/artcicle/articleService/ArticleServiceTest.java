package es.uca.cm.admin.views.artcicle.articleService;

import es.uca.cm.admin.views.article.articleService.Article;
import es.uca.cm.admin.views.article.articleService.ArticleRespository;
import es.uca.cm.admin.views.article.articleService.ArticleService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static com.helger.commons.mock.CommonsAssert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ArticleServiceTest {
    @Mock
    private ArticleRespository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    public ArticleServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByCityIsNullAndComunidadIsNullAndEliminationDateIsNull() {
        // Mocking the article list
        List<Article> articles = new ArrayList<>();
        articles.add(new Article());
        articles.add(new Article());
        when(articleRepository.findByCityIsNullAndComunidadIsNullAndEliminationDateIsNull()).thenReturn(articles);

        // Call the service method
        List<Article> result = articleService.findByCityIsNullAndComunidadIsNullAndEliminationDateIsNull();

        // Verify the result
        assertEquals(2, result.size());
        verify(articleRepository, times(1)).findByCityIsNullAndComunidadIsNullAndEliminationDateIsNull();
    }

    @Test
    public void testFindByEliminationDateIsNull() {
        // Mocking the article list
        List<Article> articles = new ArrayList<>();
        articles.add(new Article());
        articles.add(new Article());
        when(articleRepository.findByEliminationDateIsNull()).thenReturn(articles);

        // Call the service method
        List<Article> result = articleService.findByEliminationDateIsNull();

        // Verify the result
        assertEquals(2, result.size());
        verify(articleRepository, times(1)).findByEliminationDateIsNull();
    }

    @Test
    public void testSave() {
        // Create a mock article
        Article article = new Article();

        // Mock the save method
        when(articleRepository.save(article)).thenReturn(article);

        // Call the service method
        Article result = articleService.save(article);

        // Verify the result
        assertEquals(article, result);
        verify(articleRepository, times(1)).save(article);
    }

    @Test
    public void testFindById() {
        // Create a mock article
        Article article = new Article();
        UUID id = UUID.randomUUID();
        article.setId(id);

        // Mock the findById method
        when(articleRepository.findById(id)).thenReturn(Optional.of(article));

        // Call the service method
        Optional<Article> result = articleService.findById(id);

        // Verify the result
        assertTrue(result.isPresent());
        assertEquals(article, result.get());
        verify(articleRepository, times(1)).findById(id);
    }

    @Test
    public void testDeleteById() {
        // Create a mock article
        Article article = new Article();
        UUID id = UUID.randomUUID();
        article.setId(id);

        // Mock the findById method
        when(articleRepository.findById(id)).thenReturn(Optional.of(article));

        // Call the service method
        boolean result = articleService.deleteById(id);

        // Verify the result
        assertTrue(result);
        assertEquals(new Date(), article.getEliminationDate());
        verify(articleRepository, times(1)).save(article);
    }

    @Test
    public void testDeleteById_WhenArticleNotFound() {
        // Create a mock article
        UUID id = UUID.randomUUID();

        // Mock the findById method
        when(articleRepository.findById(id)).thenReturn(Optional.empty());

        // Call the service method
        boolean result = articleService.deleteById(id);

        // Verify the result
        assertFalse(result);
        verify(articleRepository, never()).save(any(Article.class));
    }
}