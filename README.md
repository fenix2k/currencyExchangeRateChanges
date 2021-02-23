# Currency Exchange Rate Changes

Сервис позволяет узнать соотношение курсов рубля (RUB) и выбранной валюты за текуший и вчерашний дни.
Если курс выбранной вылюты по отношению к рублю за сегодня стал выше вчерашнего, 
то сервис вернёт случайную GIF c https://giphy.com/search/rich,
если ниже, то вернёт случайную GIF c https://giphy.com/search/broke

Для получения курсов валют используется сервис https://openexchangerates.org/\
Для получения картинок https://giphy.com/

Сервис построен на:
 * Java 11
 * Spring Boot 2
 * Gradle - система сборки
 * Feign - для взаимодействия с внешними сервисами
 * Mockito - unit-тестирование


### Сборка и запуск
```
git clone https://github.com/fenix2k/currencyExchangeRateChanges.git

# Запуск тестов
gradle test --info

# Запуск сервиса
gradle bootRun
```

### Запуск в Docker
```
./docker-run.sh
```

### Использование
```
# Веб страница с интерфейсом 
http://localhost:8080/

или

# REST HTTP GET запрос
http://localhost:8080/start/<имя валюты>
```

### REST API
Возращает ответ в виде JSON
```
# Получение списка валют
/api/currencies

# Получение курсов валют за сегодняшний день
/api/current

# Получение курсов валют за вчерашний день
/api/yesterday

# Сравнивает курсы базовой валюты baseCurrency и currencyName за текуший и вчерашний дни.
# Если курс по отношению к базовой валюте за текущий день стал выше вчерашнего, то возвращается 1,
# если меньше, то -1, если не изменился то 0.
/api/compare/{currencyName}

# Сравнивает курсы базовой валюты baseCurrency и currencyName за текуший и вчерашний дни.
# Если курс по отношению к базовой валюте за текущий день стал выше вчерашнего,
# то возвращается URL ссылка на случайный GIF с внешнего сервиса в формате JSON с тэгом "rich",
# если меньше, то URL ссылка на случайный GIF с внешнего сервиса в формате JSON с тэгом "broke",
# если не изменился то пустой объект.
/api/compareAndGetGifUrl/{currencyName}
```

### Настройки
Конфигурация расположена в файле application.yaml
```
# Настройки клиента для взаимодействия с сервисом курсов валют
currencyClient:
    # API url    
    api_url: https://openexchangerates.org/api/
    # User ID
    api_id: 10a27244eebc4ac89df6ab88fad13ede
    # Базовая валюта
    base_currency: RUB
    # Базовая валюта если нет подписки 
    base_currency_freeacc: USD

# Настройки клиента для взаимодействия с GIF сервисом
gifClient:
    # API url  
    api_url: https://api.giphy.com/v1/gifs/
    # User ID
    api_id: Ty1QaL42rUfjmACg6fQGzNQWBVRI22tE
    # Тэг для поиска картинки если курс указанной валюты вырос
    increase_search_tag: rich
    # Тэг для поиска картинки если курс указанной валюты упал
    decrease_search_tag: broke
```
