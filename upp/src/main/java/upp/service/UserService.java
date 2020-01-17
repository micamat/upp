package upp.service;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.CustomUser;
import upp.repository.UserRepository;

@Service
public class UserService{
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	IdentityService identityService;
	
	public long count() {
		return userRepository.count();
	}
	
	public void save(CustomUser user) {
		userRepository.save(user);
	}
	
	public CustomUser findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public CustomUser checkUsernameAndPassword(String username, String password) {
		CustomUser user = userRepository.findByUsername(username);
		if(user != null) {
			if(identityService.checkPassword(user.getUsername(), password)) {
				return user;
			}
		}
		return null;
	}
	
	public void delete(CustomUser user) {
		userRepository.delete(user);
	}
	
	public List<CustomUser> findAll(){
		return userRepository.findAll();
	}
}
