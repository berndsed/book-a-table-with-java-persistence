package de.kieseltaucher.studies.persistence.restaurant.db;

import static de.kieseltaucher.studies.persistence.restaurant.db.jdbcutil.SQLStates.isIntegrityConstraintViolation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Supplier;

import de.kieseltaucher.studies.persistence.restaurant.db.jdbcutil.UncheckedSQLException;
import de.kieseltaucher.studies.persistence.restaurant.model.ReservationRequest;
import de.kieseltaucher.studies.persistence.restaurant.model.Table;
import de.kieseltaucher.studies.persistence.restaurant.model.TableNumber;
import de.kieseltaucher.studies.persistence.restaurant.service.TableDAO;

class JdbcTableDao implements TableDAO {

    private final Supplier<Connection> connectionSource;

    JdbcTableDao(Supplier<Connection> connectionSource) {
        this.connectionSource = connectionSource;
    }

    @Override
    public Table insert(TableNumber number) {
        try (Connection con = open();
            PreparedStatement insert = con.prepareStatement("insert into restaurant_table (table_number) values (?)")) {
            insert.setInt(1, number.toInt());
            insert.execute();
        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }
        return new Table(number);
    }

    @Override
    public boolean insertReservation(TableNumber tableNumber, ReservationRequest reservation) {
        try (Connection con = open();
             PreparedStatement insert = con.prepareStatement(
                 "insert into reservation (table_number, at_date, mealtime) values (?, ?, ?)")) {
            insert.setInt(1, tableNumber.toInt());
            insert.setObject(2, reservation.atDay());
            insert.setString(3, reservation.forMealtime().name());
            insert.execute();
        } catch (SQLException e) {
            if (isIntegrityConstraintViolation(e)) {
                return false;
            }
            throw new UncheckedSQLException(e);
        }
        return true;
    }

    @Override
    public Collection<Table> findAll() {
        final TableResultRowMapper mapper = new TableResultRowMapper();
        try (Connection con = open();
             PreparedStatement select = con.prepareStatement(
                 "select restaurant_table.table_number, at_date, mealtime " +
                     "from restaurant_table " +
                     "left outer join reservation on restaurant_table.table_number = reservation.table_number");
             ResultSet results = select.executeQuery()) {
            while (results.next()) {
                mapper.addRow(results);
            }
        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }
        return mapper.tables();
    }

    private Connection open() {
        return connectionSource.get();
    }
}
