package ejb.payment.session;

import ejb.payment.entity.Currency;
import javax.ejb.Local;

@Local
public interface CurrencySessionBeanLocal {
    public Currency retrieveCurrencyByType(String currencyType);
    public void updateCurrencyRate(String currencyType, String buyingRate, String sellingRate);
}
