package io.fnproject.example;

public class UpdateEmployeeInfo {
    
    private String emp_email;
    private String emp_dept;

    public UpdateEmployeeInfo() {
    }

    public UpdateEmployeeInfo(String emp_email, String emp_dept) {
        this.emp_email = emp_email;
        this.emp_dept = emp_dept;
    }

    public String getEmp_email() {
        return emp_email;
    }

    public void setEmp_email(String emp_email) {
        this.emp_email = emp_email;
    }


    public String getEmp_dept() {
        return emp_dept;
    }

    public void setEmp_dept(String emp_dept) {
        this.emp_dept = emp_dept;
    }

    @Override
    public String toString() {
        return "UpdateEmployeeInfo{" + "emp_email=" + emp_email + ", emp_dept=" + emp_dept + '}';
    }

   
    
}
