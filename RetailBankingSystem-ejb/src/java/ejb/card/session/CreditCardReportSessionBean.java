package ejb.card.session;

import ejb.card.entity.CreditCardReport;
import ejb.card.entity.PrincipalCard;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CreditCardReportSessionBean implements CreditCardReportSessionBeanLocal {

    @EJB
    private PrincipalCardSessionBeanLocal principalCardSessionBeanLocal;

    @EJB
    private CreditCardSessionBeanLocal creditCardSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewCreditCardReport(String statementDate, String cardType,
            Long startTime, Long endTime, Long cardId) {

        CreditCardReport creditCardReport = new CreditCardReport();

        creditCardReport.setCardType(cardType);
        creditCardReport.setStatementDate(statementDate);
        creditCardReport.setEndTime(endTime);
        creditCardReport.setStartTime(startTime);
        creditCardReport.setPrincipalCard(principalCardSessionBeanLocal.retrieveCardById(cardId));

        entityManager.persist(creditCardReport);
        entityManager.flush();

        return creditCardReport.getCreditCardReportId();
    }

    @Override
    public CreditCardReport retrieveCreditCardReportById(Long creditCardReportId) {
        CreditCardReport creditCardReport = new CreditCardReport();

        try {
            Query query = entityManager.createQuery("Select c From CreditCardReport c Where c.creditCardReportId=:creditCardReportId");
            query.setParameter("creditCardReportId", creditCardReportId);

            if (query.getResultList().isEmpty()) {
                return new CreditCardReport();
            } else {
                creditCardReport = (CreditCardReport) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new CreditCardReport();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return creditCardReport;
    }

    @Override
    public void generateMonthlyCreditCardReport() {
        List<PrincipalCard> principalCards = creditCardSessionBeanLocal.getAllActivatedCreditCards();

        for (PrincipalCard principalCard : principalCards) {

            Calendar cal = Calendar.getInstance();

            String cardType = principalCard.getCardType();
            String statementDate = cal.getTime().toString();
            Long startTime = cal.getTimeInMillis() - 300010;
            Long endTime = cal.getTimeInMillis();

            System.out.println(startTime);
            System.out.println(endTime);
            Long newCreditReportId = addNewCreditCardReport(statementDate,
                    cardType, startTime, endTime, principalCard.getCardId());
        }
    }
}
