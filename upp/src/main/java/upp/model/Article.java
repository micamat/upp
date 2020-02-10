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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Article implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column
	private String title;
	
	@Column
	private String coAuthors;
	
	@Column
	private String keyWords;
	
	@Column
	private String articleAbstract;
	
	@Column
	private String scienceField;
	
	@Column
	private String pdfText;
	
	@Column
	private String pdfFinal;
	
	@Column
	private Boolean acceptedBoolean;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private ScienceMagazine magazine;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private CustomUser author;
	
	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Comment> comments;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private CustomUser editor;
	
	@Column
	private String reviewers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCoAuthors() {
		return coAuthors;
	}

	public void setCoAuthors(String coAuthors) {
		this.coAuthors = coAuthors;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getArticleAbstract() {
		return articleAbstract;
	}

	public void setArticleAbstract(String articleAbstract) {
		this.articleAbstract = articleAbstract;
	}

	public String getScienceField() {
		return scienceField;
	}

	public void setScienceField(String scienceField) {
		this.scienceField = scienceField;
	}

	public String getPdfText() {
		return pdfText;
	}

	public void setPdfText(String pdfText) {
		this.pdfText = pdfText;
	}

	public String getPdfFinal() {
		return pdfFinal;
	}

	public void setPdfFinal(String pdfFinal) {
		this.pdfFinal = pdfFinal;
	}

	public Boolean getAcceptedBoolean() {
		return acceptedBoolean;
	}

	public void setAcceptedBoolean(Boolean acceptedBoolean) {
		this.acceptedBoolean = acceptedBoolean;
	}

	public ScienceMagazine getMagazine() {
		return magazine;
	}

	public void setMagazine(ScienceMagazine magazine) {
		this.magazine = magazine;
	}

	public CustomUser getAuthor() {
		return author;
	}

	public void setAuthor(CustomUser author) {
		this.author = author;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public CustomUser getEditor() {
		return editor;
	}

	public void setEditor(CustomUser editor) {
		this.editor = editor;
	}

	public String getReviewers() {
		return reviewers;
	}

	public void setReviewers(String reviewers) {
		this.reviewers = reviewers;
	}
}
