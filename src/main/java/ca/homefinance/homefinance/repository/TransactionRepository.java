package ca.homefinance.homefinance.repository;

import ca.homefinance.homefinance.entity.Category;
import ca.homefinance.homefinance.entity.Person;
import ca.homefinance.homefinance.entity.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByAmount(BigDecimal amount);

    List<Transaction> findByDate(Date date);

    List<Transaction> findByDateBetween(Date startDate, Date endDate);

    List<Transaction> findByEntityContainingIgnoreCase(String entity);

    List<Transaction> findByDetailsContainingIgnoreCase(String details);

    List<Transaction> findByCategory(Integer categoryId);

    List<Transaction> findByAccount(Transaction.AccountType account);

    List<Transaction> findByTransactionType(Transaction.TransactionType transactionType);

    List<Transaction> findByPerson(Integer personId);

    @Query("SELECT t FROM Transaction t WHERE " +
            "(:amount IS NULL OR t.amount = :amount) AND " +
            "(:startDate IS NULL OR :endDate IS NULL OR t.date BETWEEN :startDate AND :endDate) AND " +
            "(:entity IS NULL OR LOWER(t.entity) LIKE LOWER(CONCAT('%', :entity, '%'))) AND " +
            "(:details IS NULL OR LOWER(t.details) LIKE LOWER(CONCAT('%', :details, '%'))) AND " +
            "(:category IS NULL OR t.category = :category) AND " +
            "(:account IS NULL OR t.account = :account) AND " +
            "(:transactionType IS NULL OR t.transactionType = :transactionType) AND " +
            "(:person IS NULL OR t.person = :person)")
    List<Transaction> searchTransactions(
            @Param("amount") BigDecimal amount,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("entity") String entity,
            @Param("details") String details,
            @Param("category") Category category,
            @Param("account") Transaction.AccountType account,
            @Param("transactionType") Transaction.TransactionType transactionType,
            @Param("person") Person person
    );
}
