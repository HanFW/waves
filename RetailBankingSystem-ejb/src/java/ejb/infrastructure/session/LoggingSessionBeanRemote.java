/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.infrastructure.entity.Logging;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Nicole
 */
@Remote
public interface LoggingSessionBeanRemote {
public void createNewLogging(String trigger, Long person, String activity, String result, String remarks);
    public List<Logging> retrieveAllCustomerLogging();
    public List<Logging> retrieveAllEmployeeLogging();
    public List<Logging> retrieveAllSystemLogging();
}
