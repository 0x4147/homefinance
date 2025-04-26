package ca.homefinance.mapper;

import ca.homefinance.entity.Transaction;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CIBCTransactionFieldMapper implements FieldSetMapper<Transaction> {
    @Override
    public Transaction mapFieldSet(FieldSet fieldSet) {
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDate.parse(fieldSet.readString(0), DateTimeFormatter.ofPattern("M/d/yyyy")));
        transaction.setEntity(fieldSet.readString(1));
        transaction.setAmount(new BigDecimal(fieldSet.readString(2)));
        // Fill other fields as needed (account, type, etc.)
        return transaction;
    }
}
