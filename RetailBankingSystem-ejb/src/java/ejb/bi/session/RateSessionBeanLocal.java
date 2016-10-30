package ejb.bi.session;

import ejb.bi.entity.Rate;
import javax.ejb.Local;

@Local
public interface RateSessionBeanLocal {

    public Long addNewRate(Double rateValue, String updateDate, String rateType);

    public Rate retrieveAcquisitionRateByDate(String updateDate);

    public Rate retrieveAttritionRateByDate(String updateDate);
}
