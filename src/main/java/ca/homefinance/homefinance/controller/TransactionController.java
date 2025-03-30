package ca.homefinance.homefinance.controller;

import ca.homefinance.homefinance.entity.Transaction;
import ca.homefinance.homefinance.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @Autowired
    private final TransactionService service;

//    @GetMapping
//    public List<Transaction> getAllTransactions() {
//        return service.getAllTransactions();
//    }
//
//    @PostMapping
//    public Transaction addTransaction(@RequestBody Transaction transaction) {
//        return service.saveTransaction(transaction);
//    }
//
//    @GetMapping("/type/{type}")
//    public List<Transaction> getTransactionsByType(@PathVariable String type) {
//        return service.getTransactionsByType(type);
//    }
//
//    @GetMapping("/date-range")
//    public List<Transaction> getTransactionsByDateRange(
//            @RequestParam LocalDate start,
//            @RequestParam LocalDate end) {
//        return service.getTransactionsByDateRange(start, end);
//    }
}