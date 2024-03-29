package com.filterdata;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BatchApplication.class, BatchTestConfiguration.class})
public class ArticleReportJobConfigTest {

    @Autowired
    private JobLauncherTestUtils testUtils;

    @Autowired
    private ArticleReportJobConfig config;

    @Test
    public void testEntireJob() throws Exception {
        final JobExecution result = testUtils.getJobLauncher().run(config.articleReportJob(), testUtils.getUniqueJobParameters());
        Assert.assertNotNull(result);
        Assert.assertEquals(BatchStatus.COMPLETED, result.getStatus());
    }

    @Test
    public void testSpecificStep() {
        Assert.assertEquals(BatchStatus.COMPLETED, testUtils.launchStep(ArticleReportJobConfig.TASKLET_STEP).getStatus());
    }

}
