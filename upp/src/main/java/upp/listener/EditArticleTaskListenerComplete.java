package upp.listener;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.Article;
import upp.service.ArticleService;

@Service
public class EditArticleTaskListenerComplete implements TaskListener{
	
	@Autowired
	ArticleService articleService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		List<FormField> formFields = delegateTask.getExecution().getProcessEngineServices().getFormService().getTaskFormData(delegateTask.getId()).getFormFields();
		Article article = (Article)delegateTask.getVariable("article");
		if(formFields != null) {
			for(FormField field : formFields) {
				if(field.getId().equals("pdf")) {
					String text = field.getProperties().get(field.getId());
					article.setPdfText(text);
					break;
				}
			}
		}
		articleService.save(article);
	}
}
