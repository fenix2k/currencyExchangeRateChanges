package ru.fenix2k.currencyExchangeRateChanges.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import feign.Response;
import feign.gson.GsonDecoder;
import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.nio.charset.Charset;

/**
 * Класс-конфигурация Feign клиентов для GifClient
 */
public class GifClientConfig {

    /**
     * Добавляет обработчик ответа
     *
     * @return {@link FeignBuilderCustomizer}
     */
    @Bean
    public FeignBuilderCustomizer feignBuilderCustomizer() {
        return builder -> builder.mapAndDecode((response, type) -> jsopUnwrap(response), new GsonDecoder());
    }

    /**
     * Метод преобразовывает полученный от внешнего сервиса ответ в нужное представление.
     *
     * @param response ответ от внешнего сервиса
     * @return изменённый response
     */
    private Response jsopUnwrap(Response response) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(response.body().toString());
            JsonNode idNode = rootNode.get("data").get("image_original_url");

            ObjectNode newRootNode = objectMapper.createObjectNode();
            newRootNode.put("image_url", idNode.asText());

            response = response.toBuilder().body(String.valueOf(newRootNode), Charset.defaultCharset()).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return response;
    }

}
