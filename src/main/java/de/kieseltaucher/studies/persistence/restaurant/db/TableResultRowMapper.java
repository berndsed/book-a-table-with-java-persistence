package de.kieseltaucher.studies.persistence.restaurant.db;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import de.kieseltaucher.studies.persistence.restaurant.model.Mealtime;
import de.kieseltaucher.studies.persistence.restaurant.model.ReservationRequest;
import de.kieseltaucher.studies.persistence.restaurant.model.ReservationTime;
import de.kieseltaucher.studies.persistence.restaurant.model.Table;
import de.kieseltaucher.studies.persistence.restaurant.model.TableNumber;

class TableResultRowMapper {

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
}
