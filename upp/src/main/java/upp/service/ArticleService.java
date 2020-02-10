package upp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.Article;
import upp.repository.ArticleRepository;

@Service
public class ArticleService {
	
	@Autowired
	ArticleRepository repo;
	
	public void save(Article article) {
		repo.save(article);
	}
}
