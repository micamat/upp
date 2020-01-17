package upp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.camunda.bpm.engine.rest.dto.identity.UserCredentialsDto;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import upp.dto.FormDataDto;
import upp.dto.FormFieldDataDto;
import upp.dto.TaskDto;
import upp.model.CustomUser;
import upp.model.ScienceMagazine;
import upp.service.UserService;

@RestController
@RequestMapping("/rest-api")
public class UserController {

	private static final Gson gson = new Gson();

	@Autowired
	TaskService taskService;

	@Autowired
	FormService formService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	IdentityService identityService;

	@Autowired
	UserService userService;

	@RequestMapping(method = RequestMethod.GET, value = "/form-data", produces = "application/json")
	public ResponseEntity<FormDataDto> getFormData() {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_registration");
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);
		TaskFormData formData = formService.getTaskFormData(task.getId());
		List<FormField> formFields = formData.getFormFields();
		return new ResponseEntity<FormDataDto>(new FormDataDto(task.getId(), formFields, processInstance.getId()),
				HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/apply/{taskId}/{processInstanceId}")
	public ResponseEntity<CustomUser> apply(@RequestBody List<FormFieldDataDto> formFieldData, @PathVariable String taskId,
			@PathVariable String processInstanceId) {
		User camundaUser = identityService.newUser("1");
		CustomUser customUser = new CustomUser();
		customUser.setUsername(camundaUser.getId());

		for (FormFieldDataDto field : formFieldData) {
			if (field.getFieldId().equals("firstName")) {
				customUser.setFirstName(field.getFieldValue());
				camundaUser.setFirstName(field.getFieldValue());
			}
			if (field.getFieldId().equals("lastName")) {
				customUser.setLastName(field.getFieldValue());
				camundaUser.setLastName(field.getFieldValue());
			}
			if (field.getFieldId().equals("designation")) {
				customUser.setDesignation(field.getFieldValue());
			}
			if (field.getFieldId().equals("city")) {
				customUser.setCity(field.getFieldValue());
			}
			if (field.getFieldId().equals("country")) {
				customUser.setCountry(field.getFieldValue());
			}
			if (field.getFieldId().equals("email")) {
				customUser.setEmail(field.getFieldValue());
				camundaUser.setEmail(field.getFieldValue());
			}
			if (field.getFieldId().equals("username")) {
				if(field.getFieldValue() != null) {
					customUser.setUsername(field.getFieldValue());
					camundaUser.setId(field.getFieldValue());
				}
			}
			if (field.getFieldId().equals("password")) {
				camundaUser.setPassword(field.getFieldValue());
			}
			if(field.getFieldId().equals("confirmPassword")) {
				customUser.setConfirmPassword(field.getFieldValue());
			}
			if (field.getFieldId().equals("scienceFields")) {
				customUser.setScienceFields(field.getFieldValue());
			}
			if (field.getFieldId().equals("isReviewer")) {
				customUser.setIsReviewer(Boolean.parseBoolean(field.getFieldValue()));
			}
		}

		identityService.saveUser(camundaUser);

		customUser.setRole("guest");
		customUser.setPassword(camundaUser.getPassword());
		userService.save(customUser);

		if (identityService.createGroupQuery().groupId("guest").singleResult() == null) {
			identityService.saveGroup(identityService.newGroup("guest"));
		}

		identityService.createMembership(camundaUser.getId(),
				identityService.createGroupQuery().groupId("guest").singleResult().getId());

		runtimeService.setVariable(processInstanceId, "camundaUser", camundaUser);
		runtimeService.setVariable(processInstanceId, "customUser", customUser);
		runtimeService.setVariable(processInstanceId, "requestMail", false);

		HashMap<String, Object> fieldMap = new HashMap<>();

		for (FormFieldDataDto field : formFieldData) {
			if (!field.getFieldId().equals("scienceFields")) {
				fieldMap.put(field.getFieldId(), field.getFieldValue());
			}
		}

		ArrayList<String> list = new ArrayList<String>();
		list.add(customUser.getRole());
		Authentication auth = new Authentication(camundaUser.getId(), list);

		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// engine.getProcessEngineConfiguration().setAuthorizationEnabled(true).setAuthorizationEnabledForCustomCode(true);
		engine.getIdentityService().clearAuthentication();
		engine.getIdentityService().setAuthentication(auth);
		
		formService.submitTaskForm(taskId, fieldMap);

		return new ResponseEntity<>(customUser, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getVar/{processInstanceId}/{varName}", produces = "application/json")
	public ResponseEntity<Object> getProcessVariable(@PathVariable String processInstanceId, @PathVariable String varName) {
		Object var = runtimeService.getVariable(processInstanceId, varName);
		return new ResponseEntity<Object>(var, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/deleteUser/{userId}")
	public ResponseEntity revertUser(@PathVariable String userId) {
		User user = identityService.createUserQuery().userId(userId).singleResult();
		CustomUser custom = userService.findByUsername(userId);
		identityService.deleteMembership(userId, custom.getRole());
		identityService.deleteUser(userId);
		userService.delete(custom);
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/completeTask/{taskId}/{userId}", consumes = "application/json")
	public ResponseEntity completeTask(@RequestBody List<FormFieldDataDto> fieldData, @PathVariable String taskId,
			@PathVariable String userId) {
		HashMap<String, Object> fieldMap = new HashMap<>();
		Boolean isRev = false;
		for (FormFieldDataDto formField : fieldData) {
			fieldMap.put(formField.getFieldId(), formField.getFieldValue());
			if (formField.getFieldId().equals("approve")) {
				isRev = Boolean.parseBoolean(formField.getFieldValue());
			}
		}
		String processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getExecutionId();
		CustomUser user = userService.findByUsername(userId);
		user.setIsReviewer(isRev);
		runtimeService.setVariable(processInstanceId, "approvedUser", user);
		formService.submitTaskForm(taskId, fieldMap);
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/loggedUser", produces = "application/json")
	public ResponseEntity<User> getLoggedUser() {
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		Authentication auth = engine.getIdentityService().getCurrentAuthentication();
		User user = engine.getIdentityService().createUserQuery().userId(auth.getUserId()).singleResult();
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/logout/{processInstanceId}")
	public ResponseEntity logout(@PathVariable String processInstanceId) {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		IdentityService idenServ = processEngine.getIdentityService();
		idenServ.clearAuthentication();
		// runtimeService.removeVariable(processInstanceId, "camundaUser");
		// runtimeService.removeVariable(processInstanceId, "customUser");
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/mail-request/{processInstanceId}")
	public ResponseEntity mailRequested(@PathVariable String processInstanceId) {
		User currentUser = (User) runtimeService.getVariable(processInstanceId, "camundaUser");
		Task task = taskService.createTaskQuery().taskAssignee(currentUser.getId()).singleResult();
		runtimeService.setVariable(processInstanceId, "requestMail", true);
		taskService.complete(task.getId());

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/mail-confirm/{processInstanceId}", produces = "application/json")
	public ResponseEntity<User> mailConfirmed(@PathVariable String processInstanceId) {
		User currentUser = (User) runtimeService.getVariable(processInstanceId, "camundaUser");
		Task task = taskService.createTaskQuery().taskAssignee(currentUser.getId()).singleResult();
		runtimeService.setVariable(processInstanceId, "requestMail", false);
		taskService.complete(task.getId());
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/login/{processInstanceId}", consumes = "application/json")
	public ResponseEntity<User> login(@RequestBody UserCredentialsDto user, @PathVariable String processInstanceId) {
		CustomUser customUser = userService.checkUsernameAndPassword(user.getPassword(),
				user.getAuthenticatedUserPassword());
		User camundaUser = null;
		if (customUser != null) {
			// TODO: inbuilt checker
			identityService.setAuthenticatedUserId(customUser.getUsername());
			camundaUser = identityService.createUserQuery().userId(customUser.getUsername()).singleResult();
			// runtimeService.setVariable(processInstanceId, "camundaUser", camundaUser);
			// runtimeService.setVariable(processInstanceId, "customUser", customUser);
		}
		return new ResponseEntity<User>(camundaUser, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/membership/{userId}")
	public ResponseEntity<String> findMembership(@PathVariable String userId){
		List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
		String ret = "";
		for(Group g : groups) {
			ret = ret + g.getId();
		}
		return new ResponseEntity<String>(gson.toJson(ret), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getTasks/{userId}", produces = "application/json")
	public ResponseEntity<List<TaskDto>> getUserTasks(@PathVariable String userId) {
		List<TaskDto> taskDtoList = new ArrayList<>();
		for (Task task : taskService.createTaskQuery().taskAssignee(userId).list()) {
			User camUser = (User) runtimeService.getVariable(task.getExecutionId(), "camundaUser");
			ScienceMagazine magazine = (ScienceMagazine) runtimeService.getVariable(task.getExecutionId(), "newMagazine");
			CustomUser customUser = null;
			if(camUser != null) {
				customUser = userService.findByUsername(camUser.getId());
			}
			
			FormDataDto formDataDto = new FormDataDto(task.getId(),
					formService.getTaskFormData(task.getId()).getFormFields(), task.getExecutionId());

			TaskDto dto = new TaskDto(formDataDto, task.getName(), customUser, magazine);

			taskDtoList.add(dto);
		}
		return new ResponseEntity<List<TaskDto>>(taskDtoList, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/instance/{userId}", produces = "application/json")
	public ResponseEntity<String> findInstanceIdByUser(@PathVariable String userId) {
		List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery().active().list();
		String instanceId = null;
		for (ProcessInstance instance : instances) {
			User user = (User) runtimeService.getVariable(instance.getProcessInstanceId(), "camundaUser");
			if (user != null && user.getId().equals(userId)) {
				instanceId = instance.getProcessInstanceId();
			}
		}
		return new ResponseEntity<String>(gson.toJson(instanceId), HttpStatus.OK);
	}

}
