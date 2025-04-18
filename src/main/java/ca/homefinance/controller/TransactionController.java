package ca.homefinance.controller;

import ca.homefinance.dto.MonthlyBalanceResponseDto;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
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

    @GetMapping("/getMonthlyBalance")
    public ResponseEntity<MonthlyBalanceResponseDto> getMonthlyBalance(@RequestParam String month, @RequestParam String year) {
        try {
            LocalDate startLocalDate = LocalDate.of(Integer.valueOf(year), Integer.valueOf(month), 1);
            LocalDate endLocalDate = startLocalDate.withDayOfMonth(startLocalDate.lengthOfMonth());
            Date startDate = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(endLocalDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
            BigDecimal totalAsankaExpenses = BigDecimal.ZERO;
            BigDecimal totalDivyaExpenses = BigDecimal.ZERO;
            BigDecimal totalCardPaymentsAsanka = BigDecimal.ZERO;
            BigDecimal totalCardPaymentsDivya = BigDecimal.ZERO;
            MonthlyBalanceResponseDto monthlyBalanceResponseDto = new MonthlyBalanceResponseDto();

            List<Transaction> expensesPaidFromPersonalAccounts =
                    transactionService.searchTransactions(
                            Collections.emptyList(),
                            startLocalDate,
                            endLocalDate,
                            Collections.emptyList(),
                            null,
                            Collections.emptyList(),
                            Arrays.asList(Transaction.AccountType.ASANKA, Transaction.AccountType.DIVYA),
                            Arrays.asList(Transaction.TransactionType.EXPENSE), Collections.emptyList());

            List<Transaction> cardPayments =
                    transactionService.searchTransactions(
                            Collections.emptyList(),
                            startLocalDate,
                            endLocalDate,
                            Collections.emptyList(),
                            null,
                            Collections.emptyList(),
                            Arrays.asList(Transaction.AccountType.CIBC, Transaction.AccountType.AMEX),
                            Arrays.asList(Transaction.TransactionType.CARDPAYMENT), Collections.emptyList());

            for (Transaction txn : expensesPaidFromPersonalAccounts) {
                if (txn.getAccount() == Transaction.AccountType.ASANKA) {
                    totalAsankaExpenses = totalAsankaExpenses.add(txn.getAmount());
                } else if (txn.getAccount() == Transaction.AccountType.DIVYA) {
                    totalDivyaExpenses = totalDivyaExpenses.add(txn.getAmount());
                }
            }

            for (Transaction txn : cardPayments) {
                if (txn.getPerson().getPersonId() == 1) {
                    totalCardPaymentsAsanka = totalCardPaymentsAsanka.add(txn.getAmount());
                } else if (txn.getPerson().getPersonId() == 2) {
                    totalCardPaymentsDivya = totalCardPaymentsDivya.add(txn.getAmount());
                }
            }

            BigDecimal totalAsankaPaid = totalAsankaExpenses.add(totalCardPaymentsAsanka);
            BigDecimal totalDivyaPaid = totalDivyaExpenses.add(totalCardPaymentsDivya);

            BigDecimal difference = totalAsankaPaid.subtract(totalDivyaPaid);
            int compare = difference.compareTo(BigDecimal.ZERO);

            monthlyBalanceResponseDto.setBalanceAmount(difference.toString());
            monthlyBalanceResponseDto.setAsankaPaid(totalAsankaPaid.toString());
            monthlyBalanceResponseDto.setDivyaPaid(totalDivyaPaid.toString());
            monthlyBalanceResponseDto.setMonthAndYear(month + ", " + year);

            if (compare > 0) {
                // Asanka paid more
                monthlyBalanceResponseDto.setWhoOwes("Divya");
            } else if (compare < 0) {
                // Divya paid more
                monthlyBalanceResponseDto.setWhoOwes("Asanka");
            } else {
                // Both paid equally
            }

            return new ResponseEntity<>(monthlyBalanceResponseDto, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}