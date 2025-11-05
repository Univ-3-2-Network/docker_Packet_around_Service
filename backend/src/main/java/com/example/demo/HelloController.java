package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HelloController {
  @GetMapping("/hello")
  public String hello() {return "hello";}
}