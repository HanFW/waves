package ejb.bi.session;

import ejb.bi.entity.CustomerCLV;
import ejb.bi.entity.Rate;
import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.entity.Interest;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.loan.entity.LoanApplication;
import ejb.loan.entity.LoanPayableAccount;
import ejb.loan.entity.LoanRepaymentTransaction;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CustomerCLVSessionBean implements CustomerCLVSessionBeanLocal {

    @EJB
    private RateSessionBeanLocal rateSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewCustomerCLV(String loanInterest, String depositInterest, Integer updateYear,
            Integer updateMonth, String clvSegment, String clvValue, Long customerBasicId) {
        CustomerCLV customerCLV = new CustomerCLV();

        customerCLV.setClvSegment(clvSegment);
        customerCLV.setClvValue(clvValue);
        customerCLV.setDepositInterest(depositInterest);
        customerCLV.setLoanInterest(loanInterest);
        customerCLV.setUpdateMonth(updateMonth);
        customerCLV.setUpdateYear(updateYear);
        customerCLV.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));

        entityManager.persist(customerCLV);
        entityManager.flush();

        return customerCLV.getCustomerCLVId();
    }

    @Override
    public void generateMonthlyCustomerCLV() {

        Query query = entityManager.createQuery("SELECT c FROM CustomerBasic c");
        List<CustomerBasic> allCustomerBasic = query.getResultList();
        Double totalDepositInterest = 0.0;
        Double totalLoanInterest = 0.0;
        Double customerCLV = 0.0;

        Calendar cal = Calendar.getInstance();
        Long endTime = cal.getTimeInMillis();
        Long startTime = cal.getTimeInMillis() - 300000;

        for (CustomerBasic customerBasic : allCustomerBasic) {
            List<BankAccount> bankAccount = customerBasic.getBankAccount();
            List<LoanApplication> loanApplication = customerBasic.getLoanApplication();
            List<LoanRepaymentTransaction> loanRepaymentTransaction = new ArrayList();

            if (!bankAccount.isEmpty()) {
                for (int i = 0; i < bankAccount.size(); i++) {
                    Interest interest = bankAccount.get(i).getInterest();
                    totalDepositInterest = totalDepositInterest + Double.valueOf(interest.getMonthlyInterest());
                }
            }

            if (!loanApplication.isEmpty()) {
                for (int a = 0; a < loanApplication.size(); a++) {
                    LoanPayableAccount loanPayableAccount = loanApplication.get(a).getLoanPayableAccount();

                    Query transactionQuery = entityManager.createQuery("SELECT l FROM LoanRepaymentTransaction l WHERE l.transactionMillis >= :startTime And l.transactionMillis<=:endTime And l.loanPayableAccount=:loanPayableAccount And l.description=:description");
                    transactionQuery.setParameter("startTime", startTime);
                    transactionQuery.setParameter("endTime", endTime);
                    transactionQuery.setParameter("loanPayableAccount", loanPayableAccount);
                    transactionQuery.setParameter("description", "Interest Payment");

                    loanRepaymentTransaction = transactionQuery.getResultList();

                    for (int b = 0; b < loanRepaymentTransaction.size(); b++) {
                        LoanRepaymentTransaction loanRepayment = loanRepaymentTransaction.get(b);
                        totalLoanInterest = totalLoanInterest + loanRepayment.getAccountCredit();
                    }
                }
            }

            customerCLV = totalLoanInterest - totalDepositInterest;
            Rate rate = rateSessionBeanLocal.getAcqRate();
            Integer updateMonth = rate.getUpdateMonth();
            Integer updateYear = rate.getUpdateYear();
            String customerSegment = checkCustomerSegment(customerCLV);

            Long newCustomerCLV = addNewCustomerCLV(totalLoanInterest.toString(), totalDepositInterest.toString(),
                    updateYear, updateMonth, customerSegment, customerCLV.toString(), customerBasic.getCustomerBasicId());
        }
    }

    @Override
    public List<CustomerCLV> retrieveCustomerCLVByLevel(String customerSegment) {
        List<CustomerCLV> customerCLV = new ArrayList<CustomerCLV>();

        try {
            Query query = entityManager.createQuery("Select c From CustomerCLV c Where c.clvSegment=:customerSegment");
            query.setParameter("customerSegment", customerSegment);

            if (query.getResultList().isEmpty()) {
                return new ArrayList<CustomerCLV>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<CustomerCLV>();
        }
    }

    private String checkCustomerSegment(Double customerCLV) {

        if (customerCLV < 30) {
            return "Low";
        } else if (customerCLV >= 30 && customerCLV < 50) {
            return "Medium";
        } else if (customerCLV > 50) {
            return "High";
        }

        return "Invalid";
    }
}
