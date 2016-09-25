package ejb.deposit.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.entity.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean

public class StatementSessionBean implements StatementSessionBeanLocal {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewStatement(String statementDate, String statementType, String accountDetails,
            Long startTime, Long endTime, Long bankAccountId) {
        System.out.println("*");
        System.out.println("****** deposit/StatementSessionBean: addNewStatement() ******");

        Statement statement = new Statement();

        statement.setStatementDate(statementDate);
        statement.setStatementType(statementType);
        statement.setAccountDetails(accountDetails);
        statement.setStartTime(startTime);
        statement.setEndTime(endTime);
        statement.setBankAccount(bankAccountSessionLocal.retrieveBankAccountById(bankAccountId));

        entityManager.persist(statement);
        entityManager.flush();

        return statement.getStatementId();
    }

    @Override
    public List<Statement> retrieveStatementByAccNum(String bankAccountNum) {
        System.out.println("*");
        System.out.println("****** deposit/StatementSessionBean: retrieveStatementByAccNum() ******");
        BankAccount bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(bankAccountNum);

        if (bankAccount.getBankAccountId() == null) {
            return new ArrayList<Statement>();
        }
        try {
            Query query = entityManager.createQuery("Select s From Statement s Where s.bankAccount=:bankAccount");
            query.setParameter("bankAccount", bankAccount);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/StatementSessionBean: retrieveStatementByAccNum(): invalid bank account number: no result found, return new statement");
                return new ArrayList<Statement>();
            } else {
                System.out.println("****** deposit/StatementSessionBean: retrieveStatementByAccNum(): valid bank account number: return statement");
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/StatementSessionBean: retrieveStatementByAccNum(): Entity not found error: " + enfe.getMessage());
            return new ArrayList<Statement>();
        }
    }

    @Override
    public Statement retrieveStatementById(Long statementId) {
        System.out.println("*");
        System.out.println("****** deposit/StatementSessionBean: retrieveStatementById() ******");
        Statement statement = new Statement();

        try {
            Query query = entityManager.createQuery("Select s From Statement s Where s.statementId=:statementId");
            query.setParameter("statementId", statementId);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/StatementSessionBean: retrieveStatementById(): invalid statement Id: no result found, return new statement");
                return new Statement();
            } else {
                System.out.println("****** deposit/StatementSessionBean: retrieveStatementById(): valid statement Id: return statement");
                statement = (Statement) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/StatementSessionBean: retrieveStatementById(): Entity not found error: " + enfe.getMessage());
            return new Statement();
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/StatementSessionBean: retrieveStatementById(): Non unique result error: " + nure.getMessage());
        }

        return statement;
    }

    @Override
    public void generateStatement() {
        System.out.println("*");
        System.out.println("****** deposit/StatementSessionBean: generateStatement() ******");

        Query query = entityManager.createQuery("SELECT a FROM BankAccount a WHERE a.bankAccountStatus = :bankAccountStatus");
        query.setParameter("bankAccountStatus", "Activated");
        List<BankAccount> activatedBankAccounts = query.getResultList();

        for (BankAccount activatedBankAccount : activatedBankAccounts) {

//            Calendar cal = Calendar.getInstance();
//            Long startTimeLong = cal.getTimeInMillis() - 30000000;
//            AccTransaction transaction = activatedBankAccount.getAccTransaction().get(activatedBankAccount.getAccTransaction().size() - 1);
//            
//            if (transaction.getTransactionDateMilis() < startTimeLong) {
//                System.out.println("There will be no statement this month");
//            } else {
            String bankAccountNum = activatedBankAccount.getBankAccountNum();
            CustomerBasic customerBasic = bankAccountSessionLocal.retrieveCustomerBasicByAccNum(bankAccountNum);
            List<Statement> statemens = activatedBankAccount.getStatement();

            Double statementDateDouble = activatedBankAccount.getStatementDateDouble() + 1;
            if (statementDateDouble == 12) {
                activatedBankAccount.setStatementDateDouble(0.0);
            } else {
                activatedBankAccount.setStatementDateDouble(statementDateDouble);
            }

            String statementDate = handleStatementDate(statementDateDouble);
            String statementType = "Merlion Bank Consolidated Statement";

            String customerName = customerBasic.getCustomerName();
            bankAccountNum = activatedBankAccount.getBankAccountNum();
            String bankAccountType = activatedBankAccount.getBankAccountType();
            String accountDetails = customerName + "\n" + bankAccountNum + "\n" + bankAccountType;
            
            Calendar cal = Calendar.getInstance();
            Long endTimeLong = cal.getTimeInMillis();
            Long startTimeLong = endTimeLong - 300000;

            Long newStatementId = addNewStatement(statementDate, statementType, accountDetails, 
                    startTimeLong, endTimeLong, activatedBankAccount.getBankAccountId());
            Statement statement = retrieveStatementById(newStatementId);

            statemens.add(statement);
        }
    }

    private String handleStatementDate(Double statementDateDouble) {

        String statementDate = "";

        if (statementDateDouble == 1) {
            statementDate = "Jan 2016";
        } else if (statementDateDouble == 2) {
            statementDate = "Feb 2016";
        } else if (statementDateDouble == 3) {
            statementDate = "Mar 2016";
        } else if (statementDateDouble == 4) {
            statementDate = "Apr 2016";
        } else if (statementDateDouble == 5) {
            statementDate = "May 2016";
        } else if (statementDateDouble == 6) {
            statementDate = "Jun 2016";
        } else if (statementDateDouble == 7) {
            statementDate = "Jul 2016";
        } else if (statementDateDouble == 8) {
            statementDate = "Aug 2016";
        } else if (statementDateDouble == 9) {
            statementDate = "Sep 2016";
        } else if (statementDateDouble == 10) {
            statementDate = "Oct 2016";
        } else if (statementDateDouble == 11) {
            statementDate = "Nov 2016";
        } else if (statementDateDouble == 12) {
            statementDate = "Dec 2016";
        }

        return statementDate;
    }
}
