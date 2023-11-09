package com.example.demosearch.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompareController {

    private static final Logger logger = LoggerFactory.getLogger(CompareController.class);

    @PostMapping(value = {"/compareSearch"}, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> search(@RequestBody String unionSearchRequest) {
        logger.error("【比价查询】 比价查询发生未知异常...");
        return ResponseEntity.ok("compareSearch");
    }
}
