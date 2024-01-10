package com.filterdata;

import java.util.Arrays;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
public class ArticleReportJobConfig {

    public static final String TASKLET_STEP = "taskletStep";

    public static final String XML_FILE = "database.xml";

    private static final String JOB_NAME = "articleReportJob";

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobBuilderFactory jobBuilders;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    private JobExplorer jobs;

    @Bean
    public Job articleReportJob() {
        return jobBuilders.get(JOB_NAME)
                .start(taskletStep())
                .next(chunkStep())
                .build();
    }

    @Bean
    public Step taskletStep() {
        return stepBuilders.get(TASKLET_STEP)
                .tasklet(tasklet())
                .build();
    }

    @Bean
    public Step chunkStep() {
        return stepBuilders.get("chunkStep")
                .<Article, Article>chunk(20)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @StepScope
    @Bean
    public ItemReader<Article> reader() {
        return new ArticleItemReader(XML_FILE);
    }

    @StepScope
    @Bean
    public ItemProcessor<Article, Article> processor() {
        final CompositeItemProcessor<Article, Article> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(verbFilterProcessor()));
        return processor;
    }

    @StepScope
    @Bean
    public VerbFilterProcessor verbFilterProcessor() {
        return new VerbFilterProcessor();
    }

    @StepScope
    @Bean
    public ItemWriter<Article> writer() {
        return new ArticleItemWriter();
    }

    @Bean
    public Tasklet tasklet() {
        return (contribution, chunkContext) -> {
            log.info("Executing tasklet step");
            return RepeatStatus.FINISHED;
        };
    }

    @Scheduled(fixedRate = 5000)
    public void run() throws Exception {
        JobExecution execution = jobLauncher.run(
                articleReportJob(),
                new JobParametersBuilder().addLong("uniqueness", System.nanoTime()).toJobParameters()
        );
        log.info("Exit status: {}", execution.getStatus());
    }

    @Autowired
    public void setJobExplorer(JobExplorer jobExplorer) {
        this.jobs = jobExplorer;
    }

    @PreDestroy
    public void destroy() throws NoSuchJobException {
        jobs.getJobNames().forEach(name -> log.info("job name: {}", name));
        jobs.getJobInstances(JOB_NAME, 0, jobs.getJobInstanceCount(JOB_NAME)).forEach(
                jobInstance -> {
                    log.info("job instance id {}", jobInstance.getInstanceId());
                }
        );
    }

}
