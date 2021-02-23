package ru.fenix2k.currencyExchangeRateChanges.Client.Gif;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Класс для маппинга ответа внешнего GIF сервиса
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GifResource {

    /**
     * URL ссылка на файл
     */
    private String image_url;

}
