package de.kieseltaucher.studies.persistence.restaurant.model;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

public class ReservationRequest {

    private final ReservationTime at;

    public ReservationRequest(ReservationTime at) {
        requireNonNull(at);
        this.at = at;
    }

    ReservationTime at() {
        return at;
    }

    public LocalDate atDay() {
        return at.day();
    }

    public Mealtime forMealtime() {
        return at.mealtime();
    }

}
