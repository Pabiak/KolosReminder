package me.pabiak.kolosreminder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CronjobHandler {

    @GetMapping("/cronjob")
    public ResponseEntity<String> processPing(){
        return ResponseEntity.ok().body(":P");
    }
}
