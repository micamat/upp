package upp.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class CustomUser implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column
	private String firstName;
	@Column
	private String lastName;
	@Column
	private String designation;
	@Column
	private String city;
	@Column
	private String country;
	@Column
	private String email;
	@Column
	private String username;
	@Column
	private String password;
	@Column
	private String confirmPassword;
	@Column
	private String scienceFields;
	@Column
	private Boolean isReviewer;
	@Column
	private String role;
	@Column
	private Boolean hasActiveFee; 
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Article> articles;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Comment> comments;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getScienceFields() {
		return scienceFields;
	}
	public void setScienceFields(String scienceFields) {
		this.scienceFields = scienceFields;
	}
	public Boolean getIsReviewer() {
		return isReviewer;
	}
	public void setIsReviewer(Boolean isReviewer) {
		this.isReviewer = isReviewer;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public Boolean getHasActiveFee() {
		return hasActiveFee;
	}
	public void setHasActiveFee(Boolean hasActiveFee) {
		this.hasActiveFee = hasActiveFee;
	}
	
}
