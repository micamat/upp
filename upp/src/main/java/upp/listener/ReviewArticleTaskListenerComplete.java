package upp.listener;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.Article;
import upp.model.Comment;
import upp.service.CommentService;
import upp.service.UserService;

@Service
public class ReviewArticleTaskListenerComplete implements TaskListener{
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	UserService userService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		List<FormField> formFields = delegateTask.getExecution().getProcessEngineServices().getFormService().getTaskFormData(delegateTask.getId()).getFormFields();
		Comment comment = new Comment();
		if(formFields != null) {
			for(FormField field : formFields) {
				if(field.getId().equals("comment")) {
					comment.setText(field.getProperties().get(field.getId()));
				}
			}
		}
		comment.setUser(userService.findByUsername(delegateTask.getAssignee()));
		comment.setArticle((Article)delegateTask.getVariable("article"));
		commentService.save(comment);
	}
}
