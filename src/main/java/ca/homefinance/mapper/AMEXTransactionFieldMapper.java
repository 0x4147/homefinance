package ca.homefinance.mapper;

import ca.homefinance.entity.Transaction;
import ca.homefinance.helper.TransactionCategorizer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class AMEXTransactionFieldMapper implements FieldSetMapper<Transaction> {
    private final TransactionCategorizer categorizer;

    @Autowired
    public AMEXTransactionFieldMapper(TransactionCategorizer categorizer) {
        this.categorizer = categorizer;
    }

    @Override
    public Transaction mapFieldSet(FieldSet fieldSet) {
        Transaction transaction = new Transaction();

        transaction.setDate(fieldSet.readString("date"));
        transaction.setEntity(fieldSet.readString("entity"));

        // Clean the amount field (remove $ and commas)
        String rawAmount = fieldSet.readString("amount");
        String cleanedAmount = rawAmount.replace("$", "").replace(",", "").trim();
        transaction.setAmount(new BigDecimal(cleanedAmount));

        transaction.setDate(LocalDate.parse(fieldSet.readString(0), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        transaction.setEntity(fieldSet.readString(1));
        transaction.setAmount(new BigDecimal(fieldSet.readString(2)));
        transaction.setAccount(Transaction.AccountType.CIBC);
        transaction.setTransactionType(Transaction.TransactionType.EXPENSE);
        transaction.setCategory(categorizer.getCategory(transaction.getEntity()));
        transaction.setPerson(null);

        return transaction;
    }
}
