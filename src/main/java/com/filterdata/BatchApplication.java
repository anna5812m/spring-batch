package com.filterdata;

import java.beans.XMLEncoder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@EnableBatchProcessing
@SpringBootApplication
public class BatchApplication {

    public static void main(String[] args) {
        prepareTestData(50000);
        SpringApplication.run(BatchApplication.class, args);
    }

    private static void prepareTestData(final int amount) {
        final Collection<Article> articles = new LinkedList<>();
        final Random random = new Random();

        for (int i = 1; i <= amount; i++) {
            String title = generateRandomRussianWord();

            String annotation = generateRandomSentence();

            if (random.nextDouble() < 0.1) {
                annotation += " " + getRandomVerb();
            }

            final Article article = new Article();
            article.setId(i);
            article.setTitle(title);
            article.setAnnotation(annotation);
            articles.add(article);
        }

        try (final XMLEncoder encoder = new XMLEncoder(new FileOutputStream(ArticleReportJobConfig.XML_FILE))) {
            encoder.writeObject(articles);
        } catch (final FileNotFoundException e) {
            log.error(e.getMessage(), e);
            System.exit(-1);
        }
    }

    private static String generateRandomRussianWord() {
        int length = ThreadLocalRandom.current().nextInt(4, 10);
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char randomChar = (char) ThreadLocalRandom.current().nextInt('а', 'я' + 1);
            word.append(randomChar);
        }
        return word.toString();
    }

    private static String generateRandomSentence() {
        String[] words = { "аннотация", "этой", "статьи", "содержит", "слово", "для", "примера" };
        return String.join(" ", Arrays.copyOfRange(words, 0, 5));
    }

    private static String getRandomVerb() {
        String[] verbs = { "обнаруживать", "выявлять", "открывать", "оценивать", "сформулировать", "определить" };
        return verbs[ThreadLocalRandom.current().nextInt(verbs.length)];
    }

}