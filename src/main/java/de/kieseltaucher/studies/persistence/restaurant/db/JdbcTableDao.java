package de.kieseltaucher.studies.persistence.restaurant.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.kieseltaucher.studies.persistence.restaurant.db.jdbcutil.UncheckedSQLException;
import de.kieseltaucher.studies.persistence.restaurant.model.Mealtime;
import de.kieseltaucher.studies.persistence.restaurant.model.ReservationRequest;
import de.kieseltaucher.studies.persistence.restaurant.model.ReservationTime;
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
    public void insertReservation(TableNumber tableNumber, ReservationRequest reservation) {
        try (Connection con = open();
             PreparedStatement insert = con.prepareStatement(
                 "insert into reservation (table_number, at_date, mealtime) values (?, ?, ?)")) {
            insert.setInt(1, tableNumber.toInt());
            insert.setObject(2, reservation.atDay());
            insert.setString(3, reservation.forMealtime().name());
            insert.execute();
        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }
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

    private static class TableResultRowMapper {

        private final Map<TableNumber, TableBuilder> buildersByTableNumber = new HashMap<>();

        void addRow(ResultSet row) throws SQLException {
            final int tableNumberValue = row.getInt(1);
            final TableNumber tableNumber = TableNumber.of(tableNumberValue);
            buildersByTableNumber.computeIfAbsent(tableNumber, TableBuilder::new);
            buildersByTableNumber.get(tableNumber).addReservation(row);
        }

        Collection<Table> tables() {
            return buildersByTableNumber
                .values()
                .stream()
                .map(TableBuilder::build)
                .collect(Collectors.toList());
        }

    }

    private static class TableBuilder {

        private final Table table;

        TableBuilder(TableNumber number) {
            this.table = new Table(number);
        }

        void addReservation(ResultSet row) throws SQLException {
            final Date reservedAt = row.getDate(2);
            if (reservedAt == null) {
                return;
            }
            final Mealtime mealtime = Mealtime.valueOf(row.getString(3));
            final ReservationTime reservationTime = new ReservationTime(reservedAt.toLocalDate(), mealtime);
            final ReservationRequest reservation = new ReservationRequest(reservationTime);
            table.reserve(reservation);
        }

        Table build() {
            return table;
        }

    }

    private Connection open() {
        return connectionSource.get();
    }
}
