package upp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.Comment;
import upp.repository.CommentRepository;

@Service
public class CommentService {
	
	@Autowired
	CommentRepository repo;
	
	public void save(Comment comment) {
		repo.save(comment);
	}
}
