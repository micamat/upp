package upp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import upp.model.ScienceMagazine;

public interface MagazineRepository extends JpaRepository<ScienceMagazine, Long>{
	public ScienceMagazine findByName(String name);
}
