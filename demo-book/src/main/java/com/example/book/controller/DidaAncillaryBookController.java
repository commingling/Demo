package com.example.book.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DidaAncillaryBookController {
    @GetMapping(value = {"/dida/ancillaryBook"})
    public ResponseEntity<String> ancillarySearch(@RequestBody String body) {

        return ResponseEntity.ok("ancillaryBook");
    }

    @GetMapping("/ancillaryBook")
    public String ancillaryBook() {
        return "Hello World!";
    }

}