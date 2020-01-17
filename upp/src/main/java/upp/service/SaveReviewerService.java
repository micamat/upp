package upp.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.CustomUser;

@Service
public class SaveReviewerService implements JavaDelegate{
	
	@Autowired
	UserService userService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		CustomUser user = (CustomUser)execution.getVariable("approvedUser");
		user.setRole("reviewer");
		User camUser = execution.getProcessEngine().getIdentityService().createUserQuery().userId(user.getUsername()).singleResult();
		execution.getProcessEngine().getIdentityService().deleteMembership(camUser.getId(), "guest");
		execution.getProcessEngine().getIdentityService().createMembership(camUser.getId(), "reviewer");
		userService.save(user);
	}
}
