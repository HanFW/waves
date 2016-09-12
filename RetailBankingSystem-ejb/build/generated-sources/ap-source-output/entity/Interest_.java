package entity;

import entity.BankAccount;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-09-12T13:44:23")
@StaticMetamodel(Interest.class)
public class Interest_ { 

    public static volatile SingularAttribute<Interest, BankAccount> bankAccount;
    public static volatile SingularAttribute<Interest, String> isTransfer;
    public static volatile SingularAttribute<Interest, Long> interestId;
    public static volatile SingularAttribute<Interest, String> isWithdraw;
    public static volatile SingularAttribute<Interest, String> dailyInterest;
    public static volatile SingularAttribute<Interest, String> monthlyInterest;

}