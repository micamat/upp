package upp.listener;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.springframework.stereotype.Service;

@Service
public class ChooseAnotherReviewerTaskListenerComplete implements TaskListener{
	@Override
	public void notify(DelegateTask delegateTask) {
		List<FormField> formFields = delegateTask.getExecution().getProcessEngineServices().getFormService()
				.getTaskFormData(delegateTask.getId()).getFormFields();
		List<String> reviewers = new ArrayList<String>();
		if(formFields != null) {
			for(FormField field : formFields) {
				if(field.getId().equals("anotherReviewer")) {
					String reviewer = field.getProperties().get(field.getId());
					reviewers.add(reviewer);
				}
			}
		}
		delegateTask.setVariable("reviewers", reviewers);
	}
}
