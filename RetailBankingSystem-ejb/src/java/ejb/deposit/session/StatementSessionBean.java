package ejb.deposit.session;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.entity.Statement;
import java.util.ArrayList;
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

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewStatement(String statementDate, String statementType, String accountDetails,
            Long bankAccountId) {

        Statement statement = new Statement();

        statement.setStatementDate(statementDate);
        statement.setStatementType(statementType);
        statement.setAccountDetails(accountDetails);
        statement.setBankAccount(bankAccountSessionLocal.retrieveBankAccountById(bankAccountId));

        entityManager.persist(statement);
        entityManager.flush();

        return statement.getStatementId();
    }

    @Override
    public List<Statement> retrieveStatementByAccNum(String bankAccountNum) {
        BankAccount bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(bankAccountNum);

        if (bankAccount.getBankAccountId() == null) {
            return new ArrayList<Statement>();
        }
        try {
            Query query = entityManager.createQuery("Select s From Statement s Where s.bankAccount=:bankAccount");
            query.setParameter("bankAccount", bankAccount);
            return query.getResultList();
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new ArrayList<Statement>();
        }
    }

    @Override
    public Statement retrieveStatementById(Long statementId) {
        Statement statement = new Statement();

        try {
            Query query = entityManager.createQuery("Select s From Statement s Where s.statementId=:statementId");
            query.setParameter("statementId", statementId);
            statement = (Statement) query.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new Statement();
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return statement;
    }

    @Override
    public void generateStatement() {

        System.out.println("generateStatement");
        Query query = entityManager.createQuery("SELECT a FROM BankAccount a WHERE a.bankAccountStatus = :bankAccountStatus");
        query.setParameter("bankAccountStatus", "Activated");
        List<BankAccount> activatedBankAccounts = query.getResultList();

        for (BankAccount activatedBankAccount : activatedBankAccounts) {
            System.out.println("generateStatement for");
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

            System.out.println("generateStatement add");
            Long newStatementId = addNewStatement(statementDate, statementType, accountDetails, customerBasic.getCustomerBasicId());
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
