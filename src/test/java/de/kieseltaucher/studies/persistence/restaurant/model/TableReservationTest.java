package de.kieseltaucher.studies.persistence.restaurant.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

class TableReservationTest {

    @Test
    void aNewTableCanBeReserved() {
        final Table table = aTable();
        assertTrue(table.reserve(aReservationRequest()), "reserved");
    }

    @Test
    void aTableCanBeReservedOnlyOnce() {
        final Table table = aTable();
        final ReservationRequest reservationRequest = aReservationRequest();
        assertTrue(table.reserve(reservationRequest), "first reservation success");
        assertFalse(table.reserve(reservationRequest), "second reservation success");
    }

    @Test
    void aTableCanBeReservedForEachMealtimeADayOnlyOnce() {
        final Table table = aTable();
        final Collection<Mealtime> alreadyReserved = new ArrayList<>();
        for (Mealtime mealtime : Mealtime.values()) {
            assertTrue(table.reserve(aReservationRequest(mealtime)), String.format("first reservation of %s (already reserved %s)", mealtime, alreadyReserved));
            alreadyReserved.add(mealtime);
            assertFalse(table.reserve(aReservationRequest(mealtime)), String.format("second reservation of %s", mealtime));
        }
    }

    @Test
    void aTableCanBeReservedForSameMealtimeAtDifferentDays() {
        final Table table = aTable();
        assertTrue(table.reserve(aReservationRequestAtDay(1)), "success of reservation at day 1");
        assertTrue(table.reserve(aReservationRequestAtDay(2)), "success of reservation at day 2");
    }

    private Table aTable() {
        return new Table();
    }

    private ReservationRequest aReservationRequest() {
        return aReservationRequest(Mealtime.BREAKFAST);
    }

    private ReservationRequest aReservationRequest(Mealtime breakfast) {
        final ReservationTime at = new ReservationTime(LocalDate.of(1990, 12, 11), breakfast);
        return new ReservationRequest(at);
    }

    private ReservationRequest aReservationRequestAtDay(int day) {
        final LocalDate date = LocalDate.of(1990, 12, day);
        final ReservationTime at = new ReservationTime(date, Mealtime.BREAKFAST);
        return new ReservationRequest(at);
    }
}
