package com.exercise.todolist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Devil on 26.11.2017.
 */
@Controller
public class NotFoundController {
    @GetMapping("/notFoundPage")
    public ResponseEntity notFound() {
        return ResponseEntity.notFound().build();
    }
}
