package upp.listener;

import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.Article;
import upp.model.CustomUser;
import upp.model.ScienceMagazine;
import upp.service.ArticleService;
import upp.service.MagazineService;
import upp.service.UserService;

@Service
public class AddArticleAttributesTaskListenerComplete implements TaskListener{
	
	@Autowired
	ArticleService articleService;
	
	@Autowired
	MagazineService magazineService;
	
	@Autowired
	UserService userService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		
		Article article = new Article();
		
		List<FormField> formFields = delegateTask.getExecution().getProcessEngineServices().getFormService().getTaskFormData(delegateTask.getId()).getFormFields();
		if(formFields != null) {
			for(FormField field : formFields) {
				if(field.getId().equals("title")) {
					article.setTitle(field.getProperties().get(field.getId()));
				}
				if(field.getId().equals("coAuthors")) {
					article.setCoAuthors(field.getProperties().get(field.getId()));
				}
				if(field.getId().equals("keyWords")) {
					article.setKeyWords(field.getProperties().get(field.getId()));
				}
				if(field.getId().equals("abstract")) {
					article.setArticleAbstract(field.getProperties().get(field.getId()));
				}
				if(field.getId().equals("scienceField")) {
					article.setScienceField(field.getProperties().get(field.getId()));
				}
				if(field.getId().equals("pdf")) {
					article.setPdfText(field.getProperties().get(field.getId()));
				}
			}
		}
		article.setMagazine((ScienceMagazine)delegateTask.getVariable("chosenMagazine"));
		CustomUser author = userService.findByUsername(delegateTask.getAssignee());
		article.setAuthor(author);
		articleService.save(article);
		delegateTask.setVariable("article", article);
		delegateTask.setVariable("author", author);
	}
}
