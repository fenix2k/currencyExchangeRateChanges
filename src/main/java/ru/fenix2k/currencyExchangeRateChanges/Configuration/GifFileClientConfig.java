package ru.fenix2k.currencyExchangeRateChanges.Configuration;

import feign.Contract;
import feign.Logger;
import feign.codec.ErrorDecoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import ru.fenix2k.currencyExchangeRateChanges.Client.Currency.CurrencyClient;

/**
 * Класс-конфигурация Feign клиентов для GifFileClient
 */
public class GifFileClientConfig {

    /**
     * Контракт
     */
    @Bean
    public Contract gifFileContract() {
        return new SpringMvcContract();
    }

    /**
     * Логер
     */
    @Bean
    public Logger gifFileLogger() {
        return new Slf4jLogger(CurrencyClient.class);
    }

    /**
     * Декодер ошибок
     */
    @Bean
    public ErrorDecoder gifFileErrorDecoder() {
        return new FeignErrorDecoder();
    }

}
