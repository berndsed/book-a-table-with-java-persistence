package de.kieseltaucher.studies.persistence.restaurant.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import de.kieseltaucher.studies.persistence.restaurant.db.jdbcutil.JdbcTemplate;
import de.kieseltaucher.studies.persistence.restaurant.model.ReservationRequest;
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
    public void insertReservation(TableNumber tableNumber, ReservationRequest reservation) {
        jdbcTemplate.<Void>withPreparedStatement(
            "insert into reservation (table_number, at_date, mealtime) values (?, ?, ?)",
            insert -> doInsertReservation(insert, tableNumber, reservation));
    }

    private Void doInsertReservation(PreparedStatement insert, TableNumber tableNumber, ReservationRequest reservation)
        throws SQLException {
        insert.setInt(1, tableNumber.toInt());
        insert.setObject(2, reservation.atDay());
        insert.setString(3, reservation.forMealtime().name());
        insert.execute();
        return null;
    }

    @Override
    public Collection<Table> findAll() {
        return jdbcTemplate.withPreparedStatement(
            "select restaurant_table.table_number, at_date, mealtime " +
                "from restaurant_table " +
                "left outer join reservation on restaurant_table.table_number = reservation.table_number",
            query -> jdbcTemplate.withQuery(query, this::extractTables)
        );
    }

    private Collection<Table> extractTables(ResultSet results) throws SQLException {
        final TableResultRowMapper mapper = new TableResultRowMapper();
        while (results.next()) {
            mapper.addRow(results);
        }
        return mapper.tables();
    }
}
