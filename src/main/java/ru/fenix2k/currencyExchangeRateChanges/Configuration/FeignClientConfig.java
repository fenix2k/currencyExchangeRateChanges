package ru.fenix2k.currencyExchangeRateChanges.Configuration;

import feign.Contract;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import ru.fenix2k.currencyExchangeRateChanges.Client.Currency.CurrencyClient;

/**
 * Класс-конфигурация Feign клиентов (общий)
 */
public class FeignClientConfig {

    /**
     * Контракт
     */
    @Bean
    public Contract feignContract() {
        return new SpringMvcContract();
    }

    /**
     * Логер
     */
    @Bean
    public Logger feignLogger() {
        return new Slf4jLogger(CurrencyClient.class);
    }

    /**
     * Декодер ошибок
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    /**
     * Декодер ответа
     */
    @Bean
    public Decoder feignDecoder() {
        return new GsonDecoder();
    }

}
