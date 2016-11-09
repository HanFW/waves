package managedbean.deposit.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

@Named(value = "depositIndexManagedBean")
@RequestScoped

public class DepositIndexManagedBean {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    private ExternalContext ec;
    private BarChartModel barModel;

    public DepositIndexManagedBean() {
    }

    @PostConstruct
    public void init() {
        createBarModels();
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    private BarChartModel initBarModel() {
        System.out.println("=");
        System.out.println("====== deposit/DepositIndexManagedBean: initBarModel() ======");
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        List<BankAccount> bankAccounts = bankAccountSessionLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());
        Double totalBalance = 0.0;

        for (int i = 0; i < bankAccounts.size(); i++) {
            totalBalance = totalBalance + Double.valueOf(bankAccounts.get(i).getAvailableBankAccountBalance());
        }

        BarChartModel model = new BarChartModel();

        ChartSeries balance = new ChartSeries();
        balance.set("Cash&Investments", totalBalance);

        model.addSeries(balance);

        return model;
    }

    private void createBarModels() {
        createBarModel();
    }

    private void createBarModel() {
        barModel = initBarModel();

        barModel.setLegendPosition("ne");

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel(" ");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel(" ");
    }

    public List<BankAccount> getBankAccount() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        String customerIC = customerBasic.getCustomerIdentificationNum();

        List<BankAccount> bankAccount = bankAccountSessionLocal.retrieveBankAccountByCusIC(customerIC.toUpperCase());

        if (bankAccount.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Your identification is invalid", "Failed!"));
        }

        return bankAccount;
    }
}
