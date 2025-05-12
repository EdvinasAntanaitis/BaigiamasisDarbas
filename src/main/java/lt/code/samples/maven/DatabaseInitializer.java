package lt.code.samples.maven;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/orders_db";
        String user = "postgres";
        String password = "kasyra123";

        String ordersTable = """
            CREATE TABLE IF NOT EXISTS orders (
                id SERIAL PRIMARY KEY,
                order_name TEXT NOT NULL,
                material TEXT,
                color TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                barcode TEXT UNIQUE,
                length DOUBLE PRECISION,
                width DOUBLE PRECISION,
                thickness DOUBLE PRECISION,
                amount INTEGER,
                description TEXT,
                edge_type TEXT,
                status TEXT,
                edited_by TEXT
            );
            """;

        String itemsTable = """
            CREATE TABLE IF NOT EXISTS items (
                id SERIAL PRIMARY KEY,
                order_id INTEGER REFERENCES orders(id) ON DELETE CASCADE,
                length DOUBLE PRECISION,
                width DOUBLE PRECISION,
                thickness DOUBLE PRECISION,
                amount INTEGER,
                sides TEXT
            );
            """;

        String workLogsTable = """
            CREATE TABLE IF NOT EXISTS work_logs (
                id SERIAL PRIMARY KEY,
                order_id INTEGER REFERENCES orders(id) ON DELETE CASCADE,
                worker_name TEXT,
                start_time TIMESTAMP,
                end_time TIMESTAMP
            );
            """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            stmt.execute(ordersTable);
            stmt.execute(itemsTable);
            stmt.execute(workLogsTable);

            System.out.println("✅ All tables created successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Error creating tables: " + e.getMessage());
        }
    }
}
