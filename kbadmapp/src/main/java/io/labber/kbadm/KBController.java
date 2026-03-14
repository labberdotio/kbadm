package io.labber.kbadm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
// @RequestMapping(value = "/api/v1.0/chat")
public class KBController {

	@Autowired
	protected KBService kbService;

	public KBController() {
	}

	@PostMapping("/load/{schema}/{table}")
	public void load(
		@PathVariable("schema") String schema, 
		@PathVariable("table") String table
	) {
		System.out.println(" LOAD ");
		// return 
		this.kbService.loadDocuments(
			schema, 
			table
		);
	}

	@PostMapping("/ask/{schema}/{table}")
	public Answer ask(
		@PathVariable("schema") String schema, 
		@PathVariable("table") String table, 
		@RequestBody Question question
	) {
		System.out.println(" ASK ");
		return this.kbService.askQuestion(
			schema, 
			table, 
			question
		);
	}

}
