package ca.homefinance.controller;

import ca.homefinance.entity.Transaction;
import ca.homefinance.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @Autowired
    private final TransactionService transactionService;

    @GetMapping("/getAllTransactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return new ResponseEntity<>(transactionService.getAllTransactions(), HttpStatus.OK);
    }

    @GetMapping("/getTransactionsByDateRange")
    public ResponseEntity<List<Transaction>> getTransactionsByDateRange(@RequestParam Date start, @RequestParam Date end) {
        return new ResponseEntity<>(transactionService.getTransactionsByDateRange(start, end), HttpStatus.OK);
    }

    @PostMapping("/saveTransaction")
    public ResponseEntity<Transaction> saveTransaction(@RequestBody Transaction transaction) {
        return new ResponseEntity<>(transactionService.saveTransaction(transaction), HttpStatus.OK);
    }

}