package upp.listener;

import static org.camunda.bpm.engine.authorization.Authorization.ANY;
import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;
import static org.camunda.bpm.engine.authorization.Groups.CAMUNDA_ADMIN;
import static org.camunda.bpm.engine.authorization.Permissions.ALL;

import java.util.List;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.CustomUser;
import upp.repository.UserRepository;

@Service
public class ProcessExecutionListener implements ExecutionListener {

	@Autowired
	IdentityService identityService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	AuthorizationService authService;
	
	@Autowired
	UserRepository userRepo;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		execution.getProcessEngineServices().getIdentityService().setAuthenticatedUserId("test");
		List<User> users = identityService.createUserQuery().userIdIn("pera").list();
		List<User> editors = identityService.createUserQuery().userIdIn("mika", "laza").list();
		CustomUser cu = new CustomUser();
		execution.setVariable("validatedUser", cu);
		
		if(identityService.createGroupQuery().groupId("author").singleResult() == null) {
			Group group = identityService.newGroup("author");
			identityService.saveGroup(group);
		}
		if(identityService.createGroupQuery().groupId("reviewer").singleResult() == null) {
			Group group = identityService.newGroup("reviewer");
			identityService.saveGroup(group);
		}
		if(identityService.createGroupQuery().groupId("editor").singleResult() == null) {
			Group group = identityService.newGroup("editor");
			identityService.saveGroup(group);
		}
		
		if (users.isEmpty()) {
			User admin = identityService.newUser("pera");
			admin.setFirstName("Pera");
			admin.setLastName("Peric");
			admin.setEmail("milicainformatika@gmail.com");
			admin.setPassword("password");

			identityService.saveUser(admin);
			
			CustomUser user = new CustomUser();
			user.setFirstName(admin.getFirstName());
			user.setLastName(admin.getLastName());
			user.setDesignation("Administrator");
			user.setCity("Novi Sad");
			user.setCountry("Serbia");
			user.setEmail(admin.getEmail());
			user.setUsername(admin.getId());
			user.setPassword(admin.getPassword());
			user.setIsReviewer(false);
			
			userRepo.save(user);

			for (Resource resource : Resources.values()) {
				if (authService.createAuthorizationQuery().groupIdIn(CAMUNDA_ADMIN).resourceType(resource)
						.resourceId(ANY).count() == 0) {
					AuthorizationEntity userAdminAuth = new AuthorizationEntity(AUTH_TYPE_GRANT);
					userAdminAuth.setGroupId(CAMUNDA_ADMIN);
					userAdminAuth.setResource(resource);
					userAdminAuth.setResourceId(ANY);
					userAdminAuth.addPermission(ALL);
					authService.saveAuthorization(userAdminAuth);
				}
			}

			identityService.createMembership(admin.getId(), CAMUNDA_ADMIN);
		}
		if(editors.isEmpty()) {
			User editor1 = identityService.newUser("mika");
			editor1.setFirstName("Mika");
			editor1.setLastName("Mikic");
			editor1.setEmail("milicainformatika@gmail.com");
			editor1.setPassword("password");
			
			identityService.saveUser(editor1);
			
			CustomUser cEditor1 = new CustomUser();
			cEditor1.setFirstName(editor1.getFirstName());
			cEditor1.setLastName(editor1.getLastName());
			cEditor1.setDesignation("Editor");
			cEditor1.setCity("Beograd");
			cEditor1.setCountry("Serbia");
			cEditor1.setEmail(editor1.getEmail());
			cEditor1.setUsername(editor1.getId());
			cEditor1.setPassword(editor1.getPassword());
			cEditor1.setIsReviewer(false);
			cEditor1.setRole("editor");
			cEditor1.setScienceFields("Mathematics,Physics");
			
			userRepo.save(cEditor1);
			
			for (Resource resource : Resources.values()) {
				if (authService.createAuthorizationQuery().groupIdIn("editor").resourceType(resource)
						.resourceId(ANY).count() == 0) {
					AuthorizationEntity userEditorAuth = new AuthorizationEntity(AUTH_TYPE_GRANT);
					userEditorAuth.setGroupId("editor");
					userEditorAuth.setResource(resource);
					userEditorAuth.setResourceId(ANY);
					userEditorAuth.addPermission(ALL);
					authService.saveAuthorization(userEditorAuth);
				}
			}
			
			identityService.createMembership(editor1.getId(), "editor");
			
			User editor2 = identityService.newUser("laza");
			editor2.setFirstName("Laza");
			editor2.setLastName("Lazic");
			editor2.setEmail("milicainformatika@gmail.com");
			editor2.setPassword("password");
			
			identityService.saveUser(editor2);
			
			CustomUser cEditor2 = new CustomUser();
			cEditor2.setFirstName(editor2.getFirstName());
			cEditor2.setLastName(editor2.getLastName());
			cEditor2.setDesignation("Editor");
			cEditor2.setCity("Nis");
			cEditor2.setCountry("Serbia");
			cEditor2.setEmail(editor2.getEmail());
			cEditor2.setUsername(editor2.getId());
			cEditor2.setPassword(editor2.getPassword());
			cEditor2.setIsReviewer(false);
			cEditor2.setRole("editor");
			cEditor2.setScienceFields("Computer science");
			
			userRepo.save(cEditor2);
			
			identityService.createMembership(editor2.getId(), "editor");
			
			User editor3 = identityService.newUser("vaso");
			editor3.setFirstName("Vaso");
			editor3.setLastName("Vasic");
			editor3.setEmail("milicainformatika@gmail.com");
			editor3.setPassword("password");
			
			identityService.saveUser(editor3);
			
			CustomUser cEditor3 = new CustomUser();
			cEditor3.setFirstName(editor3.getFirstName());
			cEditor3.setLastName(editor3.getLastName());
			cEditor3.setDesignation("Editor");
			cEditor3.setCity("Zrenjanin");
			cEditor3.setCountry("Serbia");
			cEditor3.setEmail(editor3.getEmail());
			cEditor3.setUsername(editor3.getId());
			cEditor3.setPassword(editor3.getPassword());
			cEditor3.setIsReviewer(false);
			cEditor3.setRole("editor");
			cEditor3.setScienceFields("Mathematics");
			
			userRepo.save(cEditor3);
			
			identityService.createMembership(editor3.getId(), "editor");
			
			User editor4 = identityService.newUser("mara");
			editor4.setFirstName("Mara");
			editor4.setLastName("Maric");
			editor4.setEmail("milicainformatika@gmail.com");
			editor4.setPassword("password");
			
			identityService.saveUser(editor4);
			
			CustomUser cEditor4 = new CustomUser();
			cEditor4.setFirstName(editor4.getFirstName());
			cEditor4.setLastName(editor4.getLastName());
			cEditor4.setDesignation("Editor");
			cEditor4.setCity("Niska banja");
			cEditor4.setCountry("Serbia");
			cEditor4.setEmail(editor4.getEmail());
			cEditor4.setUsername(editor4.getId());
			cEditor4.setPassword(editor4.getPassword());
			cEditor4.setIsReviewer(false);
			cEditor4.setRole("editor");
			cEditor4.setScienceFields("Physics");
			
			userRepo.save(cEditor4);
			
			identityService.createMembership(editor4.getId(), "editor");
		}
	}
}
