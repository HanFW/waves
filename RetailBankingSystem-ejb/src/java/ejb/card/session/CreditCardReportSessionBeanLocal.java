package ejb.card.session;

import ejb.card.entity.CreditCardReport;
import javax.ejb.Local;

@Local
public interface CreditCardReportSessionBeanLocal {

    public Long addNewCreditCardReport(String statementDate, String cardType,
            Long startTime, Long endTime, Long cardId);

    public void generateMonthlyCreditCardReport();

    public CreditCardReport retrieveCreditCardReportById(Long creditCardReportId);
}
