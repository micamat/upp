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
import upp.model.CustomUser;
import upp.service.ArticleService;

@Service
public class ChooseReviewerTaskListenerComplete implements TaskListener{
	@Override
	public void notify(DelegateTask task) {
		List<FormField> formFields = task.getExecution().getProcessEngineServices().getFormService().getTaskFormData(task.getId()).getFormFields();
		List<String> reviewers = (ArrayList<String>)task.getVariable("reviewers");
		if(reviewers == null) {
			reviewers = new ArrayList<String>();
		}
		if(formFields != null) {
			for(FormField field : formFields) {
				if(field.getId().equals("reviewer")) {
					String reviewer = field.getProperties().get(field.getId());
					reviewers.add(reviewer);
				}
			}
		}else {
			reviewers.add(((CustomUser)task.getVariable("mainEditor")).getUsername());
		}
		task.setVariable("reviewers", reviewers);
	}
}
