package ru.fenix2k.currencyExchangeRateChanges.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.fenix2k.currencyExchangeRateChanges.Client.Currency.CurrencyClient;
import ru.fenix2k.currencyExchangeRateChanges.Client.Currency.CurrencyResource;
import ru.fenix2k.currencyExchangeRateChanges.Client.Gif.GifClient;
import ru.fenix2k.currencyExchangeRateChanges.Client.Gif.GifFileClient;
import ru.fenix2k.currencyExchangeRateChanges.Client.Gif.GifResource;
import ru.fenix2k.currencyExchangeRateChanges.Configuration.AppProperties;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тестирование контроллера {@link CurrencyRestController}
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Slf4j
@RequiredArgsConstructor
class CurrencyRestControllerTest {

    @MockBean
    private CurrencyClient currencyClient;
    @MockBean
    private GifClient gifClient;
    @MockBean
    private GifFileClient gifFileClient;

    @Autowired
    private AppProperties appProperties;
    @Autowired
    private MockMvc mockMvc;


    private static CurrencyResource currencyResponseLatest = new CurrencyResource(
            "Usage subject to terms: https://openexchangerates.org/terms",
            "https://openexchangerates.org/license",
            1613764800,
            "USD",
            Map.of(
                    "QAR", 3.641,
                    "RON", 4.0241,
                    "RSD", 96.856853,
                    "RUB", 74.1016,
                    "RWF", 987.5,
                    "SAR", 3.750382,
                    "BTC", 0.000018187259,
                    "USD", 1.0,
                    "EUR", 0.825381
            )
    );

    private static CurrencyResource currencyResponseHistorical = new CurrencyResource(
            "Usage subject to terms: https://openexchangerates.org/terms",
            "https://openexchangerates.org/license",
            1613779180,
            "USD",
            Map.of(
                    "QAR", 3.641,
                    "RON", 4.024,
                    "RSD", 96.856853,
                    "RUB", 74.035,
                    "RWF", 985.0,
                    "SAR", 3.750537,
                    "BTC", 0.00001790336,
                    "USD", 1.0,
                    "EUR", 0.825226
            )
    );

    private static Map<String, String> currencies = Map.of(
            "QAR", "Qatari Rial",
            "RON", "Romanian Leu",
            "RSD", "Serbian Dinar",
            "RUB", "Russian Ruble",
            "RWF", "Rwandan Franc",
            "SAR", "Saudi Riyal",
            "BTC", "Bitcoin",
            "USD", "United States Dollar"
    );

    private static GifResource richGifResource = new GifResource("https://media2.giphy.com/media/JPgHihvNm7hwQce2uV/giphy.gif");
    private static GifResource brokeGifResource = new GifResource("https://media3.giphy.com/media/lmjmpPI9FouT0YjsAe/giphy.gif");

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getLatestCurrencyExchangeRate() throws Exception {
        Mockito.when(currencyClient.getLatestCurrencyExchangeRates(appProperties.getCurrencyClientApiId(), ""))
                .thenReturn(currencyResponseLatest);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").value(currencyResponseLatest.getTimestamp()))
                .andExpect(jsonPath("$.base").value(currencyResponseLatest.getBase()))
                .andExpect(jsonPath("$.rates.BTC").value(currencyResponseLatest.getRates().get("BTC")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void getYesterdayCurrencyExchangeRate() throws Exception {
        Date date = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Mockito.when(currencyClient.getHistoricalCurrencyExchangeRates(appProperties.getCurrencyClientApiId(),
                "",
                dateFormat.format(date)))
                .thenReturn(currencyResponseHistorical);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/yesterday"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").value(currencyResponseHistorical.getTimestamp()))
                .andExpect(jsonPath("$.base").value(currencyResponseHistorical.getBase()))
                .andExpect(jsonPath("$.rates.BTC").value(currencyResponseHistorical.getRates().get("BTC")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void compareCurrencyExchangeRate() throws Exception {
        Mockito.when(currencyClient.getLatestCurrencyExchangeRates(
                ArgumentMatchers.eq(appProperties.getCurrencyClientApiId()),
                ArgumentMatchers.anyString()))
                .thenReturn(currencyResponseLatest);

        Date date = new Date((currencyResponseLatest.getTimestamp() - 24 * 60 * 60) * 1000);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Mockito.when(currencyClient.getHistoricalCurrencyExchangeRates(
                ArgumentMatchers.eq(appProperties.getCurrencyClientApiId()),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(dateFormat.format(date))))
                .thenReturn(currencyResponseHistorical);

        // Expected result 1
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/compare/BTC"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(1)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        // Expected result 0
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/compare/RUB"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(0)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        // Expected result -1
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/compare/USD"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void gifUrlCompareCurrencyExchangeRate() throws Exception {
        Mockito.when(currencyClient.getLatestCurrencyExchangeRates(
                ArgumentMatchers.eq(appProperties.getCurrencyClientApiId()),
                ArgumentMatchers.anyString()))
                .thenReturn(currencyResponseLatest);

        Date date = new Date((currencyResponseLatest.getTimestamp() - 24 * 60 * 60) * 1000);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Mockito.when(currencyClient.getHistoricalCurrencyExchangeRates(
                ArgumentMatchers.eq(appProperties.getCurrencyClientApiId()),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(dateFormat.format(date))))
                .thenReturn(currencyResponseHistorical);

        Mockito.when(gifClient.getRandomGifUrl(
                appProperties.getGifClientApiId(),
                appProperties.getGifClientIncreaseSearchTag()))
                .thenReturn(richGifResource);

        Mockito.when(gifClient.getRandomGifUrl(
                appProperties.getGifClientApiId(),
                appProperties.getGifClientDecreaseSearchTag()))
                .thenReturn(brokeGifResource);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/compareAndGetGifUrl/BTC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.image_url").value(richGifResource.getImage_url()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/compareAndGetGifUrl/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.image_url").value(brokeGifResource.getImage_url()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void gifCompareCurrencyExchangeRate() throws Exception {
        byte[] bytes = new byte[100];
        new Random().nextBytes(bytes);

        Mockito.when(currencyClient.getLatestCurrencyExchangeRates(
                ArgumentMatchers.eq(appProperties.getCurrencyClientApiId()),
                ArgumentMatchers.anyString()))
                .thenReturn(currencyResponseLatest);

        Date date = new Date((currencyResponseLatest.getTimestamp() - 24 * 60 * 60) * 1000);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Mockito.when(currencyClient.getHistoricalCurrencyExchangeRates(
                ArgumentMatchers.eq(appProperties.getCurrencyClientApiId()),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(dateFormat.format(date))))
                .thenReturn(currencyResponseHistorical);

        Mockito.when(gifClient.getRandomGifUrl(
                appProperties.getGifClientApiId(),
                appProperties.getGifClientIncreaseSearchTag()))
                .thenReturn(richGifResource);

        Mockito.when(gifFileClient.getGif(new URI(richGifResource.getImage_url())))
                .thenReturn(bytes);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/compareAndGetGif/BTC"))
                .andExpect(status().isOk())
                //.andExpect(MockMvcResultMatchers.content().bytes(bytes))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void getCurrencies() throws Exception {
        Mockito.when(currencyClient.getCurrenciesList())
                .thenReturn(currencies);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.QAR").value(currencies.get("QAR")))
                .andExpect(jsonPath("$.RUB").value(currencies.get("RUB")))
                .andExpect(jsonPath("$.BTC").value(currencies.get("BTC")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}