package upp.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.CustomUser;
import upp.model.ScienceMagazine;
import upp.service.UserService;

@Service
public class SendingEmailExecutionListenerStart implements ExecutionListener{
	
	@Autowired
	UserService userService;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		ScienceMagazine magazine = (ScienceMagazine)execution.getVariable("chosenMagazine");
		CustomUser mainEditor = userService.findByUsername(magazine.getMainEditorId());
		execution.setVariable("mainEditor", mainEditor);
	}
}
