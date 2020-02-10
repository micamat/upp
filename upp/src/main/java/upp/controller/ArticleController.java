package upp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import upp.dto.FormDataDto;
import upp.dto.FormFieldDataDto;

@RestController
@RequestMapping(value = "rest-api/article")
public class ArticleController {
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	IdentityService identityService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	FormService formService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/startProcess/{loggedUser}")
	public ResponseEntity<FormDataDto> startProcess(@PathVariable String loggedUser){
		identityService.setAuthenticatedUserId(loggedUser);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_article");
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);
		List<FormField> formFields = formService.getTaskFormData(task.getId()).getFormFields();
		FormField field = formFields.get(0);
		return new ResponseEntity<FormDataDto>(new FormDataDto(task.getId(), formFields, processInstance.getId()), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getTasks/{userId}")
	public ResponseEntity<List<TaskDto>> getTasks(@PathVariable String userId){
		List<TaskDto> dtos = new ArrayList<TaskDto>();
		List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).list();
		for(Task task : tasks) {
			TaskDto dto = TaskDto.fromEntity(task);
			dtos.add(dto);
		}
		return new ResponseEntity<List<TaskDto>>(dtos, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getFormData/{taskId}")
	public ResponseEntity<List<FormField>> getFormData(@PathVariable String taskId){
		List<FormField> formData = formService.getTaskFormData(taskId).getFormFields();
		
		return new ResponseEntity<List<FormField>>(formData, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/completeTask/{taskId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity completeTask(@RequestBody List<FormFieldDataDto> data, @PathVariable String taskId) {
		List<FormField> formData = formService.getTaskFormData(taskId).getFormFields();
		for(FormFieldDataDto d : data) {
			for(FormField f : formData) {
				if(f.getId().equals(d.getFieldId())) {
					HashMap<String, String> properties = (HashMap<String, String>)f.getProperties();
					properties.put(d.getFieldId(), d.getFieldValue());
				}
			}
		}
		taskService.complete(taskId);
		
		return new ResponseEntity(HttpStatus.OK);
	}
}
