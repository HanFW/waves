package ejb.session.singleton;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;



@Singleton
@LocalBean
@Startup

public class InitSessionBean
{
    @PostConstruct
    public void init()
    {
        System.err.println("********** InitSessionBean.init(): Start");
        
        System.err.println("********** InitSessionBean.init(): End");
    }
}