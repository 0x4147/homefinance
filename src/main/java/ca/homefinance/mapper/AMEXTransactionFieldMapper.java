package ca.homefinance.mapper;

import ca.homefinance.entity.Transaction;
import ca.homefinance.helper.GeneralHelper;
import ca.homefinance.helper.TransactionCategorizer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM. yyyy", Locale.ENGLISH);
        transaction.setDate(LocalDate.parse(fieldSet.readString(0), formatter));

        String entity = fieldSet.readString(1);
        transaction.setEntity(entity);

        String rawAmount = fieldSet.readString(2);
        String cleanedAmount = rawAmount.replace("$", "").trim();
        BigDecimal amount = new BigDecimal(cleanedAmount);
        transaction.setAmount(amount);

        transaction.setTransactionType(GeneralHelper.determineTransactionType(entity, amount));

        transaction.setAccount(Transaction.AccountType.AMEX);
        transaction.setCategory(categorizer.getCategory(transaction.getEntity()));
        transaction.setPerson(null);

        return transaction;
    }
}
