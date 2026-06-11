package org.example.web;

import org.example.dtos.AccountDto;
import org.example.dtos.TransferDto;
import org.example.service.AccountService;
import org.example.service.RewardServiceInterface;
import org.example.service.TransactionServiceinterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ServerController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionServiceinterface transactionService;

    @Autowired
    private RewardServiceInterface rewardService;

    // ── Account endpoints ────────────────────────────────────────────────────

    @GetMapping("/new-account")
    public ModelAndView newAccountForm() {
        return new ModelAndView("Account-form");
    }

    @PostMapping("/Account")
    public String createAccount(@ModelAttribute AccountDto accountDto) {
        accountService.createAccount(accountDto);
        return "redirect:/";
    }

    // ── Transfer endpoints ───────────────────────────────────────────────────

    @GetMapping("/transfer")
    public ModelAndView transferForm() {
        ModelAndView mav = new ModelAndView("transfer-form");
        mav.addObject("transferDto", new TransferDto());
        return mav;
    }

    @PostMapping("/transfer")
    public String processTransfer(@ModelAttribute TransferDto transferDto) {
        transactionService.transfer(
                transferDto.getFromAccountId(),
                transferDto.getToAccountId(),
                transferDto.getAmount()
        );
        return "redirect:/";
    }

    // ── Reward endpoints ─────────────────────────────────────────────────────

    @GetMapping("/rewards/{accountId}")
    public ModelAndView viewRewards(@PathVariable long accountId) {
        ModelAndView mav = new ModelAndView("rewards");
        mav.addObject("accountId", accountId);
        mav.addObject("totalPoints", rewardService.getRewardBalance(accountId));
        mav.addObject("rewardHistory", rewardService.getRewardHistory(accountId));
        return mav;
    }

    // ── Global exception handler ─────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }
}
