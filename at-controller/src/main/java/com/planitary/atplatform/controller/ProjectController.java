package com.planitary.atplatform.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ProjectController {

    @RequestMapping("/test")
    public String doHello(String name){
        return "hello " +name;
    }
}
