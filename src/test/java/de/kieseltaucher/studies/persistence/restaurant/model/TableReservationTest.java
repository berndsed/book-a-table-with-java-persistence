package de.kieseltaucher.studies.persistence.restaurant.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

class TableReservationTest {

    private final Table table = new TableBuilder().build();
    private final ReservationRequestBuilder reservationRequestBuilder = new ReservationRequestBuilder();

    @Test
    void aNewTableCanBeReserved() {
        assertTrue(reserve(), "reserved");
    }

    @Test
    void aTableCanBeReservedOnlyOnce() {
        assertTrue(reserve(), "first reservation success");
        assertFalse(reserve(), "second reservation success");
    }

    @Test
    void aTableCanBeReservedForEachMealtimeADayOnlyOnce() {
        final Collection<Mealtime> alreadyReserved = new ArrayList<>();
        for (Mealtime mealtime : Mealtime.values()) {
            reservationRequestBuilder.withMealtime(mealtime);
            assertTrue(reserve(), String.format("first reservation of %s (already reserved %s)", mealtime, alreadyReserved));
            alreadyReserved.add(mealtime);
            assertFalse(reserve(), String.format("second reservation of %s", mealtime));
        }
    }

    @Test
    void aTableCanBeReservedForSameMealtimeAtDifferentDays() {
        reservationRequestBuilder.withDayOfMonth(1);
        assertTrue(reserve(), "success of reservation at day 1");
        reservationRequestBuilder.withDayOfMonth(2);
        assertTrue(reserve(), "success of reservation at day 2");
    }

    private boolean reserve() {
        return table.reserve(reservationRequestBuilder.build());
    }

}
