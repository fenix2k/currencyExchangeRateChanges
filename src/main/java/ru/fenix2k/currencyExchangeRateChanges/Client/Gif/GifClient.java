package ru.fenix2k.currencyExchangeRateChanges.Client.Gif;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.fenix2k.currencyExchangeRateChanges.Configuration.FeignClientConfig;
import ru.fenix2k.currencyExchangeRateChanges.Configuration.GifClientConfig;

/**
 * Описание Feign клиента для взаимодействия с внешним Gif сервисом
 */
@FeignClient(name = "gifClient", url = "${gifClient.api_url:}",
        configuration = {FeignClientConfig.class, GifClientConfig.class})
public interface GifClient {

    /**
     * Метод отправляет REST HTTP запрос на внешний сервис и получает случайную ссылку на GIF файл,
     * который соответсвует тэгу tag
     *
     * @param apiKey идентификатор пользоваателя
     * @param tag    тэг-фильтр запрашиваемого ресурса
     * @return сслыка на GIF файл в виде объекта класса {@link GifResource}
     */
    @GetMapping(value = "random?api_key={apiKey}&tag={tag}&rating=g", produces = "application/json")
    GifResource getRandomGifUrl(@PathVariable String apiKey, @PathVariable String tag);

}
