package com.peko.houshoukaizokudan.service;


import com.peko.houshoukaizokudan.model.test.QandARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QandAService {

    @Autowired
    private QandARepository qandARepository;
}
