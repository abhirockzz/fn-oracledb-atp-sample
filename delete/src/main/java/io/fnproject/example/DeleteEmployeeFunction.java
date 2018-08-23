package io.fnproject.example;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.util.Properties;

public class DeleteEmployeeFunction {

    private Connection conn = null;

    public DeleteEmployeeFunction() {
        try {
            Class<Driver> driver = (Class<Driver>) Class.forName("oracle.jdbc.driver.OracleDriver");

            String dbUrl = System.getenv().getOrDefault("DB_URL", "localhost");
            String dbUser = System.getenv().getOrDefault("DB_USER", "scott");
            String dbPasswd = System.getenv().getOrDefault("DB_PASSWORD", "tiger");

            System.err.println("Connecting to Oracle ATP DB...");
            System.err.println("URL " + dbUrl);
            System.err.println("User " + dbUser);

            Properties prop = new Properties();
            
            prop.setProperty("user", dbUser);
            prop.setProperty("password", dbPasswd);
            prop.put("oracle.net.tns_admin", System.getenv().getOrDefault("CLIENT_CREDENTIALS", "."));
            prop.put("oracle.net.ssl_server_dn_match", "true");
            prop.put("oracle.net.ssl_version", "1.2");
            prop.put("javax.net.ssl.keyStore", System.getenv().getOrDefault("CLIENT_CREDENTIALS", ".")+"/keystore.jks");
            prop.put("javax.net.ssl.keyStorePassword", System.getenv().getOrDefault("KEYSTORE_PASSWORD", "s3cr3t"));
            prop.put("javax.net.ssl.trustStore", System.getenv().getOrDefault("CLIENT_CREDENTIALS", ".")+"/truststore.jks");
            prop.put("javax.net.ssl.trustStorePassword", System.getenv().getOrDefault("TRUSTSTORE_PASSWORD", "s3cr3t"));

            conn = driver.getDeclaredConstructor().newInstance().connect(dbUrl, prop);
            System.err.println("Connected to Oracle ATP DB successfully");

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
