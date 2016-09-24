/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import javax.ejb.Local;

/**
 *
 * @author hanfengwei
 */
@Local
public interface LoggingSessionBeanLocal {
    public void createNewLogging(String trigger, Long person, String activity, String result, String remarks);
}
