package com.filterdata;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.IteratorItemReader;

@Slf4j
public class ArticleItemReader implements ItemReader<Article> {

    private final String filename;

    private ItemReader<Article> delegate;

    public ArticleItemReader(final String filename) {
        this.filename = filename;
    }

    @Override
    public Article read() throws Exception {
        if (delegate == null) {
            log.info("Создание reader элемента iterator");
            delegate = new IteratorItemReader<>(customers());
        }
        log.info("Чтение следующей аннотации");
        return delegate.read();
    }

    private List<Article> customers() throws FileNotFoundException {
        try (XMLDecoder decoder = new XMLDecoder(new FileInputStream(filename))) {
            return (List<Article>) decoder.readObject();
        }
    }
}
