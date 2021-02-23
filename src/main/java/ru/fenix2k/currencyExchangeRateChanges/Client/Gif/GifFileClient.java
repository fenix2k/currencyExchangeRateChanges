package ru.fenix2k.currencyExchangeRateChanges.Client.Gif;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.fenix2k.currencyExchangeRateChanges.Configuration.GifFileClientConfig;

import java.net.URI;

/**
 * Описание Feign клиента для взаимодействия с внешним Gif сервисом
 */
@FeignClient(name = "gifFileClient", url = "${gifClient.api_url:}",
        configuration = GifFileClientConfig.class)
public interface GifFileClient {

    /**
     * Метод загружает файл с внешнего ресурса
     *
     * @return массив byte
     */
    @GetMapping
    byte[] getGif(URI uri);

}
