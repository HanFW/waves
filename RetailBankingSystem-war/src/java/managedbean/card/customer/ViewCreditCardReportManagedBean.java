package managedbean.card.customer;

import ejb.card.entity.CreditCardReport;
import ejb.card.entity.PrincipalCard;
import ejb.card.session.CreditCardReportSessionBeanLocal;
import ejb.card.session.PrincipalCardSessionBeanLocal;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@Named(value = "viewCreditCardReportManagedBean")
@SessionScoped

public class ViewCreditCardReportManagedBean implements Serializable{

    @EJB
    private CreditCardReportSessionBeanLocal creditCardReportSessionBeanLocal;

    @EJB
    private PrincipalCardSessionBeanLocal principalCardSessionBeanLocal;

    @Resource(name = "retailBankingSystemDataSource")
    private DataSource retailBankingSystemDataSource;

    private ExternalContext ec;
    private Long cardId;
    private Long creditCardReportId;

    public ViewCreditCardReportManagedBean() {
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        System.out.println("set");
        this.cardId = cardId;
    }

    public Long getCreditCardReportId() {
        return creditCardReportId;
    }

    public void setCreditCardReportId(Long creditCardReportId) {
        this.creditCardReportId = creditCardReportId;
    }

    public List<CreditCardReport> getCreditCardReport() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        System.out.println("********card" + cardId);
        PrincipalCard card = principalCardSessionBeanLocal.retrieveCardById(cardId);
        List<CreditCardReport> creditCardReport = card.getCreditCardReport();

        return creditCardReport;
    }

    public void viewCreditCardReport() throws ClassNotFoundException, IOException, JRException, SQLException {

        System.out.println("********report" + creditCardReportId);
        Connection connection;

        FacesContext ctx = FacesContext.getCurrentInstance();

        HttpServletResponse response = (HttpServletResponse) ctx
                .getExternalContext().getResponse();

        InputStream reportStream = ctx.getExternalContext()
                .getResourceAsStream("/E-Statements/creditCardReport.jasper");

        ServletOutputStream servletOutputStream = response.getOutputStream();
        Class.forName("com.mysql.jdbc.Driver");
        connection = retailBankingSystemDataSource.getConnection();

        ctx.responseComplete();
        response.setContentType("application/pdf");

        CreditCardReport creditCardReport = creditCardReportSessionBeanLocal.retrieveCreditCardReportById(creditCardReportId);
        PrincipalCard principalCard = creditCardReport.getPrincipalCard();

        Long startTimeLong = creditCardReport.getStartTime();
        Long endTimeLong = creditCardReport.getEndTime();

        Map parameters = new HashMap();
        parameters.put("cardId", principalCard.getCardId());
        parameters.put("startTime", startTimeLong);
        parameters.put("endTime", endTimeLong);

        JasperRunManager.runReportToPdfStream(reportStream, servletOutputStream, parameters, connection);

        connection.close();
        servletOutputStream.flush();
        servletOutputStream.close();
    }
}
