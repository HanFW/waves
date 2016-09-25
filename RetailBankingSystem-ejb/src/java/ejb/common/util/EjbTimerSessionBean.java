package ejb.common.util;

import java.util.Collection;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.StatementSessionBeanLocal;

@Stateless
@LocalBean

public class EjbTimerSessionBean implements EjbTimerSessionBeanLocal{
    @EJB
    private StatementSessionBeanLocal statementSessionBeanLocal;
    
    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    @Resource
    private SessionContext ctx;
    @PersistenceContext
    private EntityManager entityManager;

    private final String TIMER_NAME_10000MS = "EJB-TIMER-10000MS";
    private final int TIMER_DURATION_10000MS = 10000;
    private final String TIMER_NAME_300000MS = "EJB-TIMER-300000MS";
    private final int TIMER_DURATION_300000MS = 300100;

    public EjbTimerSessionBean() {

    }
    
    public EjbTimerSessionBean(String bankAccountNum) {
        
    }
    
    @Override
    public void createTimer10000MS() {
        TimerService timerService = ctx.getTimerService();

        Timer timer10000ms = timerService.createTimer(TIMER_DURATION_10000MS,
                TIMER_DURATION_10000MS, new String(TIMER_NAME_10000MS));
        System.out.println("{***10000MS Timer created" + String.valueOf(timer10000ms.getTimeRemaining()) + ","
                + timer10000ms.getInfo().toString());
    }

    @Override
    public void cancelTimer10000MS() {
        TimerService timerService = ctx.getTimerService();
        Collection timers = timerService.getTimers();

        for (Object obj : timers) {
            Timer timer = (Timer) obj;

            if (timer.getInfo().toString().equals(TIMER_NAME_10000MS));
            {
                timer.cancel();
                System.out.println("*** 10000MS Timer cancelled");
            }
        }
    }
    
    @Override
    public void createTimer300000MS() {
        TimerService timerService = ctx.getTimerService();

        Timer timer300000ms = timerService.createTimer(TIMER_DURATION_300000MS,
                TIMER_DURATION_300000MS, new String(TIMER_NAME_300000MS));
        
        System.out.println("{***300000MS Timer created" + String.valueOf(timer300000ms.getTimeRemaining()) + ","
                + timer300000ms.getInfo().toString());
    }
    
    @Override
    public void cancelTimer300000MS() {
        TimerService timerService = ctx.getTimerService();
        Collection timers = timerService.getTimers();

        for (Object obj : timers) {
            Timer timer = (Timer) obj;

            if (timer.getInfo().toString().equals(TIMER_NAME_300000MS));
            {
                timer.cancel();
                System.out.println("*** 300000MS Timer cancelled");
            }
        }
    }

    @Timeout
    public void handleTimeout(Timer timer) {
        if (timer.getInfo().toString().equals(TIMER_NAME_10000MS)) 
        {
            handleTimeout_10000ms();
        } 
        else if(timer.getInfo().toString().equals(TIMER_NAME_300000MS))
        {
            handleTimeout_300000ms();
        }
        else {
            System.out.println("*** Unknown timer timeout: " + timer.getInfo().toString());
        }
    }

    private void handleTimeout_10000ms() {
//        System.out.println("*** 10000MS Timer timeout");
        
        bankAccountSessionLocal.interestAccuring();
    }
    
    private void handleTimeout_300000ms() {
        System.out.println("*** 300000MS Timer timeout");
        
        bankAccountSessionLocal.interestCrediting();
        statementSessionBeanLocal.generateStatement();
    }
}
