package upp.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class ScienceMagazine implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column
	private String name;
	@Column
	private String issn;
	@Column
	private Boolean isOpenAcces;
	@Column
	private String scienceFields;
	@Column
	private Boolean active;
	@Column
	private String editors;
	@Column
	private String reviewers;
	@Column
	private String mainEditorId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIssn() {
		return issn;
	}
	public void setIssn(String issn) {
		this.issn = issn;
	}
	public Boolean getIsOpenAcces() {
		return isOpenAcces;
	}
	public void setIsOpenAcces(Boolean isOpenAcces) {
		this.isOpenAcces = isOpenAcces;
	}
	public String getScienceFields() {
		return scienceFields;
	}
	public void setScienceFields(String scienceFields) {
		this.scienceFields = scienceFields;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public String getEditors() {
		return editors;
	}
	public void setEditors(String editors) {
		this.editors = editors;
	}
	public String getReviewers() {
		return reviewers;
	}
	public void setReviewers(String reviewers) {
		this.reviewers = reviewers;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMainEditorId() {
		return mainEditorId;
	}
	public void setMainEditorId(String mainEditorId) {
		this.mainEditorId = mainEditorId;
	}
	
}
