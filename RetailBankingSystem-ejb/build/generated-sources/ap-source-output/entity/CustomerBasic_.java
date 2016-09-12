package entity;

import entity.BankAccount;
import entity.Payee;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-09-12T13:44:23")
@StaticMetamodel(CustomerBasic.class)
public class CustomerBasic_ { 

    public static volatile SingularAttribute<CustomerBasic, String> customerSalutation;
    public static volatile SingularAttribute<CustomerBasic, String> customerAddress;
    public static volatile ListAttribute<CustomerBasic, BankAccount> bankAccount;
    public static volatile SingularAttribute<CustomerBasic, String> customerPayeeNum;
    public static volatile SingularAttribute<CustomerBasic, Long> customerBasicId;
    public static volatile SingularAttribute<CustomerBasic, String> customerRace;
    public static volatile SingularAttribute<CustomerBasic, String> customerGender;
    public static volatile SingularAttribute<CustomerBasic, byte[]> customerSignature;
    public static volatile SingularAttribute<CustomerBasic, String> customerPostal;
    public static volatile SingularAttribute<CustomerBasic, String> customerOnlineBankingPassword;
    public static volatile SingularAttribute<CustomerBasic, String> customerDateOfBirth;
    public static volatile SingularAttribute<CustomerBasic, String> customerCountryOfResidence;
    public static volatile SingularAttribute<CustomerBasic, String> customerName;
    public static volatile SingularAttribute<CustomerBasic, String> customerStatus;
    public static volatile ListAttribute<CustomerBasic, Payee> payee;
    public static volatile SingularAttribute<CustomerBasic, String> customerOnlineBankingAccountNum;
    public static volatile SingularAttribute<CustomerBasic, String> customerIdentificationNum;
    public static volatile SingularAttribute<CustomerBasic, String> customerOccupation;
    public static volatile SingularAttribute<CustomerBasic, String> customerNationality;
    public static volatile SingularAttribute<CustomerBasic, String> customerEmail;
    public static volatile SingularAttribute<CustomerBasic, String> customerMaritalStatus;
    public static volatile SingularAttribute<CustomerBasic, String> customerCompany;
    public static volatile SingularAttribute<CustomerBasic, String> customerMobile;

}