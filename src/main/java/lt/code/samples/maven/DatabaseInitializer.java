package lt.code.samples.maven;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:orders.db";

        String sql = """
                CREATE TABLE IF NOT EXISTS orders (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    order_name TEXT NOT NULL,
                    material TEXT,
                    color TEXT,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    barcode TEXT UNIQUE,
                    length REAL,
                    width REAL,
                    thickness REAL,
                    amount INTEGER,
                    description TEXT,
                    edge_type TEXT,
                    status TEXT,
                    edited_by TEXT
                );
                """;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("✅ Table created successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Error creating table: " + e.getMessage());
        }
    }
}
