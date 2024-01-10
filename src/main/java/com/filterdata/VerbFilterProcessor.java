package com.filterdata;

import java.util.Calendar;
import java.util.GregorianCalendar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class VerbFilterProcessor implements ItemProcessor<Article, Article> {

    @Override
    public Article process(final Article item) throws Exception {
        String annotation = item.getAnnotation().toLowerCase();

        if (annotation.contains("обнаруживать") ||
                annotation.contains("выявлять") ||
                annotation.contains("открывать") ||
                annotation.contains("оценивать") ||
                annotation.contains("сформулировать") ||
                annotation.contains("определить")) {
            log.info("Статья {} соответствует фильтру по глаголам", item);
            return item;
        }
        return null;
    }
}
