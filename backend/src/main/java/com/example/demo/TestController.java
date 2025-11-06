package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController {
  @GetMapping("/db_test")
  public String db_test() {return "db_test";}
}
