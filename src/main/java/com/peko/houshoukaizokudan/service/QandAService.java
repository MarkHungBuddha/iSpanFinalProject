package com.peko.houshoukaizokudan.service;


import com.peko.houshoukaizokudan.DTO.ProductQandADTO;
import com.peko.houshoukaizokudan.Repository.MemberRepository;
import com.peko.houshoukaizokudan.Repository.ProductBasicRepository;
import com.peko.houshoukaizokudan.Repository.QandARepository;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.QandA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QandAService {

    @Autowired
    private QandARepository qandARepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductBasicRepository productBasicRepository;

    //新增問題
    @Transactional
    public ProductQandADTO addQuestion(Integer productId, Integer buyerMemberId, String question) {
        Instant now = Instant.now();
        String iso8601Time = DateTimeFormatter.ISO_INSTANT.format(now);

        ProductQandADTO productQandADTO = ProductQandADTO.builder()
                .question(question)
                .questiontime(iso8601Time)
                .productId(productId)
                .buyerMemberid(buyerMemberId)
                .build();

        QandA qandA = new QandA();
        qandA.setProductid(productBasicRepository.findById(productId).orElse(null));
        qandA.setBuyerMember(memberRepository.findById(buyerMemberId).orElse(null));
        qandA.setQuestion(question);
        qandA.setQuestiontime(iso8601Time);
        qandARepository.save(qandA);

        return productQandADTO;
    }




    //列出全部問題byProductID
    @Transactional
    public List<ProductQandADTO> findProductQandAsByProductid(Integer productId) {
        List<QandA> qandAList=qandARepository.findByProductid_Id(productId);
        List<ProductQandADTO> productQandADTOList=new ArrayList<>();
        for (QandA qandA:qandAList){
            ProductQandADTO productQandADTO = new ProductQandADTO();
            productQandADTO.setProductId(qandA.getProductid().getId());
            productQandADTO.setBuyerMemberid(qandA.getBuyerMember().getId());
            productQandADTO.setQuestion(qandA.getQuestion());
            productQandADTO.setQuestiontime(qandA.getQuestiontime());
            productQandADTO.setSellerMemberid(qandA.getSellerMember().getId());
            productQandADTO.setAnswer(qandA.getAnswer());
            productQandADTO.setAnswertime(qandA.getAnswertime());

            productQandADTOList.add(productQandADTO);
        }
        return productQandADTOList;
    }

    @Transactional
    public List<ProductQandADTO> getUnansweredQuestions(Integer memberId) {
        List<QandA> unansweredQuestions = qandARepository.findBySellerMember_IdAndAnswerIsNull(memberId);
        return convertToDTO(unansweredQuestions);
    }

    @Transactional
    public void deleteQuestion(Integer qandaId, Integer memberId) {
        QandA qanda = qandARepository.findById(qandaId).orElse(null);
        if(qanda != null && qanda.getBuyerMember().getId().equals(memberId)) {
            qandARepository.deleteById(qandaId);
        } else {
            throw new RuntimeException("Unauthorized or Question not found");
        }
    }

    @Transactional
    public ProductQandADTO editQuestionByBuyer(Integer qandaId, Integer memberId, String newQuestion) {
        QandA qanda = qandARepository.findById(qandaId).orElseThrow(() -> new RuntimeException("Question not found"));
        if (!qanda.getBuyerMember().getId().equals(memberId)) {
            throw new RuntimeException("Unauthorized to edit this question");
        }
        qanda.setQuestion(newQuestion);
        qandARepository.save(qanda);
        return convertToDTO(qanda);
    }

    @Transactional
    public ProductQandADTO answerQuestionBySeller(Integer qandaId, Integer memberId, String answer) {
        QandA qanda = qandARepository.findById(qandaId).orElseThrow(() -> new RuntimeException("Question not found"));
        if (!qanda.getProductid().getSellermemberid().getId().equals(memberId)) {
            throw new RuntimeException("Unauthorized to answer this question");
        }

        Instant now = Instant.now();
        String iso8601Time = DateTimeFormatter.ISO_INSTANT.format(now);
        qanda.setAnswertime(iso8601Time);

        qanda.setAnswer(answer);
        qandARepository.save(qanda);
        return convertToDTO(qanda);
    }



    private List<ProductQandADTO> convertToDTO(List<QandA> qandas) {
        // Convert QandA entities to DTOs
        List<ProductQandADTO> dtos = new ArrayList<>();
        for (QandA qanda : qandas) {
            ProductQandADTO dto = new ProductQandADTO();
            dto.setProductId(qanda.getProductid().getId());
            dto.setBuyerMemberid(qanda.getBuyerMember().getId());
            dto.setQuestion(qanda.getQuestion());
            dto.setQuestiontime(qanda.getQuestiontime());
            dto.setSellerMemberid(qanda.getSellerMember().getId());
            dto.setAnswer(qanda.getAnswer());
            dto.setAnswertime(qanda.getAnswertime());
            dtos.add(dto);
        }
        return dtos;
    }

    private ProductQandADTO convertToDTO(QandA qanda) {
        ProductQandADTO dto = new ProductQandADTO();
        dto.setProductId(qanda.getProductid().getId());
        dto.setBuyerMemberid(qanda.getBuyerMember().getId());
        dto.setQuestion(qanda.getQuestion());
        dto.setQuestiontime(qanda.getQuestiontime());
        dto.setSellerMemberid(qanda.getSellerMember().getId());
        dto.setAnswer(qanda.getAnswer());
        dto.setAnswertime(qanda.getAnswertime());
        return dto;
    }




}
