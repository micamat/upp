package upp.listener;

import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.ScienceMagazine;
import upp.service.MagazineService;

@Service
public class ChooseJournalTaskListenerComplete implements TaskListener {
	
	@Autowired
	MagazineService magazineService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		List<FormField> formFields = delegateTask.getExecution().getProcessEngineServices().getFormService()
				.getTaskFormData(delegateTask.getId()).getFormFields();
		if(formFields != null) {
			for(FormField formField : formFields) {
				if(formField.getId().equals("journal")) {
					//HashMap<String, String> enumValues = (HashMap<String, String>) formField.getType()
							//.getInformation("values");
					if(formField.getValue() != null) {
						String name = formField.getProperties().get(formField.getId());
						ScienceMagazine magazine = magazineService.findByName(name);
						delegateTask.getExecution().setVariable("chosenMagazine", magazine);
					}
				}
			}
		}
	}
}
