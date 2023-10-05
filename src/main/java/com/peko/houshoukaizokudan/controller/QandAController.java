package com.peko.houshoukaizokudan.controller;

import com.peko.houshoukaizokudan.service.QandAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class QandAController {

    @Autowired
    private QandAService qandAService;
}
