package jsondemo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.JsonLineMapper;
import org.springframework.batch.item.file.separator.JsonRecordSeparatorPolicy;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import com.google.common.collect.ListMultimap;
import org.springframework.core.io.FileSystemResource;

import java.util.HashMap;

@SpringBootApplication
@EnableBatchProcessing
@ImportResource("applicationContext.xml")
public class JsondemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsondemoApplication.class, args);
    }

    @Bean
    @StepScope
    public FlatFileItemReader<ListMultimap<String,String>> reader(@Value("#{jobParameters[FileName]}") String file){
        FlatFileItemReader reader = new FlatFileItemReader();
        reader.setResource(new FileSystemResource(file));
        reader.setLineMapper(new JsonLineMapper());
        reader.setRecordSeparatorPolicy(new JsonRecordSeparatorPolicy());
        return reader;
    }

    @Bean
    public ItemWriter writer(){
        return new DummyWriter();
    }

    @Bean
    public ItemProcessor processor(){
        return new PassThroughItemProcessor();
    }

    @Bean
    public Job importUserJob(JobBuilderFactory jobs, Step s1) {
        return jobs.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(s1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<ListMultimap> reader,
                      ItemWriter<ListMultimap> writer, ItemProcessor<ListMultimap, ListMultimap> processor) {
        return stepBuilderFactory.get("step1")
                .<ListMultimap, ListMultimap> chunk(2)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant().skip(Exception.class)
                .build();
    }

}
