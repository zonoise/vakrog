package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zonoise on 2017/08/24.
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    String index() {
        return "index";
    }
}
