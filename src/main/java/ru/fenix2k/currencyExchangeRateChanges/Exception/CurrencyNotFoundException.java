package ru.fenix2k.currencyExchangeRateChanges.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение если валюта в списке не найдена
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CurrencyNotFoundException extends Exception {

    public CurrencyNotFoundException(String message) {
        super(message);
    }
}
