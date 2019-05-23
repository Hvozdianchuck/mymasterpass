package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
public class CostumerController {
    @Autowired
    RestTemplate restTemplate;
//    @RequestMapping("/costumer")
//    String getcallBack(){
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setAccept(Arrays.asList(MediaType.TEXT_HTML));
//        HttpEntity httpEntity = new HttpEntity(httpHeaders);
//
//    }
}
