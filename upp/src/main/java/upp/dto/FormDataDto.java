package upp.dto;

import java.io.Serializable;
import java.util.List;

import org.camunda.bpm.engine.form.FormField;

public class FormDataDto implements Serializable{
	private String taskId;
	private List<FormField> fields;
	private String processInstanceId;
	
	public FormDataDto() {
		super();
	}

	public FormDataDto(String taskId, List<FormField> fields, String processInstanceId) {
		super();
		this.taskId = taskId;
		this.fields = fields;
		this.processInstanceId = processInstanceId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List<FormField> getFields() {
		return fields;
	}

	public void setFields(List<FormField> fields) {
		this.fields = fields;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
}
