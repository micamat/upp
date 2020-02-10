package upp.listener;

import java.util.Arrays;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.Article;
import upp.model.CustomUser;
import upp.service.ArticleService;
import upp.service.UserService;

@Service
public class ChoosingEditorExecutionListener implements ExecutionListener{
	
	@Autowired
	UserService userService;
	
	@Autowired
	ArticleService articleService;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		Article article = (Article)execution.getVariable("article");
		String primaryField = article.getScienceField();
		List<String> editors = Arrays.asList(article.getMagazine().getEditors().split(","));
		for(String username : editors) {
			CustomUser user = userService.findByUsername(username);
			if(!username.equals(article.getMagazine().getMainEditorId())) {
				for(String field : Arrays.asList(user.getScienceFields().split(","))) {
					if(primaryField.equals(field)) {
						execution.setVariable("editor", user);
						article.setEditor((CustomUser)execution.getVariable("editor"));
						articleService.save(article);
						return;
					}
				}
			}
		}
		execution.setVariable("editor", userService.findByUsername(article.getMagazine().getMainEditorId()));
		article.setEditor((CustomUser)execution.getVariable("editor"));
		articleService.save(article);
	}
}
