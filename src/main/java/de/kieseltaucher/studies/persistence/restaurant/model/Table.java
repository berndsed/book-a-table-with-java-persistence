package de.kieseltaucher.studies.persistence.restaurant.model;

import static java.util.Objects.requireNonNull;

class Table {

    private boolean reserved;

    boolean reserve(ReservationRequest reservationRequest) {
        requireNonNull(reservationRequest);
        if (reserved) {
            return false;
        }
        this.reserved = true;
        return true;
    }

}
