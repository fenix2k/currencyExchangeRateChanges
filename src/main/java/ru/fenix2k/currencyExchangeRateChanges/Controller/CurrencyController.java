package ru.fenix2k.currencyExchangeRateChanges.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.fenix2k.currencyExchangeRateChanges.Service.CurrencyService;

import java.util.concurrent.TimeoutException;

/**
 * Контроллер
 */
@Controller
@RequestMapping("/")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    /**
     * Метод рендерит главную страницу и выдёт клиенту
     *
     * @param model {@link Model}
     * @return HTML страница
     */
    @GetMapping("")
    public String getMainPage(Model model) {
        model.addAttribute("currencies", currencyService.getCurrencies());
        return "main";
    }

}
