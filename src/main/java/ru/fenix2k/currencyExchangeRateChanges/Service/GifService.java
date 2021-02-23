package ru.fenix2k.currencyExchangeRateChanges.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.fenix2k.currencyExchangeRateChanges.Client.Gif.GifClient;
import ru.fenix2k.currencyExchangeRateChanges.Client.Gif.GifFileClient;
import ru.fenix2k.currencyExchangeRateChanges.Client.Gif.GifResource;
import ru.fenix2k.currencyExchangeRateChanges.Configuration.AppProperties;

import java.net.URI;

/**
 * Сервисный класс для получения GIF из внешних источников
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GifService {

    /**
     * Содержит настройки приложения
     */
    private final AppProperties appProperties;
    /**
     * Клиент для взаимодействия с внещним сервисом
     */
    private final GifClient client;
    private final GifFileClient fileClient;

    /**
     * Метод отправляет REST HTTP запрос на внешний сервис и получает случайную ссылку на GIF файл,
     * который соответсвует тэгу tag
     *
     * @param tag тэг-фильтр запрашиваемого ресурса
     * @return сслыка на GIF файл в виде объекта класса GifResource
     */
    public GifResource getRandomGifUrl(String tag) {
        return client.getRandomGifUrl(appProperties.getGifClientApiId(), tag);
    }

    /**
     * Метод загружает файл с внешнего ресурса
     *
     * @param url URL адрес файла
     * @return массив byte
     */
    public byte[] getGif(String url) {
        URI uri = URI.create(url);
        return fileClient.getGif(uri);
    }

}
