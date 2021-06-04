package de.kieseltaucher.studies.persistence.restaurant.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

class Schema {

    void evolve(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("create table if not exists restaurant_table (table_number decimal(10) not null)");
        }
    }
}
