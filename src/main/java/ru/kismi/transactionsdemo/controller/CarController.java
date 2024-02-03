package ru.kismi.transactionsdemo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kismi.transactionsdemo.service.TransactionService;

@RestController
@RequiredArgsConstructor
public class CarController {

    private final TransactionService transactionService;

    @GetMapping("/")
    public void test() {
        transactionService.readOnlyTransaction();
    }
}