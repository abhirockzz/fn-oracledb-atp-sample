package io.fnproject.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

public class DeleteEmployeeFunction {

    private Connection conn = null;

    public DeleteEmployeeFunction() {
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

    public String handle(String empEmail) {
        if (empEmail == null || empEmail.equals("")) {
            return "Employee email null/empty";
        }
        return delete(empEmail);
    }

    private String delete(String empEmail) {
        String status = "Failed to delete employee " + empEmail;

        if (conn == null) {
            System.err.println("Warning: JDBC connection was 'null'");
            return status;
        }

        System.err.println("Deleting employee from DB " + empEmail);
        int updated = 0;
        try (PreparedStatement st = conn.prepareStatement("delete from EMPLOYEES where EMP_EMAIL=?")) {
            st.setString(1, empEmail);
            updated = st.executeUpdate();

            System.err.println(updated + " rows updated");
            status = "Deleted employee " + empEmail;

        } catch (Exception se) {
            System.err.println("Unable to delete from DB due to - " + se.getMessage());
        }

        return status;
    }

}
