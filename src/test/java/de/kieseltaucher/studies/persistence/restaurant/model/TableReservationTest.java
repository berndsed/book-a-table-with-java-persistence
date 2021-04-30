package de.kieseltaucher.studies.persistence.restaurant.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    private Table aTable() {
        return new Table();
    }

    private ReservationRequest aReservationRequest() {
        return new ReservationRequest();
    }

}
