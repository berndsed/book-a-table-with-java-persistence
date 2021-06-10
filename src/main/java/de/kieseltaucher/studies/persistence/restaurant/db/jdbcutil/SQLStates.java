package de.kieseltaucher.studies.persistence.restaurant.db.jdbcutil;

import java.sql.SQLException;

public class SQLStates {

    public static boolean isIntegrityConstraintViolation(SQLException e) {
        return e.getSQLState().startsWith("23");
    }

    private SQLStates() {
    }
}
