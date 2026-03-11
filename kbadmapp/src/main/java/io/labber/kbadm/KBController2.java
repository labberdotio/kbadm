package io.labber.kbadm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
// @RequestMapping(value = "/api/v1.0/chat")
public class KBController2 {

	@Autowired
	protected KBService2 kbService;

	public KBController2() {
	}

	@PostMapping("/load/{kb}")
	public void load(
		@PathVariable("kb") String kb
	) throws Exception {
		System.out.println(" LOAD ");
		// return 
		this.kbService.loadDocuments(
			kb
		);
	}

	@PostMapping("/ask/{kb}")
	public Answer ask(
		@PathVariable("kb") String kb, 
		@RequestBody Question question
	) throws Exception {
		System.out.println(" ASK ");
		return this.kbService.askQuestion(
			kb, 
			question
		);
	}

}
