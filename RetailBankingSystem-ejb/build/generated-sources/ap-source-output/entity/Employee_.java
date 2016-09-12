package entity;

import entity.Role;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-09-12T13:44:23")
@StaticMetamodel(Employee.class)
public class Employee_ { 

    public static volatile SingularAttribute<Employee, String> employeeName;
    public static volatile SetAttribute<Employee, Role> role;
    public static volatile SingularAttribute<Employee, Long> employeeId;
    public static volatile SingularAttribute<Employee, String> employeePosition;
    public static volatile SingularAttribute<Employee, String> employeeAccountNum;
    public static volatile SingularAttribute<Employee, String> employeePassword;
    public static volatile SingularAttribute<Employee, String> employeeDepartment;

}