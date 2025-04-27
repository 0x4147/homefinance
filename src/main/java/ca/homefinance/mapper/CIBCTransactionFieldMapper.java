package ca.homefinance.mapper;

import ca.homefinance.entity.Transaction;
import ca.homefinance.helper.TransactionCategorizer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CIBCTransactionFieldMapper implements FieldSetMapper<Transaction> {
    @Override
    public Transaction mapFieldSet(FieldSet fieldSet) {
        Transaction transaction = new Transaction();
        TransactionCategorizer categorizer = new TransactionCategorizer();

        transaction.setDate(LocalDate.parse(fieldSet.readString(0), DateTimeFormatter.ofPattern("M/d/yyyy")));
        transaction.setEntity(fieldSet.readString(1));
        transaction.setAmount(new BigDecimal(fieldSet.readString(2)));

        transaction.setAccount(Transaction.AccountType.CIBC);
        transaction.setTransactionType(Transaction.TransactionType.EXPENSE);

        transaction.setCategory(categorizer.getCategory(transaction.getEntity()));
        //transaction.setPerson(TransactionCategorizer.getPerson(entity));

        return transaction;
    }
}
