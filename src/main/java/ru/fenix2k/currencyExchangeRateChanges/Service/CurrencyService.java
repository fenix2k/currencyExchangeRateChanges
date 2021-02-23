package ru.fenix2k.currencyExchangeRateChanges.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.fenix2k.currencyExchangeRateChanges.Client.Currency.CurrencyClient;
import ru.fenix2k.currencyExchangeRateChanges.Client.Currency.CurrencyResource;
import ru.fenix2k.currencyExchangeRateChanges.Configuration.AppProperties;
import ru.fenix2k.currencyExchangeRateChanges.Exception.CurrencyNotFoundException;

import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Сервисный класс для получения информации о курсах валют из внешних источников
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyService {

    /**
     * Содержит настройки приложения
     */
    private final AppProperties appProperties;
    /**
     * Клиент для взаимодействия с внещним сервисом
     */
    private final CurrencyClient client;

    /**
     * Метод делает REST HTTP запрос на внешний сервис и получает список валют
     *
     * @return список валют в виде Map<String, String>
     */
    public Map<String, String> getCurrencies() {
        return client.getCurrenciesList();
    }

    /**
     * Метод делает REST HTTP запрос на внешний сервис и получает курсы валют, согласно списку currencies,
     * за сегодняшний день относительно валюты USD (для возможности указания базовой валюты требуется подписка)
     *
     * @param currencies список запрашиваемых валют, разделенные через запятую
     * @return экземпляр класса {@link CurrencyResource} с результатом запроса
     */
    public CurrencyResource getTodayCurrencyExchangeRates(String currencies) {
        return client.getLatestCurrencyExchangeRates(appProperties.getCurrencyClientApiId(), currencies);
    }

    /**
     * Метод делает REST HTTP запрос на внешний сервис и получает курсы валют, согласно списку currencies,
     * за вречершний день относительно валюты USD (для возможности указания базовой валюты требуется подписка)
     *
     * @param currencies список запрашиваемых валют, разделенные через запятую
     * @return экземпляр класса {@link CurrencyResource} с результатом запроса
     */
    public CurrencyResource getHistoricalCurrencyExchangeRates(String currencies, Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return client.getHistoricalCurrencyExchangeRates(
                appProperties.getCurrencyClientApiId(),
                currencies,
                dateFormat.format(date));
    }

    /**
     * Метод сравнивает курсы базовой валюты baseCurrency и currencyName за текуший и вчерашний дни.
     * Если курс по отношению к базовой валюте за текущий день стал выше вчерашнего, то возвращается 1,
     * если меньше, то -1, если не изменился то 0.
     *
     * @param currencyName имя валюты для сравнения с базовой
     * @return результат сравнения (-1, 0, 1)
     * @throws {@link CurrencyNotFoundException} выбрасывается при отсутсвии указанной пользователем валюты в ответе
     */
    public int compareCurrencyExchangeRates(String currencyName)
            throws CurrencyNotFoundException {

        if (!currencyName.matches("^([a-zA-z]{3})$"))
            throw new InvalidParameterException("Specified currency string name is invalid");

        // Формирование списка запрашиваемых валют
        Set<String> currencySet = new HashSet<>();
        currencySet.add(appProperties.getCurrencyClientBaseCurrency());
        currencySet.add(appProperties.getCurrencyClientBaseCurrencyFreeAcc());
        currencySet.add(currencyName.toUpperCase());
        // Преобразование спика Set к строке с запятыми в качестве разделителя
        String currencies = currencySet.stream().reduce((res, elem) -> res + "," + elem).orElse("");

        CurrencyResource todayExchangeRate = getTodayCurrencyExchangeRates(currencies);
        if (todayExchangeRate == null || !todayExchangeRate.getRates().containsKey(currencyName))
            throw new CurrencyNotFoundException("Specified currency is not found");

        Date date = new Date((todayExchangeRate.getTimestamp() - 24 * 60 * 60) * 1000);
        CurrencyResource yesterdayExchangeRate = getHistoricalCurrencyExchangeRates(currencies, date);
        if (yesterdayExchangeRate == null || !yesterdayExchangeRate.getRates().containsKey(currencyName))
            throw new CurrencyNotFoundException("Specified currency is not found");

        Double todayRatio = todayExchangeRate.getRates().get(appProperties.getCurrencyClientBaseCurrency())
                / todayExchangeRate.getRates().get(currencyName);
        Double yesterdayRatio = yesterdayExchangeRate.getRates().get(appProperties.getCurrencyClientBaseCurrency())
                / yesterdayExchangeRate.getRates().get(currencyName);

        if (todayRatio > yesterdayRatio)
            return -1;
        if (todayRatio < yesterdayRatio)
            return 1;

        return 0;
    }


}
