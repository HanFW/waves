package ejb.deposit.session;

import ejb.deposit.entity.Statement;
import java.util.List;
import javax.ejb.Local;


@Local
public interface StatementSessionBeanLocal {
    
    public Long addNewStatement(String statementDate, String statementType, String accountDetails, 
            Long startTime, Long endTime, Long customerBasicId);
    public Statement retrieveStatementById(Long statementId);
    public List<Statement> retrieveStatementByAccNum(String bankAccountNum);
    public void generateStatement();
}
