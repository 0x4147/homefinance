package ca.homefinance.homefinance.repository;

import ca.homefinance.homefinance.entity.Transaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, ObjectId> {

    Optional<List<Transaction>> findTransactionsByType(String type);
}
