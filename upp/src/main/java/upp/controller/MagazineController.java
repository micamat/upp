package upp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.FormProperty;
import org.camunda.bpm.engine.form.FormType;
import org.camunda.bpm.engine.form.TaskFormData;
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
import upp.model.CustomUser;
import upp.model.ScienceMagazine;
import upp.service.MagazineService;
import upp.service.UserService;

@RestController
@RequestMapping("/rest-api")
public class MagazineController {
	
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
	
	@Autowired
	MagazineService magazineService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/magazine-form-data", produces = "application/json")
	public ResponseEntity<FormDataDto> getFormData() {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_magazine");
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);
		TaskFormData formData = formService.getTaskFormData(task.getId());
		List<FormField> formFields = formData.getFormFields();
		return new ResponseEntity<FormDataDto>(new FormDataDto(task.getId(), formFields, processInstance.getId()),
				HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/add-magazine/{taskId}/{processInstanceId}/{editorId}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ScienceMagazine> newMagazine(@RequestBody List<FormFieldDataDto> formFieldData, @PathVariable String taskId, @PathVariable String processInstanceId, @PathVariable String editorId){
		ScienceMagazine magazine = new ScienceMagazine();
		for (FormFieldDataDto field : formFieldData) {
			if (field.getFieldId().equals("name")) {
				magazine.setName(field.getFieldValue());
			}
			if (field.getFieldId().equals("issn")) {
				magazine.setIssn(field.getFieldValue());
			}
			if (field.getFieldId().equals("isOpenAccess")) {
				magazine.setIsOpenAcces(Boolean.parseBoolean(field.getFieldValue()));
			}
			if (field.getFieldId().equals("scienceFields")) {
				magazine.setScienceFields(field.getFieldValue());
			}
		}
		magazine.setActive(false);
		magazine.setMainEditorId(editorId);
		runtimeService.setVariable(processInstanceId, "newMagazine", magazine);
		
		HashMap<String, Object> fieldMap = new HashMap<>();

		for (FormFieldDataDto field : formFieldData) {
			if (!field.getFieldId().equals("scienceFields")) {
				fieldMap.put(field.getFieldId(), field.getFieldValue());
			}
		}

		formService.submitTaskForm(taskId, fieldMap);
		
		return new ResponseEntity<ScienceMagazine>(magazine, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/editors/{scienceFields}", produces = "application/json")
	public ResponseEntity<List<CustomUser>> findEditorsByScienceFields(@PathVariable String scienceFields){
		List<CustomUser> ret = new ArrayList<CustomUser>();
		List<CustomUser> editors = new ArrayList<CustomUser>();
		for(CustomUser user : userService.findAll()) {
			if(user.getRole() != null && user.getRole().equals("editor")) {
				editors.add(user);
			}
		}
		for(CustomUser editor: editors) {
			List<String> fields = Arrays.asList(editor.getScienceFields().split(","));
			for(String field : fields) {
				for(String f : scienceFields.split(",")) {
					if(field.equals(f)) {
						if(!ret.contains(editor)) {
							ret.add(editor);
							break;
						}
					}
				}
			}
		}
		return new ResponseEntity<List<CustomUser>>(ret, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/reviewers/{scienceFields}", produces = "application/json")
	public ResponseEntity<List<CustomUser>> findReviewersByScienceFields(@PathVariable String scienceFields){
		List<CustomUser> ret = new ArrayList<CustomUser>();
		List<CustomUser> reviewers = new ArrayList<CustomUser>();
		for(CustomUser user : userService.findAll()) {
			if(user.getRole() != null && user.getRole().equals("reviewer")) {
				reviewers.add(user);
			}
		}
		for(CustomUser editor: reviewers) {
			List<String> fields = Arrays.asList(editor.getScienceFields().split(","));
			for(String field : fields) {
				for(String f : scienceFields.split(",")) {
					if(field.equals(f)) {
						if(!ret.contains(editor)) {
							ret.add(editor);
							break;
						}	
					}
				}
			}
		}
		return new ResponseEntity<List<CustomUser>>(ret, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/completeEditorTask/{taskId}/{magazineId}", consumes = "application/json")
	public ResponseEntity completeTaskAddEditorsAndReviewers(@RequestBody List<FormFieldDataDto> fieldData, @PathVariable String taskId, @PathVariable Long magazineId) {
		HashMap<String, Object> fieldMap = new HashMap<>();
		String editors = null;
		String reviewers = null;
		Boolean activated = false;
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		for (FormFieldDataDto formField : fieldData) {
			//fieldMap.put(formField.getFieldId(), formField.getFieldValue());
			if(formField.getFieldId().equals("editors")) {
				editors = formField.getFieldValue();
			}
			if(formField.getFieldId().equals("reviewers")) {
				reviewers = formField.getFieldValue();
			}
			if(formField.getFieldId().equals("approve")) {
				fieldMap.put(formField.getFieldId(), formField.getFieldValue());
				activated = Boolean.parseBoolean(formField.getFieldValue());
			}
		}
		String processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getExecutionId();
		ScienceMagazine magazine = magazineService.findById(magazineId);
		if(task.getName().equals("Add editors and reviewers")) {
			magazine.setEditors(editors);
			magazine.setReviewers(reviewers);
		}
		else if(task.getName().equals("Check magazine data")){
			magazine.setActive(activated);
		}
		magazineService.save(magazine);
		runtimeService.setVariable(processInstanceId, "newMagazine", magazine);
		formService.submitTaskForm(taskId, fieldMap);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/updateMagazine", consumes = "application/json")
	public ResponseEntity update(@RequestBody ScienceMagazine magazine) {
		magazineService.save(magazine);
		return new ResponseEntity(HttpStatus.OK);
	}
}
