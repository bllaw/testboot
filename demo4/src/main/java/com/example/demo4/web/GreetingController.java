package com.example.demo4.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo4.domain.Kugc;
import com.example.demo4.domain.User;
import com.example.demo4.service.UserService;

@Controller
public class GreetingController {
    @Autowired
    private UserService userService;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
    	final User user = userService.findUserById(1);
    	final User user2 = userService.findUserById(15);
    	System.out.println(user2);
        model.addAttribute("name", name + "XXX");
        model.addAttribute("user", user);
        return "greeting";
    }

    @GetMapping("/kugc")
    public String kugc(@RequestParam(name="id", required=false, defaultValue="M0032") String id, Model model) {
    	final Kugc kugc = userService.findKugcById(id);
        model.addAttribute("name", kugc.getKugc_cgcid() + " - " + kugc.getKugc_cgcname());
        return "kugc";
    }

}
