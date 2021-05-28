package de.kieseltaucher.studies.persistence.restaurant.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

class JdbcTemplate {

    private final Supplier<Connection> dataSource;

    JdbcTemplate(Supplier<Connection> dataSource) {
        this.dataSource = dataSource;
    }

    <R> R withPreparedStatement(String sql, SQLFunction<PreparedStatement, R> consumer) {
        try (final Connection con = open();
             final PreparedStatement stm = con.prepareStatement(sql)) {
            return consumer.apply(stm);
        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }
    }

    <R> R withQuery(PreparedStatement query, SQLFunction<ResultSet, R> consumer) {
        try (final ResultSet results = query.executeQuery()) {
            return consumer.apply(results);
        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }
    }

    private Connection open() {
        return dataSource.get();
    }
}
