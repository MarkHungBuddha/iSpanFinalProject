package com.peko.houshoukaizokudan.service;


import com.peko.houshoukaizokudan.DTO.ProductQandADTO;
import com.peko.houshoukaizokudan.Repository.QandARepository;
import com.peko.houshoukaizokudan.model.QandA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class QandAService {

    @Autowired
    private QandARepository qandARepository;


    //新增問題

    //編輯問題

    //修改問題

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

//            assert productQandADTO != null;
            productQandADTOList.add(productQandADTO);
        }
        return productQandADTOList;
    }


}
