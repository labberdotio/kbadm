//package io.labber.kbadm;
//
//// import com.ankit.rag_demo.model.Answer;
//// import com.ankit.rag_demo.model.Question;
//// import com.ankit.rag_demo.service.RagService;
//import org.springframework.web.bind.annotation.*;
//
//import io.labber.kbadm.Answer;
//import io.labber.kbadm.Question;
//import io.labber.kbadm.RagService;
//
//@RestController
//// @RequestMapping(value = "/api/v1.0/chat")
//public class RagController {
//
//    private final RagService ragService;
//
//    public RagController(RagService ragService) {
//        this.ragService = ragService;
//    }
//
//    @PostMapping("/ask")
//    public Answer ask(@RequestBody Question question){
//        return ragService.askQuestion(question);
//    }
//
//    @GetMapping("/health")
//    public String health() {
//        return "RAG service is running!";
//    }
//
//}
