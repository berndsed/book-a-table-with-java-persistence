package de.kieseltaucher.studies.persistence.restaurant.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import de.kieseltaucher.studies.persistence.restaurant.db.PersistenceTypes;
import de.kieseltaucher.studies.persistence.restaurant.model.Mealtime;
import de.kieseltaucher.studies.persistence.restaurant.model.ReservationRequest;
import de.kieseltaucher.studies.persistence.restaurant.model.ReservationRequestBuilder;
import de.kieseltaucher.studies.persistence.restaurant.model.TableNumber;

class ReservationServiceTest {

    private ReservationService service;

    @BeforeEach
    void setUp(TableDAO tableDAO) {
        service = new ReservationService(tableDAO);
    }

    @TestTemplate
    @ExtendWith(PersistenceTypes.class)
    void noTables() {
        assertThat(service.listAllTables(), emptyString());
    }

    @TestTemplate
    @ExtendWith(PersistenceTypes.class)
    void createdTableIsListed(TableDAO tableDAO) {
        tableDAO.insert(TableNumber.of(1));
        assertThat(service.listAllTables(), containsString("Table 1"));
    }

    @TestTemplate
    @ExtendWith(PersistenceTypes.class)
    void tableCanBeReserved(TableDAO tableDAO) {
        tableDAO.insert(TableNumber.of(1));
        final ReservationRequestBuilder requestBuilder = new ReservationRequestBuilder().withDayOfMonth(1)
            .withMealtime(Mealtime.BREAKFAST);
        final ReservationRequest breakfastReservation = requestBuilder.build();
        final ReservationRequest lunchtimeReservation = requestBuilder.withMealtime(Mealtime.LUNCH).build();

        assertThat("reservation for breakfast",
                   service.reserve(breakfastReservation), containsString("Table 1"));
        assertThat("reservation for breakfast when already reserved",
                   service.reserve(breakfastReservation), not(containsString("Table 1")));
        assertThat("reservation for lunchtime when already reserved for breakfast",
                   service.reserve(lunchtimeReservation), containsString("Table 1"));
    }

}
