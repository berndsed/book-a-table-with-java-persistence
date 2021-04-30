package de.kieseltaucher.studies.persistence.restaurant.model;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Set;

class Table {

    private final Set<ReservationTime> reservations = new HashSet<>();

    boolean reserve(ReservationRequest reservationRequest) {
        requireNonNull(reservationRequest);
        return reservations.add(reservationRequest.at());
    }

}
