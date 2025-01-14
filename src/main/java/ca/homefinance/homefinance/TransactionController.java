package ca.homefinance.homefinance;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return new ResponseEntity<List<Transaction>>(transactionService.getAllTransactions(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Transaction>> getTransactionsById(@PathVariable ObjectId id) {
        return new ResponseEntity<Optional<Transaction>> (transactionService.getTransactionsById(id), HttpStatus.OK);
    }

    @GetMapping("/bytype/{type}")
    public ResponseEntity<Optional<List<Transaction>>> getTransactionsByType(@PathVariable String type){
        return new ResponseEntity<Optional<List<Transaction>>> (transactionService.getTransactionsByType(type), HttpStatus.OK);
    }
}
