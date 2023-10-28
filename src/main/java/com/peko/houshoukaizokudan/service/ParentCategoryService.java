package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.Repository.*;
import com.peko.houshoukaizokudan.model.ParentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParentCategoryService {

    @Autowired
    private ParentCategoryRepository parentCategoryRepository;
    public ParentCategory findbyid(int e) {
        Optional<ParentCategory> op= parentCategoryRepository.findById(e);
        if (op.isPresent()) {
            return op.get();
        }

        return null;
    }


}
