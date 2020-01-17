package upp.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.model.ScienceMagazine;

@Service
public class SaveMagazine implements JavaDelegate{
	
	@Autowired
	MagazineService magazineService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		ScienceMagazine magazine = (ScienceMagazine) execution.getVariable("newMagazine");
		magazineService.save(magazine);
	}
}
