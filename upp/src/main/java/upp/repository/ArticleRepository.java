package upp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import upp.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long>{
	
}
