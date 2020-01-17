package upp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import upp.model.CustomUser;


public interface UserRepository extends JpaRepository<CustomUser, Long>{
	public CustomUser findByUsername(String username);
}
