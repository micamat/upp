package upp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comment implements Serializable{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column
	private String text;
	
	@ManyToOne
	private CustomUser user;
	
	@ManyToOne
	private Article article;
	
	@Column
	private boolean visibleToAuthor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public CustomUser getUser() {
		return user;
	}

	public void setUser(CustomUser user) {
		this.user = user;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public boolean isVisibleToAuthor() {
		return visibleToAuthor;
	}

	public void setVisibleToAuthor(boolean visibleToAuthor) {
		this.visibleToAuthor = visibleToAuthor;
	}
}
