package ca.homefinance.batch;

import ca.homefinance.entity.Transaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.configuration.annotation.StepScope;

@Configuration
public class BatchConfig {

    @Autowired
    private CsvTransactionReader csvReader;

    @Autowired
    private TransactionWriter writer;

    @Bean
    public Job transactionJob(JobRepository jobRepository,
                              PlatformTransactionManager transactionManager,
                              ItemReader<Transaction> csvFileReader) {
        return new JobBuilder("transactionJob", jobRepository)
                .start(importTransactionsStep(jobRepository, transactionManager, csvFileReader))
                .build();
    }

    @Bean
    public Step importTransactionsStep(JobRepository jobRepository,
                                       PlatformTransactionManager transactionManager,
                                       ItemReader<Transaction> csvFileReader) {
        return new StepBuilder("importTransactions", jobRepository)
                .<Transaction, Transaction>chunk(10, transactionManager)
                .reader(csvFileReader)
                .writer(writer)
                .build();
    }

    @StepScope
    @Bean
    public ItemReader<Transaction> csvFileReader(@Value("#{jobParameters['filePath']}") String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("The filePath job parameter is required.");
        }
        return csvReader.reader(filePath);
    }
}
