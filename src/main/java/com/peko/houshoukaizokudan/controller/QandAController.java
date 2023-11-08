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


    //session抓memeberid 傳入productid 買家新增問題
    @PostMapping("/customer/api/product/qanda/add/{productid}")
    public ResponseEntity<ProductQandADTO> addQandA(@PathVariable Integer productid, HttpSession session, @RequestParam("question") String question) {
        try {
            Member loginUser = (Member) session.getAttribute("loginUser");
            ProductQandADTO productQandADTO = qandAService.addQuestion(productid, loginUser.getId(), question);
            return ResponseEntity.ok(productQandADTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    //賣家查看尚未回答問題
    @GetMapping("/seller/api/qanda/unanswered")
    public ResponseEntity<List<ProductQandADTO>> getUnansweredQuestions(HttpSession session) {
        Member member = (Member) session.getAttribute("loginUser");
        List<ProductQandADTO> unanswered = qandAService.getUnansweredQuestions(member.getId());
        return ResponseEntity.ok(unanswered);
    }

    //買家顯示問的問題
    @GetMapping("/customer/api/qanda/asked")
    public ResponseEntity<List<ProductQandADTO>> getAskedQuestions(HttpSession session) {
        Member member = (Member) session.getAttribute("loginUser");
        if(member == null)
            return ResponseEntity.badRequest().build();
        List<ProductQandADTO> asked = qandAService.getAskedQuestions(member.getId());
        return ResponseEntity.ok(asked);
    }

    //賣家顯示所有問答
    @GetMapping("/seller/api/qanda/all")
    public ResponseEntity<List<ProductQandADTO>> getAllQuestions(HttpSession session) {
        Member member = (Member) session.getAttribute("loginUser");
        if(member == null)
            return ResponseEntity.badRequest().build();
        List<ProductQandADTO> asked = qandAService.getAllQuestions(member.getId());
        return ResponseEntity.ok(asked);
    }



    //買家刪除問題
    @DeleteMapping("/customer/api/qanda/delete/{qandaId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Integer qandaId, HttpSession session) {
        Member member = (Member) session.getAttribute("loginUser");
        qandAService.deleteQuestion(qandaId, member.getId());
        return ResponseEntity.ok().build();
    }

    //買家編輯問題
    @PutMapping("/customer/api/product/qanda/edit-question/{qandaId}")
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

    //賣家回答問題
    @PutMapping("/seller/api/product/qanda/answer/{qandaId}")
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


    //顯示商品所有問答
    @GetMapping("/public/api/product/{productid}/qanda")
    public ResponseEntity<List<ProductQandADTO>> findProductQandAsByProductid(@PathVariable Integer productid) {
        try {
            List<ProductQandADTO> qandaList = qandAService.findProductQandAsByProductid(productid);
            return ResponseEntity.ok(qandaList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }
}
