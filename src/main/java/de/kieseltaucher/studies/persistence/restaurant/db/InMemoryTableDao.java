package de.kieseltaucher.studies.persistence.restaurant.db;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.kieseltaucher.studies.persistence.restaurant.model.ReservationRequest;
import de.kieseltaucher.studies.persistence.restaurant.model.ReservationTime;
import de.kieseltaucher.studies.persistence.restaurant.model.Table;
import de.kieseltaucher.studies.persistence.restaurant.model.TableNumber;
import de.kieseltaucher.studies.persistence.restaurant.service.TableDAO;

class InMemoryTableDao implements TableDAO {

    private final Map<TableNumber, Table> tablesByNumber = new ConcurrentHashMap<>();
    private final Map<TableNumber, Set<ReservationTime>> reservationTimesByTableNumber = new ConcurrentHashMap<>();

    @Override
    public Table insert(TableNumber number) {
        final Table table = new Table(number);
        tablesByNumber.put(number, table);
        reservationTimesByTableNumber.put(number, new HashSet<>());
        return table;
    }

    @Override
    public synchronized boolean insertReservation(TableNumber tableNumber, ReservationRequest reservation) {
        final Set<ReservationTime> reservationTimes = reservationTimesByTableNumber.get(tableNumber);
        return reservationTimes.add(reservation.at());
    }

    @Override
    public Collection<Table> findAll() {
        return Collections.unmodifiableCollection(tablesByNumber.values());
    }
}
