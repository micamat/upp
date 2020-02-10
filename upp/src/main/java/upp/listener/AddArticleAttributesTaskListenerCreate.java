package upp.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.ScienceMagazine;
import upp.service.UserService;

@Service
public class AddArticleAttributesTaskListenerCreate implements TaskListener{
	
	@Autowired
	UserService userService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		List<String> scienceFields = Arrays.asList(((ScienceMagazine)delegateTask.getVariable("chosenMagazine")).getScienceFields().split(","));
		List<FormField> formFields = delegateTask.getExecution().getProcessEngineServices().getFormService()
				.getTaskFormData(delegateTask.getId()).getFormFields();
		if(formFields != null) {
			for(FormField formField : formFields) {
				if(formField.getId().equals("scienceField")) {
					HashMap<String, String> enumValues = (HashMap<String, String>) formField.getType()
							.getInformation("values");
					enumValues.clear();
					for(String field : scienceFields) {
						enumValues.put(field, field);
					}
				}
			}
		}
	}
}
