package de.kieseltaucher.studies.persistence.restaurant.model;

import static de.kieseltaucher.studies.persistence.restaurant.model.Mealtime.BREAKFAST;

import java.time.LocalDate;

class ReservationRequestBuilder {

    private Mealtime mealtime = BREAKFAST;
    private LocalDate date = LocalDate.of(1990, 12, 11);

    ReservationRequestBuilder withMealtime(Mealtime mealtime) {
        this.mealtime = mealtime;
        return this;
    }

    ReservationRequestBuilder withDayOfMonth(int day) {
        date = date.withDayOfMonth(day);
        return this;
    }

    ReservationRequest build() {
        final ReservationTime at = new ReservationTime(date, mealtime);
        return new ReservationRequest(at);
    }

}
