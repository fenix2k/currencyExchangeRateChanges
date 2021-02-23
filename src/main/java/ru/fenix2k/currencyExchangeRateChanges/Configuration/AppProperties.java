package ru.fenix2k.currencyExchangeRateChanges.Configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Класс содержит насраиваемые параметры программы.
 * Параметры загружаются из файла application.properties
 */
@Component
@Scope("singleton")
@PropertySource("classpath:application.yaml")
@Getter
@Slf4j
public final class AppProperties {

  /** Базовая валюта если аккаунт без подписки */
  @Value("${currencyClient.base_currency_freeacc:USD}")
  private String currencyClientBaseCurrencyFreeAcc;

  /** Базовая валюта */
  @Value("${currencyClient.base_currency:RUB}")
  private String currencyClientBaseCurrency;

  /** Тэг по которому производиться поиск картинки при росте курса указанной валюты */
  @Value("${gifClient.increase_search_tag:rich}")
  private String gifClientIncreaseSearchTag;

  /** Тэг по которому производиться поиск картинки при понижении курса указанной валюты */
  @Value("${gifClient.decrease_search_tag:broke}")
  private String gifClientDecreaseSearchTag;

  /** URL сервиса курсов валют */
  @Value("${currencyClient.api_url:}")
  private String currencyClientApiUrl;

  /** Идентификатор пользователя сервиса курсов валют */
  @Value("${currencyClient.api_id:}")
  private String currencyClientApiId;

  /** URL сервиса GIF */
  @Value("${gifClient.api_url:}")
  private String gifClientApiUrl;

  /** Идентификатор пользователя сервиса GIF */
  @Value("${gifClient.api_id:}")
  private String gifClientApiId;

  /** Выводит на консоль все свойства экземпляра класса */
  @PostConstruct
  public void writeParamsToConsole() throws IllegalAccessException {
    Field[] fields = this.getClass().getDeclaredFields();
    System.out.printf("Loaded properties:\n");
    for (Field field: fields) {
      System.out.printf("  " + field.getName() + " = " + field.get(this) + "\n");
    }
  }

}
