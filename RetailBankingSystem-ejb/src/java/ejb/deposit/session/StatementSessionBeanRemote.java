/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.deposit.session;

import ejb.deposit.entity.Statement;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Nicole
 */
@Remote
public interface StatementSessionBeanRemote {

    public Long addNewStatement(String statementDate, String statementType, String accountDetails,
            Long startTime, Long endTime, Long customerBasicId);

    public Statement retrieveStatementById(Long statementId);

    public List<Statement> retrieveStatementByAccNum(String bankAccountNum);

    public String generateStatement();

    public String deleteStatement(Long statementId);

}
