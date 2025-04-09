package ca.homefinance.repository;

import ca.homefinance.entity.Category;
import ca.homefinance.entity.Transaction;
import ca.homefinance.entity.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate);

//    List<Transaction> findByAmount(BigDecimal amount);
//
//    List<Transaction> findByDate(Date date);
//
//    List<Transaction> findByEntityContainingIgnoreCase(String entity);
//
//    List<Transaction> findByDetailsContainingIgnoreCase(String details);
//
//    List<Transaction> findByCategory(Category categoryId);
//
//    List<Transaction> findByAccount(Transaction.AccountType account);
//
//    List<Transaction> findByTransactionType(Transaction.TransactionType transactionType);
//
//    List<Transaction> findByPerson(Person personId);
//
//    @Query("SELECT t FROM Transaction t WHERE " +
//            "(:amount IS NULL OR t.amount = :amount) AND " +
//            "(:startDate IS NULL OR :endDate IS NULL OR t.date BETWEEN :startDate AND :endDate) AND " +
//            "(:entity IS NULL OR LOWER(t.entity) LIKE LOWER(CONCAT('%', :entity, '%'))) AND " +
//            "(:details IS NULL OR LOWER(t.details) LIKE LOWER(CONCAT('%', :details, '%'))) AND " +
//            "(:category IS NULL OR t.category = :category) AND " +
//            "(:account IS NULL OR t.account = :account) AND " +
//            "(:transactionType IS NULL OR t.transactionType = :transactionType) AND " +
//            "(:person IS NULL OR t.person = :person)")
//    List<Transaction> searchTransactions(
//            @Param("amount") BigDecimal amount,
//            @Param("startDate") Date startDate,
//            @Param("endDate") Date endDate,
//            @Param("entity") String entity,
//            @Param("details") String details,
//            @Param("category") Category category,
//            @Param("account") Transaction.AccountType account,
//            @Param("transactionType") Transaction.TransactionType transactionType,
//            @Param("person") Person person
//    );
}
