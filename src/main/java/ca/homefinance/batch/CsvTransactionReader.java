package ca.homefinance.batch;

import ca.homefinance.entity.Transaction;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.*;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class CsvTransactionReader {

    public FlatFileItemReader<Transaction> reader(String filePath) {
        FlatFileItemReader<Transaction> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(0); // No header to skip
        reader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer("\t") {{ // Tab-delimited
                setNames("date", "entity", "amount");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(Transaction.class);
            }});
        }});
        return reader;
    }
}