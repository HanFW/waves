package session.stateless;

import entity.Statement;
import java.util.List;
import javax.ejb.Local;


@Local
public interface StatementSessionBeanLocal {
    
    public Long addNewStatement(String statementDate, String statementType, String accountDetails,Long customerBasicId);
    public List<Statement> retrieveStatementByCusIC(String customerIdentificationNum);
    public Statement retrieveStatementById(Long statementId);
    public void generateStatement();
}
