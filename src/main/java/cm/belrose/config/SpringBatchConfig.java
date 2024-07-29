package cm.belrose.config;

import cm.belrose.dto.EmployeeDto;
import cm.belrose.model.Employee;
import cm.belrose.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SpringBatchConfig {

    //private  JobRepository jobRepository;
   // private  PlatformTransactionManager platformTransactionManager;
    //private  DataSource dataSource;

    @Bean
    @StepScope //this annotation is mandatory when parsing any parameter in the reader method
    public FlatFileItemReader<EmployeeDto> reader(@Value("#{jobParameters['inputFilePath']}") FileSystemResource fileSystemResource) {
        return new FlatFileItemReaderBuilder<EmployeeDto>()
                .name("employeeItemReader")
                .linesToSkip(1) //skip the first line, cause the header
                .resource(fileSystemResource)
                .delimited().delimiter(",")
                .names("employeeId","fullName", "jobTitle","department","businessUnit","gender","ethnicity","age")
                .targetType(EmployeeDto.class)
                .build();
    }

    @Bean
    public ItemProcessor<EmployeeDto, Employee> itemProcessor() {
        return new EmployeeProcessor();
    }

    @Bean
    public EmployeeWriter employeeWriter(final EmployeeRepository employeeRepository) {
        return new EmployeeWriter(employeeRepository);
    }

    @Bean
    public Step importEmployeeStep(final JobRepository jobRepository,
                                   final PlatformTransactionManager platformTransactionManager,
                                   final EmployeeRepository employeeRepository) {
        return new StepBuilder("importEmployeesStep", jobRepository)
                .<EmployeeDto, Employee>chunk(100, platformTransactionManager)
                .reader(reader(null))
                .processor(itemProcessor())
                .writer(employeeWriter(employeeRepository))
                .build();
    }

    @Bean
    public Job importEmployeesJob(final JobRepository jobRepository,
                                  final PlatformTransactionManager transactionManager,
                                  final EmployeeRepository employeeRepository) {
        return new JobBuilder("job" , jobRepository)
                .incrementer(new RunIdIncrementer()) //Use it if you want
                .start(importEmployeeStep(jobRepository, transactionManager, employeeRepository))
                .build();
    }

}
