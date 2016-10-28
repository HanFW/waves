package ejb.payment.session;

import ejb.payment.entity.Currency;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless

public class CurrencySessionBean implements CurrencySessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Currency retrieveCurrencyByType(String currencyType) {
        Currency currency = new Currency();

        try {
            Query query = entityManager.createQuery("Select c From Currency c Where c.currencyType=:currencyType");
            query.setParameter("currencyType", currencyType);

            if (query.getResultList().isEmpty()) {
                return new Currency();
            } else {
                currency = (Currency) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new Currency();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return currency;
    }
    
    @Override
    public void updateCurrencyRate(String currencyType, String buyingRate, String sellingRate) {
        Currency currency = retrieveCurrencyByType(currencyType);
        currency.setBuyingRate(buyingRate);
        currency.setSellingRate(sellingRate);
    }
}
