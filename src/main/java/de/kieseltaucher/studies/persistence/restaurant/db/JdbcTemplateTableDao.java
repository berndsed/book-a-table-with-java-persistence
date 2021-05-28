package de.kieseltaucher.studies.persistence.restaurant.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.kieseltaucher.studies.persistence.restaurant.db.jdbcutil.JdbcTemplate;
import de.kieseltaucher.studies.persistence.restaurant.model.Table;
import de.kieseltaucher.studies.persistence.restaurant.model.TableNumber;
import de.kieseltaucher.studies.persistence.restaurant.service.TableDAO;

class JdbcTemplateTableDao implements TableDAO {

    private final JdbcTemplate jdbcTemplate;

    JdbcTemplateTableDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Table insert(TableNumber number) {
        return jdbcTemplate.withPreparedStatement(
            "insert into restaurant_table (table_number) values (?)",
            insertStatement -> doInsert(insertStatement, number)
        );
    }

    private Table doInsert(PreparedStatement statement, TableNumber number) throws SQLException {
        statement.setInt(1, number.toInt());
        statement.execute();
        return new Table(number);
    }

    @Override
    public Collection<Table> findAll() {
        return jdbcTemplate.withPreparedStatement(
            "select table_number from restaurant_table",
            query -> jdbcTemplate.withQuery(query, this::extractTables)
        );
    }

    private Set<Table> extractTables(ResultSet results) throws SQLException {
        final Set<Table> all = new HashSet<>();
        while (results.next()) {
            final int tableNumberValue = results.getInt(1);
            final TableNumber tableNumber = TableNumber.of(tableNumberValue);
            final Table table = new Table(tableNumber);
            all.add(table);
        }
        return all;
    }
}
