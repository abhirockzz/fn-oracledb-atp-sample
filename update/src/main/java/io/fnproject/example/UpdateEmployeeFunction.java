package io.fnproject.example;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.util.Properties;

public class UpdateEmployeeFunction {

    private Connection conn = null;

    public UpdateEmployeeFunction() {
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

    public String handle(UpdateEmployeeInfo empInfo) {
        return update(empInfo);
    }

    private String update(UpdateEmployeeInfo empInfo) {
        String status = "Failed to update employee " + empInfo;

        if (conn == null) {
            return status;
        }

        System.err.println("Updating employee info " + empInfo);

        int updated = 0;
        try (PreparedStatement st = conn.prepareStatement("update EMPLOYEES set EMP_DEPT=? where EMP_EMAIL=?")) {
            st.setString(1, empInfo.getEmp_dept());
            st.setString(2, empInfo.getEmp_email());

            updated = st.executeUpdate();

            System.err.println(updated + " rows updated");
            status = "Updated employee " + empInfo;

        } catch (Exception se) {
            System.err.println("Unable to update data in DB due to - " + se.getMessage());
        }
        return status;
    }

}
