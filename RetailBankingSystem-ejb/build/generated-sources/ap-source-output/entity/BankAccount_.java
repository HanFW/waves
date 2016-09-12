package entity;

import entity.AccTransaction;
import entity.CustomerBasic;
import entity.Interest;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-09-12T13:44:23")
@StaticMetamodel(BankAccount.class)
public class BankAccount_ { 

    public static volatile SingularAttribute<BankAccount, String> transferBalance;
    public static volatile SingularAttribute<BankAccount, String> bankAccountPwd;
    public static volatile SingularAttribute<BankAccount, String> bankAccountType;
    public static volatile SingularAttribute<BankAccount, Interest> interest;
    public static volatile SingularAttribute<BankAccount, String> bankAccountStatus;
    public static volatile SingularAttribute<BankAccount, Long> bankAccountId;
    public static volatile SingularAttribute<BankAccount, String> bankAccountBalance;
    public static volatile SingularAttribute<BankAccount, CustomerBasic> customerBasic;
    public static volatile SingularAttribute<BankAccount, String> transferDailyLimit;
    public static volatile SingularAttribute<BankAccount, String> bankAccountNum;
    public static volatile ListAttribute<BankAccount, AccTransaction> accTransaction;

}