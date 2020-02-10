package upp.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.CustomUser;
import upp.service.UserService;

@Service
public class PayFeeTaskListenerComplete implements TaskListener{
	
	@Autowired
	UserService userService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		CustomUser customUser = userService.findByUsername(delegateTask.getAssignee());
		customUser.setHasActiveFee(true);
		userService.save(customUser);
	}
}
