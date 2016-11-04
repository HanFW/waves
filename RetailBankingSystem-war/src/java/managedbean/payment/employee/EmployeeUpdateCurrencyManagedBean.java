package managedbean.payment.employee;

import ejb.payment.entity.Currency;
import ejb.payment.session.CurrencySessionBeanLocal;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "employeeUpdateCurrencyManagedBean")
@RequestScoped

public class EmployeeUpdateCurrencyManagedBean {

    @EJB
    private CurrencySessionBeanLocal currencySessionBeanLocal;

    private String usdSellingRate;
    private String usdBuyingRate;
    private String usdUnit;
    private String jpyBuyingRate;
    private String jpySellingRate;
    private String jpyUnit;
    private String euroBuyingRate;
    private String euroSellingRate;
    private String euroUnit;
    private String krwBuyingRate;
    private String krwSellingRate;
    private String krwUnit;
    private String cnyBuyingRate;
    private String cnySellingRate;
    private String cnyUnit;
    private String audBuyingRate;
    private String audSellingRate;
    private String audUnit;

    private ExternalContext ec;

    public EmployeeUpdateCurrencyManagedBean() {
    }

    public String getUsdSellingRate() {
        Currency usd = currencySessionBeanLocal.retrieveCurrencyByType("United States");
        usdSellingRate = usd.getSellingRate();

        return usdSellingRate;
    }

    public void setUsdSellingRate(String usdSellingRate) {
        this.usdSellingRate = usdSellingRate;
    }

    public String getUsdBuyingRate() {
        Currency usd = currencySessionBeanLocal.retrieveCurrencyByType("United States");
        usdBuyingRate = usd.getBuyingRate();

        return usdBuyingRate;
    }

    public void setUsdBuyingRate(String usdBuyingRate) {
        this.usdBuyingRate = usdBuyingRate;
    }

    public String getJpyBuyingRate() {
        Currency japanese = currencySessionBeanLocal.retrieveCurrencyByType("Japanese");
        jpyBuyingRate = japanese.getBuyingRate();

        return jpyBuyingRate;
    }

    public void setJpyBuyingRate(String jpyBuyingRate) {
        this.jpyBuyingRate = jpyBuyingRate;
    }

    public String getJpySellingRate() {
        Currency japanese = currencySessionBeanLocal.retrieveCurrencyByType("Japanese");
        jpySellingRate = japanese.getSellingRate();

        return jpySellingRate;
    }

    public void setJpySellingRate(String jpySellingRate) {
        this.jpySellingRate = jpySellingRate;
    }

    public String getEuroBuyingRate() {
        Currency euro = currencySessionBeanLocal.retrieveCurrencyByType("Euro");
        euroBuyingRate = euro.getBuyingRate();

        return euroBuyingRate;
    }

    public void setEuroBuyingRate(String euroBuyingRate) {
        this.euroBuyingRate = euroBuyingRate;
    }

    public String getEuroSellingRate() {
        Currency euro = currencySessionBeanLocal.retrieveCurrencyByType("Euro");
        euroSellingRate = euro.getSellingRate();

        return euroSellingRate;
    }

    public void setEuroSellingRate(String euroSellingRate) {
        this.euroSellingRate = euroSellingRate;
    }

    public String getKrwBuyingRate() {
        Currency korean = currencySessionBeanLocal.retrieveCurrencyByType("South Korean");
        krwBuyingRate = korean.getBuyingRate();

        return krwBuyingRate;
    }

    public void setKrwBuyingRate(String krwBuyingRate) {
        this.krwBuyingRate = krwBuyingRate;
    }

    public String getKrwSellingRate() {
        Currency korean = currencySessionBeanLocal.retrieveCurrencyByType("South Korean");
        krwSellingRate = korean.getSellingRate();

        return krwSellingRate;
    }

    public void setKrwSellingRate(String krwSellingRate) {
        this.krwSellingRate = krwSellingRate;
    }

    public String getCnyBuyingRate() {
        Currency chinese = currencySessionBeanLocal.retrieveCurrencyByType("Chinese");
        cnyBuyingRate = chinese.getBuyingRate();

        return cnyBuyingRate;
    }

    public void setCnyBuyingRate(String cnyBuyingRate) {
        this.cnyBuyingRate = cnyBuyingRate;
    }

    public String getCnySellingRate() {
        Currency chinese = currencySessionBeanLocal.retrieveCurrencyByType("Chinese");
        cnySellingRate = chinese.getSellingRate();

        return cnySellingRate;
    }

    public void setCnySellingRate(String cnySellingRate) {
        this.cnySellingRate = cnySellingRate;
    }

    public String getAudBuyingRate() {
        Currency aud = currencySessionBeanLocal.retrieveCurrencyByType("Australian");
        audBuyingRate = aud.getBuyingRate();

        return audBuyingRate;
    }

    public void setAudBuyingRate(String audBuyingRate) {
        this.audBuyingRate = audBuyingRate;
    }

    public String getAudSellingRate() {
        Currency aud = currencySessionBeanLocal.retrieveCurrencyByType("Australian");
        audSellingRate = aud.getSellingRate();

        return audSellingRate;
    }

    public String getUsdUnit() {
        Currency usd = currencySessionBeanLocal.retrieveCurrencyByType("United States");
        usdUnit = usd.getUnit();

        return usdUnit;
    }

    public void setUsdUnit(String usdUnit) {
        this.usdUnit = usdUnit;
    }

    public String getJpyUnit() {
        Currency japanese = currencySessionBeanLocal.retrieveCurrencyByType("Japanese");
        jpyUnit = japanese.getUnit();

        return jpyUnit;
    }

    public void setJpyUnit(String jpyUnit) {
        this.jpyUnit = jpyUnit;
    }

    public String getEuroUnit() {
        Currency euro = currencySessionBeanLocal.retrieveCurrencyByType("Euro");
        euroUnit = euro.getUnit();

        return euroUnit;
    }

    public void setEuroUnit(String euroUnit) {
        this.euroUnit = euroUnit;
    }

    public String getKrwUnit() {
        Currency korean = currencySessionBeanLocal.retrieveCurrencyByType("South Korean");
        krwUnit = korean.getUnit();

        return krwUnit;
    }

    public void setKrwUnit(String krwUnit) {
        this.krwUnit = krwUnit;
    }

    public String getCnyUnit() {
        Currency chinese = currencySessionBeanLocal.retrieveCurrencyByType("Chinese");
        cnyUnit = chinese.getUnit();

        return cnyUnit;
    }

    public void setCnyUnit(String cnyUnit) {
        this.cnyUnit = cnyUnit;
    }

    public String getAudUnit() {
        Currency aud = currencySessionBeanLocal.retrieveCurrencyByType("Australian");
        audUnit = aud.getUnit();

        return audUnit;
    }

    public void setAudUnit(String audUnit) {
        this.audUnit = audUnit;
    }

    public void setAudSellingRate(String audSellingRate) {
        this.audSellingRate = audSellingRate;
    }

    public void submit() {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        currencySessionBeanLocal.updateCurrencyRate("United States", usdBuyingRate, usdSellingRate);
        currencySessionBeanLocal.updateCurrencyRate("Japanese", jpyBuyingRate, jpySellingRate);
        currencySessionBeanLocal.updateCurrencyRate("Euro", euroBuyingRate, euroSellingRate);
        currencySessionBeanLocal.updateCurrencyRate("South Korean", krwBuyingRate, krwSellingRate);
        currencySessionBeanLocal.updateCurrencyRate("Chinese", cnyBuyingRate, cnySellingRate);
        currencySessionBeanLocal.updateCurrencyRate("Australian", audBuyingRate, audSellingRate);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfully! Recipient deleted Successfully.", "Successfuly!"));
    }
}
