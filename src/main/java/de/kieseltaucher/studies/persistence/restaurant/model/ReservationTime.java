package de.kieseltaucher.studies.persistence.restaurant.model;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.Objects;

class ReservationTime {

    private final LocalDate day;
    private final Mealtime mealtime;

    ReservationTime(LocalDate day, Mealtime mealtime) {
        requireNonNull(day);
        requireNonNull(mealtime);
        this.day = day;
        this.mealtime = mealtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTime that = (ReservationTime) o;
        return day.equals(that.day) &&
            mealtime == that.mealtime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, mealtime);
    }
}
