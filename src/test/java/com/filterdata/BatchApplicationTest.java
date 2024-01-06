package com.filterdata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BatchApplication.class, BatchTestConfiguration.class})
public class BatchApplicationTest {

    @Test
    public void contextLoads() {
    }

}
