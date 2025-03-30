package ca.homefinance.homefinance.service;

import ca.homefinance.homefinance.entity.Category;
import ca.homefinance.homefinance.entity.Person;
import ca.homefinance.homefinance.entity.Transaction;
import ca.homefinance.homefinance.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getByAmount(BigDecimal amount) {
        return transactionRepository.findByAmount(amount);
    }

    public List<Transaction> getByDate(Date date) {
        return transactionRepository.findByDate(date);
    }

    public List<Transaction> getByDateRange(Date startDate, Date endDate) {
        return transactionRepository.findByDateBetween(startDate, endDate);
    }

    public List<Transaction> getByEntity(String entity) {
        return transactionRepository.findByEntityContainingIgnoreCase(entity);
    }

    public List<Transaction> getByDetails(String details) {
        return transactionRepository.findByDetailsContainingIgnoreCase(details);
    }

    public List<Transaction> getByCategory(Integer categoryId) {
        return transactionRepository.findByCategory(categoryId);
    }

    public List<Transaction> getByAccount(Transaction.AccountType account) {
        return transactionRepository.findByAccount(account);
    }

    public List<Transaction> getByTransactionType(Transaction.TransactionType transactionType) {
        return transactionRepository.findByTransactionType(transactionType);
    }

    public List<Transaction> getByPerson(Integer personId) {
        return transactionRepository.findByPerson(personId);
    }

    public List<Transaction> searchTransactions(BigDecimal amount, Date startDate, Date endDate, String entity, String details, Category categoryId, Transaction.AccountType account, Transaction.TransactionType transactionType, Person person) {
        return transactionRepository.searchTransactions(amount, startDate, endDate, entity, details, categoryId, account, transactionType, person);
    }
}
