package upp.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.CustomUser;
import upp.model.ScienceMagazine;
import upp.service.MagazineService;
import upp.service.UserService;

@Service
public class ChooseJournalTaskListener implements TaskListener {

	@Autowired
	UserService userService;
	
	@Autowired
	MagazineService magazineService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		List<ScienceMagazine> magazines = new ArrayList<ScienceMagazine>();
		CustomUser customUser = userService.findByUsername(delegateTask.getAssignee());
		delegateTask.setVariable("currentAuthor", customUser);
		List<String> userFields = Arrays.asList(customUser.getScienceFields().split(","));
		for (ScienceMagazine magazine : magazineService.findAll()) {
			for (String magazineField : Arrays.asList(magazine.getScienceFields().split(","))) {
				for (String userField : userFields) {
					if (!magazines.contains(magazine) && magazineField.equals(userField)) {
						magazines.add(magazine);
					}
				}
			}
		}

		List<FormField> formFields = delegateTask.getExecution().getProcessEngineServices().getFormService()
				.getTaskFormData(delegateTask.getId()).getFormFields();
		
		if (formFields != null) {
			for (FormField field : formFields) {
				if (field.getId().equals("journal")) {
					HashMap<String, String> enumValues = (HashMap<String, String>) field.getType()
							.getInformation("values");
					enumValues.clear();
					for (ScienceMagazine magazine : magazines) {
						enumValues.put(magazine.getId().toString(), magazine.getName());
					}
				}
			}
		}
	}
}
