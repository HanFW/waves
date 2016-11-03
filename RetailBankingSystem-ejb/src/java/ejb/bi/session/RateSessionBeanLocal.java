package ejb.bi.session;

import ejb.bi.entity.Rate;
import javax.ejb.Local;

@Local
public interface RateSessionBeanLocal {

    public Long addNewRate(Double rateValue, Integer updateDate, String rateType,
            String rateStatus);

    public Rate retrieveAcquisitionRateByDate(Integer updateDate);

    public Rate retrieveAttritionRateByDate(Integer updateDate);

    public Rate retrieveAcquisitionRateByDate(Long rateId);

    public Rate retrieveAttritionRateById(Long rateId);

    public void monthlyDashboardRate();

    public Rate getAcqRate();

    public Rate retrieveRateById(Long rateId);

    public Rate getAttRate();
}
