package upp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.ScienceMagazine;
import upp.repository.MagazineRepository;

@Service
public class MagazineService {
	@Autowired
	MagazineRepository repo;
	
	public void save(ScienceMagazine magazine) {
		repo.save(magazine);
	}
	
	public ScienceMagazine findById(Long id) {
		return repo.findById(id).get();
	}
	
	public List<ScienceMagazine> findAll(){
		return repo.findAll();
	}
	
	public ScienceMagazine findByName(String name) {
		return repo.findByName(name);
	}
}
