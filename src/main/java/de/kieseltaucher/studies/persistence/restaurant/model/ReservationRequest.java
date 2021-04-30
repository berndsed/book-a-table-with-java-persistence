package de.kieseltaucher.studies.persistence.restaurant.model;

import static java.util.Objects.requireNonNull;

class ReservationRequest {

    private final ReservationTime at;

    ReservationRequest(ReservationTime at) {
        requireNonNull(at);
        this.at = at;
    }

    ReservationTime at() {
        return at;
    }

}
