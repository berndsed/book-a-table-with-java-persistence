package de.kieseltaucher.studies.persistence.restaurant.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.extension.ExtensionContext;

class DatabaseInstance implements ExtensionContext.Store.CloseableResource {

    private static final String ORG_H_2_DRIVER = "org.h2.Driver";
    /*
     * Without a database name in the url each new connection is a connection to a new database.
     * This makes it easy to isolate the tests, because each test has its own private database.
     */
    private static final String PRIVATE_DB_CONNECTION_URL = "jdbc:h2:mem:";

    static DatabaseInstance newInstance() {
        registerDriver();
        final Connection connection = openPrivateConnection();
        createSchema(connection);
        return new DatabaseInstance(connection);
    }

    private static void registerDriver() {
        try {
            Class.forName(ORG_H_2_DRIVER);
        } catch (ClassNotFoundException e) {
            final var msg = String.format("Failed to register driver %s: %s", ORG_H_2_DRIVER, e);
            throw new IllegalStateException(msg, e);
        }
    }

    private static Connection openPrivateConnection() {
        final Connection connection;
        try {
            connection = DriverManager.getConnection(PRIVATE_DB_CONNECTION_URL);
        } catch (SQLException e) {
            final var msg = String.format("Failed to create connection to %s: %s", PRIVATE_DB_CONNECTION_URL, e);
            throw new IllegalStateException(msg, e);
        }
        return connection;
    }

    private static void createSchema(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("create table if not exists test_table (v varchar)");
        } catch (SQLException e) {
            final var msg = String.format("Failed to create schema: %s", e);
            throw new IllegalStateException(msg, e);
        }
    }

    private final Connection connection;

    private DatabaseInstance(Connection connection) {

        this.connection = connection;
    }

    Connection connection() {
        return connection;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
