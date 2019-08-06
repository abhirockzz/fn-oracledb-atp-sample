package io.fnproject.example;

import com.fnproject.fn.api.RuntimeContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class GetEmployeeFunction {

    private Connection conn = null;

    public GetEmployeeFunction(RuntimeContext ctx) {
        try {

            String dbUser = System.getenv().getOrDefault("DB_USER", "scott");
            System.err.println("DB User " + dbUser);

            String dbPasswd = System.getenv().getOrDefault("DB_PASSWORD", "tiger");

            String dbServiceName = System.getenv().getOrDefault("DB_SERVICE_NAME", "localhost");
            System.err.println("DB Service name " + dbServiceName);

            String tnsAdminLocation = System.getenv().getOrDefault("CLIENT_CREDENTIALS", ".");
            System.err.println("TNS Admin location " + tnsAdminLocation);

            String dbUrl = "jdbc:oracle:thin:@" + dbServiceName + "?TNS_ADMIN=" + tnsAdminLocation;
            System.err.println("DB URL " + dbUrl);

            Properties prop = new Properties();

            prop.setProperty("user", dbUser);
            prop.setProperty("password", dbPasswd);

            System.err.println("Connecting to Oracle ATP DB......");

            conn = DriverManager.getConnection(dbUrl, prop);
            if (conn != null) {
                System.err.println("Connected to Oracle ATP DB successfully");
            }

        } catch (Throwable e) {
            System.err.println("DB connectivity failed due - " + e.getMessage());
        }

    }

    public List<Employee> handle(String empEmail) {
        return read(empEmail);
    }

    private static final String GET_ALL_EMPLOYEES = "select * from EMPLOYEES";
    private static final String GET_EMPLOYEE_INFO = "select * from EMPLOYEES where EMP_EMAIL=?";

    private List<Employee> read(String empEmail) {

        if (conn == null) {
            System.err.println("Warning: JDBC connection was 'null'");
            return Collections.emptyList();
        }

        String query = null;

        if (empEmail.equals("")) {
            System.err.println("Getting all employees...");
            query = GET_ALL_EMPLOYEES;
        } else {
            System.err.println("Fetching employee info for " + empEmail);
            query = GET_EMPLOYEE_INFO;
        }

        List<Employee> emps = new ArrayList<>();

        try (PreparedStatement st = conn.prepareStatement(query)) {
            if (!empEmail.equals("")) {
                st.setString(1, empEmail);
            }

            ResultSet empRSet = st.executeQuery();

            while (empRSet.next()) {
                emps.add(new Employee(empRSet.getString("EMP_EMAIL"), empRSet.getString("EMP_NAME"), empRSet.getString("EMP_DEPT")));
            }

        } catch (Exception se) {
            System.err.println("Unable to fetch employee info " + se.getMessage());
        }
        return emps;
    }

}
