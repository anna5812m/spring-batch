package com.filterdata;

import java.io.Serializable;
import java.util.Calendar;
import lombok.Data;

@Data
public class Article implements Serializable {

    private int id;
    private String title;
    private String annotation;

    @Override
    public String toString() {
        return String.format(
                "#%s, %s: %s",
                id,
                title,
                annotation
        );
    }

}
