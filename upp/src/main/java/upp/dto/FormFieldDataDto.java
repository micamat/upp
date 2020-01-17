package upp.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FormFieldDataDto implements Serializable{
	private String fieldId;
	private String fieldValue;
	
	public FormFieldDataDto() {
		super();
	}
	
	public FormFieldDataDto(String fieldId, String fieldValue) {
		super();
		this.fieldId = fieldId;
		this.fieldValue = fieldValue;
	}
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
}
