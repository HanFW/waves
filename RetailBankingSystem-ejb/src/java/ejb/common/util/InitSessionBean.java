package ejb.common.util;

import ejb.payement.session.SACHMasterBankAccountSessionBeanLocal;
import ejb.payement.session.SACHSessionBeanLocal;
import ejb.payment.entity.SACHMasterBankAccount;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;

@Singleton
@LocalBean
@Startup
public class InitSessionBean {
    @EJB
    private SACHSessionBeanLocal sACHSessionBeanLocal;
    
    @EJB
    private SACHMasterBankAccountSessionBeanLocal sACHMasterBankAccountSessionBeanLocal;
    
    @EJB
    private EjbTimerSessionBeanLocal ejbTimerSessionLocal;
    
    @PostConstruct
    public void init()
    {
//        ejbTimerSessionLocal.createTimer10000MS();
//        ejbTimerSessionLocal.createTimer300000MS();
//        ejbTimerSessionLocal.createTimer15000MS();
//        ejbTimerSessionLocal.createTimer70000MS();
//        createSACH();
//        ejbTimerSessionLocal.createTimer20000MS();
    }
    
    private void createSACH() {
        
        SACHMasterBankAccount dbsMasterBankAccount = sACHMasterBankAccountSessionBeanLocal.retrieveSACHMasterBankAccountByBankName("DBS");
        SACHMasterBankAccount merlionMasterBankAccount = sACHMasterBankAccountSessionBeanLocal.retrieveSACHMasterBankAccountByBankName("Merlion");
        
        Calendar cal = Calendar.getInstance();
        
        sACHSessionBeanLocal.addNewSACH(dbsMasterBankAccount.getMasterBankAccountBalance(), 
                merlionMasterBankAccount.getMasterBankAccountBalance(), 0.0, 0.0, cal.getTime().toString(), "DBS&Merlion");
    }
}
