package upp.dto;

import java.io.Serializable;

import upp.model.CustomUser;
import upp.model.ScienceMagazine;

public class TaskDto implements Serializable{
	private FormDataDto formDataDto;
	private String name;
	private CustomUser customUser;
	private ScienceMagazine magazine;

	public TaskDto(FormDataDto formDataDto, String name, CustomUser customUser, ScienceMagazine magazine) {
		super();
		this.formDataDto = formDataDto;
		this.name = name;
		this.customUser = customUser;
		this.magazine = magazine;
	}

	public TaskDto() {
		super();
	}
	
	public FormDataDto getFormdataDto() {
		return formDataDto;
	}
	public void setFormdataDto(FormDataDto formdataDto) {
		this.formDataDto = formdataDto;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CustomUser getCustomUser() {
		return customUser;
	}
	public void setCustomUser(CustomUser customUser) {
		this.customUser = customUser;
	}

	public ScienceMagazine getMagazine() {
		return magazine;
	}

	public void setMagazine(ScienceMagazine magazine) {
		this.magazine = magazine;
	}
	
}
