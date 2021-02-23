package ru.fenix2k.currencyExchangeRateChanges.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fenix2k.currencyExchangeRateChanges.Client.Currency.CurrencyResource;

import java.util.Map;

@RestController
@RequestMapping("/fakeapi/")
@Slf4j
public class Test {

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

    @GetMapping("latest.json")
    public ResponseEntity<CurrencyResource> getFakeLatest() {
        log.info("getFakeLatest");
        return ResponseEntity.ok(currencyResponseLatest);
    }

    @GetMapping("historical/*.json")
    public ResponseEntity<CurrencyResource> getFakeHistorical() {
        log.info("getFakeHistorical");
        return ResponseEntity.ok(currencyResponseHistorical);
    }

    @GetMapping("currencies.json")
    public ResponseEntity<Map<String, String>> getFakeCurrencies() {
        log.info("getFakeCurrencies");
        return ResponseEntity.ok(currencies);
    }

}
