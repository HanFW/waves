package ejb.payement.session;

import ejb.payment.entity.MEPS;
import ejb.payment.entity.MEPSMasterBankAccount;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless

public class MEPSSessionBean implements MEPSSessionBeanLocal {

    @EJB
    private MEPSMasterAccountTransactionSessionBeanLocal mEPSMasterAccountTransactionSessionBeanLocal;

    @EJB
    private MEPSMasterBankAccountSessionBeanLocal mEPSMasterBankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewMEPS(String settlementRef, String settlementDate, String bankNames) {

        MEPS meps = new MEPS();

        meps.setSettlementRef(settlementRef);
        meps.setSettlementDate(settlementDate);
        meps.setBankNames(bankNames);

        entityManager.persist(meps);
        entityManager.flush();

        return meps.getMepsId();
    }

    @Override
    public void MEPSSettlement(String fromMasterBankAccountNum, String toMasterBankAccountNum, Double transferAmt) {

        MEPSMasterBankAccount merlionMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveMEPSMasterBankAccountByAccNum(fromMasterBankAccountNum);
        MEPSMasterBankAccount dbsMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveMEPSMasterBankAccountByAccNum(toMasterBankAccountNum);

        String settlementRefMerlion = "Pay " + "S$" + transferAmt+ " to DBS";
        String settlementRefDBS = "Receive " +"S$" +transferAmt+" from Merlion Bank";
        String settlementRef = "Merlion Bank pays DBS S$"+transferAmt;
        
        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        String bankNames = "DBS&Merlion";

        Long newMepsId = addNewMEPS(settlementRef, currentTime, bankNames);

        Double merlionCurrentBalance = Double.valueOf(merlionMasterBankAccount.getMasterBankAccountBalance()) - transferAmt;
        Double dbsCurrentBalance = Double.valueOf(dbsMasterBankAccount.getMasterBankAccountBalance()) + transferAmt;

        Long newMerlionMasterAccountTransaction = mEPSMasterAccountTransactionSessionBeanLocal.addNewMasterAccountTransaction(currentTime, settlementRefMerlion, transferAmt.toString(), " ", Long.valueOf(1));
        Long newDBSMasterAccountTransaction = mEPSMasterAccountTransactionSessionBeanLocal.addNewMasterAccountTransaction(currentTime, settlementRefDBS, " ", transferAmt.toString(), Long.valueOf(2));

        merlionMasterBankAccount.setMasterBankAccountBalance(merlionCurrentBalance.toString());
        dbsMasterBankAccount.setMasterBankAccountBalance(dbsCurrentBalance.toString());
    }
    
    @Override
    public List<MEPS> getAllMEPS(String bankNames) {

        Query query = entityManager.createQuery("SELECT m FROM MEPS m Where m.bankNames=:bankNames");
        query.setParameter("bankNames", bankNames);

        return query.getResultList();
    }
}
