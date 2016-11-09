package ejb.bi.session;

import ejb.bi.entity.Rate;
import java.util.List;
import javax.ejb.Local;

@Local
public interface RateSessionBeanLocal {

    public Long addNewRate(Double rateValue, String rateType, String rateStatus,
            Integer updateYear, Integer updateMonth, String currentYear);

    public Rate retrieveAcquisitionRateByMonth(Integer updateMonth);

    public Rate retrieveAttritionRateByMonth(Integer updateMonth);

    public Rate retrieveAcquisitionRateById(Long rateId);

    public Rate retrieveAttritionRateById(Long rateId);

    public void monthlyDashboardRate();

    public Rate getAcqRate();

    public Rate retrieveRateById(Long rateId);

    public Rate getAttRate();

    public List<Rate> getCurrentYearAcqRate();

    public List<Rate> getCurrentYearAttRate();
    
    public void generateMonthlyAccountClosureReason();
}
