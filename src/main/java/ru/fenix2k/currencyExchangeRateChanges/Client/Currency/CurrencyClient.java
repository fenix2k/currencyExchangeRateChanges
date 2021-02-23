package ru.fenix2k.currencyExchangeRateChanges.Client.Currency;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.fenix2k.currencyExchangeRateChanges.Configuration.FeignClientConfig;

import java.util.Map;

/**
 * Описание Feign клиента для взаимодействия с внешним сервисом курсов валют
 */
@FeignClient(name = "currencyClient", url = "${currencyClient.api_url:}",
        configuration = FeignClientConfig.class)
public interface CurrencyClient {

    /**
     * Метод делает REST HTTP запрос на внешний сервис и получает курсы валют, согласно списку currencyList,
     * за сегодняшний день относительно валюты USD (для возможности указания базовой валюты требуется подписка)
     *
     * @param apiId        идентификатор пользователя
     * @param currencyList список запрашиваемых валют, разделенные через запятую
     * @return экземпляр класса CurrencyResource с результатом запроса
     */
    @GetMapping(value = "latest.json?app_id={apiId}&symbols={currencyList}", produces = "application/json")
    CurrencyResource getLatestCurrencyExchangeRates(
            @PathVariable String apiId,
            @PathVariable String currencyList);

    /**
     * Метод делает REST HTTP запрос на внешний сервис и получает курсы валют, согласно списку currencies,
     * за указанную дату date день относительно валюты USD (для возможности указания базовой валюты требуется подписка)
     *
     * @param apiId        идентификатор пользователя
     * @param currencyList список запрашиваемых валют, разделенные через запятую
     * @param date         дата в формате yyyy-MM-dd
     * @return экземпляр класса CurrencyResource с результатом запроса
     */
    @GetMapping(value = "historical/{date}.json?app_id={apiId}&symbols={currencyList}", produces = "application/json")
    CurrencyResource getHistoricalCurrencyExchangeRates(
            @PathVariable String apiId,
            @PathVariable String currencyList,
            @PathVariable String date);

    /**
     * Метод делает REST HTTP запрос на внешний сервис и получает список валют
     *
     * @return CompletableFuture список валют в виде Map<String, String>
     */
    @GetMapping(value = "currencies.json", produces = "application/json")
    Map<String, String> getCurrenciesList();
}
