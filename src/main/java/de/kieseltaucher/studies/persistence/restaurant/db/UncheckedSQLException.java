package de.kieseltaucher.studies.persistence.restaurant.db;

import java.sql.SQLException;

class UncheckedSQLException extends RuntimeException {

    UncheckedSQLException(SQLException cause) {
        super(cause);
    }

}
