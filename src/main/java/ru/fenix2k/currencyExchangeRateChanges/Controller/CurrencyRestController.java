package ru.fenix2k.currencyExchangeRateChanges.Controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fenix2k.currencyExchangeRateChanges.Client.Currency.CurrencyResource;
import ru.fenix2k.currencyExchangeRateChanges.Client.Gif.GifResource;
import ru.fenix2k.currencyExchangeRateChanges.Configuration.AppProperties;
import ru.fenix2k.currencyExchangeRateChanges.Exception.CurrencyNotFoundException;
import ru.fenix2k.currencyExchangeRateChanges.Service.CurrencyService;
import ru.fenix2k.currencyExchangeRateChanges.Service.GifService;

import java.util.Date;
import java.util.Map;

/**
 * REST контроллер для получения информации о курсах валют
 */
@RestController
@RequestMapping(path = "/api", produces = "application/json")
public class CurrencyRestController {

    /**
     * Сервис для работы получения информации о курсах валют
     */
    private final CurrencyService currencyService;
    /**
     * Сервис для получения GIF
     */
    private final GifService gifService;
    /**
     * Содержит настройки приложения
     */
    private final AppProperties appProperties;

    public CurrencyRestController(CurrencyService currencyService, GifService gifService, AppProperties appProperties) {
        this.currencyService = currencyService;
        this.gifService = gifService;
        this.appProperties = appProperties;
    }

    /**
     * Метод делает REST HTTP запрос на внешний сервис и получает список валют
     *
     * @return список валют в виде Map<String, String>
     */
    @GetMapping("currencies")
    public ResponseEntity<Map<String, String>> getCurrencies() {
        return ResponseEntity.ok(currencyService.getCurrencies());
    }

    /**
     * Метод получает курсы валют на сегодняшний день относительно USD
     *
     * @return информация о курсах валют в формате JSON
     */
    @GetMapping("current")
    public ResponseEntity<CurrencyResource> getLatestCurrencyExchangeRate() {
        CurrencyResource resource = currencyService.getTodayCurrencyExchangeRates("");
        return ResponseEntity.ok(resource);
    }

    /**
     * Метод получает курсы валют за вчерашний день относительно USD
     *
     * @return информация о курсах валют в формате JSON
     */
    @GetMapping("yesterday")
    public ResponseEntity<CurrencyResource> getYesterdayCurrencyExchangeRate() {
        Date date = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        CurrencyResource resource = currencyService.getHistoricalCurrencyExchangeRates("", date);
        return ResponseEntity.ok(resource);
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
    @GetMapping("compare/{currencyName}")
    public ResponseEntity<Integer> compareCurrencyExchangeRate(@PathVariable("currencyName") String currencyName)
            throws CurrencyNotFoundException {
        int result = currencyService.compareCurrencyExchangeRates(currencyName);
        return ResponseEntity.ok(result);
    }

    /**
     * Метод сравнивает курсы базовой валюты baseCurrency и currencyName за текуший и вчерашний дни.
     * Если курс по отношению к базовой валюте за текущий день стал выше вчерашнего,
     * то возвращается URL ссылка на случайный GIF с внешнего сервиса в формате JSON с тэгом "rich",
     * если меньше, то URL ссылка на случайный GIF с внешнего сервиса в формате JSON с тэгом "broke",
     * если не изменился то пустой объект.
     *
     * @param currencyName имя валюты для сравнения с базовой
     * @return URL ссылка на GIF в формате JSON
     * @throws {@link CurrencyNotFoundException} выбрасывается при отсутсвии указанной пользователем валюты в ответе
     */
    @GetMapping("compareAndGetGifUrl/{currencyName}")
    public ResponseEntity<GifResource> gifUrlCompareCurrencyExchangeRate(@PathVariable("currencyName") String currencyName)
            throws CurrencyNotFoundException {

        int compare = currencyService.compareCurrencyExchangeRates(currencyName);

        GifResource result = new GifResource();
        if (compare > 0)
            result = gifService.getRandomGifUrl(appProperties.getGifClientIncreaseSearchTag());
        else if (compare < 0)
            result = gifService.getRandomGifUrl(appProperties.getGifClientDecreaseSearchTag());

        return ResponseEntity.ok(result);
    }

    /**
     * Метод сравнивает курсы базовой валюты baseCurrency и currencyName за текуший и вчерашний дни.
     * Если курс по отношению к базовой валюте за текущий день стал выше вчерашнего,
     * то возвращается случайный файл GIF с внешнего сервиса с тэгом "rich",
     * если меньше, то случайный файл GIF с внешнего сервиса с тэгом "broke",
     * если не изменился то пустой объект.
     *
     * @param currencyName имя валюты для сравнения с базовой
     * @return файл GIF в виде массива byte
     * @throws {@link CurrencyNotFoundException} выбрасывается при отсутсвии указанной пользователем валюты в ответе
     */
    @GetMapping({"compareAndGetGif/{currencyName}", "start/{currencyName}"})
    public ResponseEntity<byte[]> gifCompareCurrencyExchangeRate(@PathVariable("currencyName") String currencyName)
            throws CurrencyNotFoundException {

        int compare = currencyService.compareCurrencyExchangeRates(currencyName);

        GifResource gifResource = null;
        if (compare > 0)
            gifResource = gifService.getRandomGifUrl(appProperties.getGifClientIncreaseSearchTag());
        else if (compare < 0)
            gifResource = gifService.getRandomGifUrl(appProperties.getGifClientDecreaseSearchTag());

        if (gifResource == null)
            return ResponseEntity.ok(null);

        byte[] result = gifService.getGif(gifResource.getImage_url());

        return ResponseEntity.ok()
                .contentLength(result.length)
                .contentType(MediaType.IMAGE_GIF)
                .body(result);
    }

}
