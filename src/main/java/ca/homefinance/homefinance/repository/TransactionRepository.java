package ca.homefinance.homefinance.repository;

import ca.homefinance.homefinance.entity.Transaction;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByPersonId(Integer personId);

}
