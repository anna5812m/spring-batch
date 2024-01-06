package com.filterdata;

import java.io.Serializable;
import java.util.Calendar;
import lombok.Data;

@Data
public class Customer implements Serializable {

    private int id;
    private String name;
    private Calendar birthday;
    private int transactions;

    @Override
    public String toString() {
        return String.format(
            "#%s, %s родился %3$tb %3$te, %3$tY, выполнил %4$s транзакций",
            id,
            name,
            birthday.getTime(),
            transactions
        );
    }

}
