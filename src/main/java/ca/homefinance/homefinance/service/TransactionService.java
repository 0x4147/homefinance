package ca.homefinance.homefinance.service;

import ca.homefinance.homefinance.repository.TransactionRepository;
import ca.homefinance.homefinance.entity.Transaction;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionsById(ObjectId id) {
        return transactionRepository.findById(id);
    }

    public Optional<List<Transaction>> getTransactionsByType(String type){
        return transactionRepository.findTransactionsByType(type);
    }
}
