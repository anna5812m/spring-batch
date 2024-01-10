package com.filterdata;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@Slf4j
@RunWith(SpringRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, StepScopeTestExecutionListener.class})
@ContextConfiguration(classes = {BatchApplication.class, BatchTestConfiguration.class})
public class VerbFilterProcessorTest {

    @Autowired
    private VerbFilterProcessor processor;

    public StepExecution getStepExecution() {
        log.info("getting step execution");
        return MetaDataInstanceFactory.createStepExecution();
    }

    @Test
    public void filter() throws Exception {
        final Article article = new Article();
        article.setId(1);
        article.setTitle("title");
        article.setAnnotation("annotation");
        Assert.assertNotNull(processor.process(article));
    }

    @Test
    public void filterId() throws Exception {
        final Article article = new Article();
        article.setId(1);
        article.setTitle("titke");
        article.setAnnotation("annotation");
        final int id = StepScopeTestUtils.doInStepScope(
            getStepExecution(),
            () -> processor.process(article).getId()
        );
        Assert.assertEquals(1, id);
    }

}
