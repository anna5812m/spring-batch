package com.filterdata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

@Slf4j
public class TransactionValidatingProcessor extends ValidatingItemProcessor<Customer> {
    public TransactionValidatingProcessor(final int limit) {
        super(
            item -> {
                if (item.getTransactions() >= limit) {
                    throw new ValidationException("Клиент имеет более " + limit + " транзакций");
                }
                log.info("Клиент {} соответствует фильтру транзакций", item);
            }
        );
        setFilter(true);
    }
}
