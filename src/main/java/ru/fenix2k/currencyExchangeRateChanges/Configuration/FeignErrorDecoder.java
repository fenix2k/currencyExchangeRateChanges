package ru.fenix2k.currencyExchangeRateChanges.Configuration;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Реализация декодера ошибок Feign клиента
 */
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad external service request");
    }
}
