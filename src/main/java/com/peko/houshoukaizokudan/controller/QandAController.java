package com.peko.houshoukaizokudan.controller;


import com.peko.houshoukaizokudan.DTO.ProductQandADTO;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.QandA;
import com.peko.houshoukaizokudan.service.QandAService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.format.DateTimeFormatter;




import java.util.List;

@RestController
public class QandAController {

    @Autowired
    private QandAService qandAService;



    //session抓memeberid 傳入productid 新增Q
    @PostMapping("/product/qanda/add/{productid}")
    public ResponseEntity<ProductQandADTO> addQandA(@PathVariable Integer productid, HttpSession session, @RequestBody String question) {
        try {
            Member loginUser = (Member) session.getAttribute("loginUser");
            ProductQandADTO productQandADTO = qandAService.addQuestion(productid, loginUser.getId(), question);
            return ResponseEntity.ok(productQandADTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/qanda/unanswered")
    public ResponseEntity<List<ProductQandADTO>> getUnansweredQuestions(HttpSession session) {
        Member member = (Member) session.getAttribute("loginUser");
        List<ProductQandADTO> unanswered = qandAService.getUnansweredQuestions(member.getId());
        return ResponseEntity.ok(unanswered);
    }

    @DeleteMapping("/qanda/delete/{qandaId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Integer qandaId, HttpSession session) {
        Member member = (Member) session.getAttribute("loginUser");
        qandAService.deleteQuestion(qandaId, member.getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/product/qanda/edit-question/{qandaId}")
    public ResponseEntity<ProductQandADTO> editQuestionByBuyer(@PathVariable Integer qandaId, HttpSession session, @RequestBody String question) {
        try {
            Member loginUser = (Member) session.getAttribute("loginUser");
            ProductQandADTO updatedQuestion = qandAService.editQuestionByBuyer(qandaId, loginUser.getId(), question);
            return ResponseEntity.ok(updatedQuestion);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/product/qanda/answer/{qandaId}")
    public ResponseEntity<ProductQandADTO> answerQuestionBySeller(@PathVariable Integer qandaId, HttpSession session, @RequestBody String answer) {
        try {
            Member loginUser = (Member) session.getAttribute("loginUser");
            ProductQandADTO updatedAnswer = qandAService.answerQuestionBySeller(qandaId, loginUser.getId(), answer);
            return ResponseEntity.ok(updatedAnswer);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }



    @GetMapping("/product/qanda/{productid}")
    public ResponseEntity<List<ProductQandADTO>> findProductQandAsByProductid(@PathVariable Integer productid){
        try{
            List<ProductQandADTO> qandaList = qandAService.findProductQandAsByProductid(productid);
            return ResponseEntity.ok(qandaList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }
}
