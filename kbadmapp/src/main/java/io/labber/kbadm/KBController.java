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

	@PostMapping("/load/{kb}")
	public void load(
		@PathVariable("kb") String kb
	) throws Exception {
		this.kbService.loadDocuments(
			kb
		);
	}

	@PostMapping("/refs/{kb}")
	public void refs(
		@PathVariable("kb") String kb, 
		@RequestBody Question question
	) throws Exception {
		this.kbService.findRefs(
			kb, 
			question
		);
	}

	@PostMapping("/ask/{kb}")
	public Answer ask(
		@PathVariable("kb") String kb, 
		@RequestBody Question question
	) throws Exception {
		return this.kbService.runPrompt(
			kb, 
			question
		);
	}

}
