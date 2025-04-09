package ca.homefinance.controller;

import ca.homefinance.dto.TransactionDto;
import ca.homefinance.entity.Transaction;
import ca.homefinance.repository.CategoryRepository;
import ca.homefinance.repository.PersonRepository;
import ca.homefinance.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @Autowired
    private final TransactionService transactionService;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final PersonRepository personRepository;

    @GetMapping("/getAllTransactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return new ResponseEntity<>(transactionService.getAllTransactions(), HttpStatus.OK);
    }

    @GetMapping("/getTransactionsByDateRange")
    public ResponseEntity<List<TransactionDto>> getTransactionsByDateRange(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        List<TransactionDto> toRet = transactionService.getTransactionsByDateRange(start, end)
                .stream()
                .map(tx -> new TransactionDto(
                        tx.getAmount(),
                        tx.getDate(),
                        tx.getEntity(),
                        tx.getDetails(),
                        tx.getAccount().name(),
                        tx.getTransactionType().name(),
                        tx.getCategory().getName(),
                        tx.getPerson().getName()
                )).collect(Collectors.toList());
        return new ResponseEntity<>(toRet, HttpStatus.OK);
    }

    @PostMapping("/saveTransaction")
    public ResponseEntity<Transaction> saveTransaction(@RequestBody TransactionDto transaction) {
        Transaction transactionEntity = new Transaction();
        transactionEntity.setAmount(transaction.getAmount());
        transactionEntity.setDate(transaction.getDate());
        transactionEntity.setEntity(transaction.getEntity());
        transactionEntity.setDetails(transaction.getDetails());
        transactionEntity.setAccount(Transaction.AccountType.valueOf(transaction.getAccount()));
        transactionEntity.setTransactionType(Transaction.TransactionType.valueOf(transaction.getTransactionType()));
        transactionEntity.setCategory(categoryRepository.findById(Integer.parseInt(transaction.getCategory())).orElseThrow());
        transactionEntity.setPerson(personRepository.findById(Integer.parseInt(transaction.getPerson())).orElseThrow());
        return new ResponseEntity<>(transactionService.saveTransaction(transactionEntity), HttpStatus.OK);
    }

}