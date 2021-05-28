package de.kieseltaucher.studies.persistence.restaurant.db;

import java.sql.SQLException;

interface SQLFunction<T, R> {
    R apply(T t) throws SQLException;
}
