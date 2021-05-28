package de.kieseltaucher.studies.persistence.restaurant.db.jdbcutil;

import java.sql.SQLException;

public interface SQLFunction<T, R> {
    R apply(T t) throws SQLException;
}
