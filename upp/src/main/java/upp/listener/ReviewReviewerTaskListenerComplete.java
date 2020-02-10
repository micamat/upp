package upp.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.Article;
import upp.model.Comment;
import upp.model.CustomUser;
import upp.service.ArticleService;
import upp.service.CommentService;
import upp.service.UserService;

@Service
public class ReviewReviewerTaskListenerComplete implements TaskListener {
	
	@Autowired
	ArticleService articleService;
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	UserService userService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		Article article = (Article)delegateTask.getVariable("article");
		List<String> reviewers = new ArrayList<String>();
		if(article.getReviewers() != null) {
			reviewers = Arrays.asList(article.getReviewers().split(","));
		}
		String current = delegateTask.getAssignee();
		boolean isHere = false;
		for(String r : reviewers) {
			if(r.equals(current)) {
				isHere = true;
				break;
			}
		}
		if(!isHere) {
			if(article.getReviewers() == null) {
				article.setReviewers(current);
			}
			else {
				article.setReviewers(article.getReviewers() + "," + current);
			}
		}
		articleService.save(article);
		
		CustomUser user = userService.findByUsername(delegateTask.getAssignee());
		Comment authorComment = new Comment();
		Comment editorComment = new Comment();
		String authorCommentText = "";
		String editorCommentText = "";
		
		List<FormField> formFields = delegateTask.getExecution().getProcessEngineServices().getFormService().getTaskFormData(delegateTask.getId()).getFormFields();
		if(formFields != null) {
			for(FormField field : formFields) {
				if(field.getId().equals("authorComment")) {
					authorCommentText = field.getProperties().get(field.getId());
				}
				else {
					editorCommentText = field.getProperties().get(field.getId());
				}
			}
		}
		authorComment.setText(authorCommentText);
		authorComment.setArticle(article);
		authorComment.setUser(user);
		authorComment.setVisibleToAuthor(true);
		
		editorComment.setArticle(article);
		editorComment.setText(editorCommentText);
		editorComment.setUser(user);
		editorComment.setVisibleToAuthor(false);
		
		commentService.save(authorComment);
		commentService.save(editorComment);
	}
}
