package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.model.ParentCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParentCategoryService {

    @Autowired
    private ParentCategoryRepository parentCategoryRepository;
}
