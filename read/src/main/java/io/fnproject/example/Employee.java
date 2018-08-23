package io.fnproject.example;

import java.sql.ResultSet;
import java.util.Objects;

public class Employee {
    
    private String emp_email;
    private String emp_name;
    private String emp_dept;

    public Employee() {
    }

    public Employee(String emp_email, String emp_name, String emp_dept) {
        this.emp_email = emp_email;
        this.emp_name = emp_name;
        this.emp_dept = emp_dept;
    }

    public String getEmp_email() {
        return emp_email;
    }

    public void setEmp_email(String emp_email) {
        this.emp_email = emp_email;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_dept() {
        return emp_dept;
    }

    public void setEmp_dept(String emp_dept) {
        this.emp_dept = emp_dept;
    }

    @Override
    public String toString() {
        return "CreateEmployeeInfo{" + "emp_email=" + emp_email + ", emp_name=" + emp_name + ", emp_dept=" + emp_dept + '}';
    }

    
   
    
}
