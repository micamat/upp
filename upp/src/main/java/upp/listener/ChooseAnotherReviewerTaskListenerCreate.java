package upp.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.springframework.stereotype.Service;

import upp.model.ScienceMagazine;

@Service
public class ChooseAnotherReviewerTaskListenerCreate implements TaskListener{
	@Override
	public void notify(DelegateTask task) {
		ScienceMagazine magazine = (ScienceMagazine)task.getVariable("chosenMagazine");
		List<String> magazineReviewers = Arrays.asList(magazine.getReviewers().split(","));
		
		List<FormField> formFields = task.getExecution().getProcessEngineServices().getFormService().getTaskFormData(task.getId()).getFormFields();
		if(formFields != null) {
			for(FormField field : formFields) {
				if(field.getId().equals("anotherReviewer")) {
					HashMap<String, String> enumValues = (HashMap<String, String>)field.getType().getInformation("values");
					for(String magazineReviewer : magazineReviewers) {
							enumValues.put(magazineReviewer, magazineReviewer);
					}
				}
			}
		}
	}
}
