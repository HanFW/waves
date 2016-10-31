package ejb.bi.session;

import ejb.bi.entity.Rate;
import javax.ejb.Local;

@Local
public interface RateSessionBeanLocal {

    public Long addNewRate(Double rateValue, int updateDate, String rateType,
            String openAccountNumOfPeople, String closeAccountNumOfPeople);

    public Rate retrieveAcquisitionRateByDate(int updateDate);

    public Rate retrieveAttritionRateByDate(int updateDate);
}
