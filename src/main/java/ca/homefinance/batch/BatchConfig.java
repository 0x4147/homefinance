package ca.homefinance.batch;

import ca.homefinance.entity.Transaction;
import ca.homefinance.mapper.CIBCTransactionFieldMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.configuration.annotation.StepScope;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final TransactionWriter writer;
    private final CIBCTransactionFieldMapper cibcTransactionFieldMapper;

    @Autowired
    public BatchConfig (TransactionWriter writer, CIBCTransactionFieldMapper cibcTransactionFieldMapper){
        this.writer = writer;
        this.cibcTransactionFieldMapper = cibcTransactionFieldMapper;
    }

    @Bean
    public Job transactionJob(JobRepository jobRepository,
                              PlatformTransactionManager transactionManager) {
        return new JobBuilder("transactionJob", jobRepository)
                .start(importTransactionsStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step importTransactionsStep(JobRepository jobRepository,
                                       PlatformTransactionManager transactionManager) {
        return new StepBuilder("importTransactions", jobRepository)
                .<Transaction, Transaction>chunk(10, transactionManager)
                .reader(csvFileReader(null)) // Correct usage: Let Spring inject the value
                .writer(writer)
                .build();
    }

    @StepScope
    @Bean
    public FlatFileItemReader<Transaction> csvFileReader(@Value("#{jobParameters['filePath']}") String filePath) {
        FlatFileItemReader<Transaction> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));  // Use filePath injected at runtime
        reader.setLinesToSkip(0); // No header to skip
        reader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setDelimiter(",");  // Comma-separated
                setQuoteCharacter('"'); // Handle quoted fields correctly
                setStrict(false);  // Allow extra commas (like trailing commas)
                setNames("date", "entity", "amount", "dummyColumn");  // Column names
            }});
            setFieldSetMapper(cibcTransactionFieldMapper);
        }});
        return reader;
    }
}
