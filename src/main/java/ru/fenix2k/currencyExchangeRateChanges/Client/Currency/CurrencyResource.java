package ru.fenix2k.currencyExchangeRateChanges.Client.Currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

/**
 * Класс для маппинга ответа внешнего сервиса курсов валют
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyResource {

    private String disclaimer;
    private String license;
    /** Дата на которую были актуальны данные */
    private long timestamp;
    /** Базовая валюта */
    private String base;
    /** Список валют и их курсы относительно базовой */
    private Map<String, Double> rates;

}
