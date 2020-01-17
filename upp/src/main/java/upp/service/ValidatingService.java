package upp.service;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;

import upp.model.CustomUser;

public class ValidatingService implements JavaDelegate {

	@Autowired
	UserService userService;

	@Autowired
	IdentityService identityService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		CustomUser user = (CustomUser) execution.getVariable("customUser");
		CustomUser validatedUser = null;
		String mailPattern = ".*@[a-z]*\\.com";
		if (user.getFirstName() == null) {
			if (validatedUser == null) {
				validatedUser = user;
			}
			validatedUser.setFirstName("First name cannot be empty.");
		}
		if (user.getLastName() == null) {
			if (validatedUser == null) {
				validatedUser = user;
			}
			validatedUser.setLastName("Last name cannot be empty.");
		}
		if (user.getDesignation() == null) {
			if (validatedUser == null) {
				validatedUser = user;
			}
			validatedUser.setDesignation("Designation cannot be empty.");
		}
		if (user.getCity() == null) {
			if (validatedUser == null) {
				validatedUser = user;
			}
			validatedUser.setCity("City cannot be empty.");
		}
		if (user.getCountry() == null) {
			if (validatedUser == null) {
				validatedUser = user;
			}
			validatedUser.setCountry("Country cannot be empty.");
		}
		if (user.getEmail() == null) {
			if (validatedUser == null) {
				validatedUser = user;
			}
			validatedUser.setEmail("Email cannot be empty.");
		}
		if (user.getUsername() == null) {
			if (validatedUser == null) {
				validatedUser = user;
			}
			validatedUser.setUsername("Username cannot be empty.");
		}
		if (user.getPassword() == null) {
			if (validatedUser == null) {
				validatedUser = user;
			}
			validatedUser.setPassword("Password cannot be empty.");
		}
		if (user.getConfirmPassword() != null) {
			User camUser = execution.getProcessEngine().getIdentityService().createUserQuery().userId(user.getUsername()).singleResult();
			Boolean checked = execution.getProcessEngine().getIdentityService().checkPassword(user.getUsername(), user.getConfirmPassword());
			if (!checked) {
				if (validatedUser == null) {
					validatedUser = user;
				}
				validatedUser.setConfirmPassword("Password confirmation failed.");
			}
		}
		if (user.getEmail() != null && !user.getEmail().matches(mailPattern)) {
			if (validatedUser == null) {
				validatedUser = user;
			}
			validatedUser.setEmail("Email address is not valid.");
		}
		execution.setVariable("validatedUser", validatedUser);
	}
}
