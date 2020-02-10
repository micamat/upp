package upp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import upp.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	
}
