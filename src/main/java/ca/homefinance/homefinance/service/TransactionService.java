package ca.homefinance.homefinance.service;

import ca.homefinance.homefinance.entity.Transaction;
import ca.homefinance.homefinance.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;

    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    public Transaction saveTransaction(Transaction transaction) {
        return repository.save(transaction);
    }

    public List<Transaction> getTransactionsByType(String type) {
        return repository.findByType(type);
    }

    public List<Transaction> getTransactionsByDateRange(LocalDate start, LocalDate end) {
        return repository.findByDateBetween(start, end);
    }
}
