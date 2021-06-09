package de.kieseltaucher.studies.persistence.restaurant.model;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Set;

public class Table {

    private final Set<ReservationTime> reservations = new HashSet<>();

    private final TableNumber number;

    public Table(TableNumber number) {
        this.number = number;
    }

    public TableNumber getNumber() {
        return number;
    }

    public boolean reserve(ReservationRequest reservationRequest) {
        requireNonNull(reservationRequest);
        return reservations.add(reservationRequest.at());
    }

    public String renderAsString() {
        return String.format("Table %s", number.toString());
    }

}
