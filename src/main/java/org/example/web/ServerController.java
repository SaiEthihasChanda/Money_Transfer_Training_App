package org.example.web;

import org.example.service.AccountService;
import org.example.dtos.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ServerController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/new-account")
    public ModelAndView newAccountForm() {
        System.out.println("hit end point for account");
        ModelAndView mav = new ModelAndView();

        mav.setViewName("Account-form");
        return mav;
    }

    @PostMapping("/Account")
    public String createAccount(@ModelAttribute AccountDto accountDto) {
        System.out.println(accountDto.getName());
        accountService.createAccount(accountDto);
        return "redirect:/"; // Redirect to the home page (or wherever)
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", ex.getMessage());
        mav.setViewName("error");
        return mav;
    }
}
