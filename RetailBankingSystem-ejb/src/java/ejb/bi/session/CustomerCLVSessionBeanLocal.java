package ejb.bi.session;

import ejb.bi.entity.CustomerCLV;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CustomerCLVSessionBeanLocal {

    public Long addNewCustomerCLV(String loanInterest, String depositInterest, Integer updateYear,
            Integer updateMonth, String clvSegment, String clvValue, Long customerBasicId);

    public void generateMonthlyCustomerCLV();

    public List<CustomerCLV> retrieveCustomerCLVByLevel(String customerSegment);
}
