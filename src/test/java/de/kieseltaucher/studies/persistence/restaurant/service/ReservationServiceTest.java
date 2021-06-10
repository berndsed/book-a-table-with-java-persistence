package de.kieseltaucher.studies.persistence.restaurant.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    private final ReservationRequestBuilder requestBuilder = new ReservationRequestBuilder()
        .withDayOfMonth(1)
        .withMealtime(Mealtime.BREAKFAST);

    @BeforeEach
    void setUp(TableDAO tableDAO) {
        service = new ReservationService(tableDAO);
    }

    @TestTemplate
    @ExtendWith(PersistenceTypes.class)
    void noTables() {
        assertThat(service.listAllTables(), emptyString());
        assertThat(reservedTables(), arrayWithSize(0));
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
        final ReservationRequest breakfastReservation = requestBuilder.build();
        final ReservationRequest lunchtimeReservation = requestBuilder.withMealtime(Mealtime.LUNCH).build();

        assertThat("reservation for breakfast",
                   service.reserve(breakfastReservation), containsString("Table 1"));
        assertThat("reservation for breakfast when already reserved",
                   service.reserve(breakfastReservation), not(containsString("Table 1")));
        assertThat("reservation for lunchtime when already reserved for breakfast",
                   service.reserve(lunchtimeReservation), containsString("Table 1"));
    }

    @TestTemplate
    @ExtendWith(PersistenceTypes.class)
    void onlyOneTableIsReserved(TableDAO tableDAO) {
        tableDAO.insert(TableNumber.of(1));
        tableDAO.insert(TableNumber.of(2));

        assertThat("first reservation", reservedTables(), arrayWithSize(1));
        assertThat("second reservation", reservedTables(), arrayWithSize(1));
        assertThat("third reservation", reservedTables(), arrayWithSize(0));
    }

    @TestTemplate
    @ExtendWith(PersistenceTypes.class)
    void onlyOneConcurrentReservationWins(TableDAO tableDAO) throws InterruptedException, TimeoutException, ExecutionException {
        final int threads = 3;
        final ExecutorService executor = Executors.newFixedThreadPool(threads);
        for (int tableNo = 1; ++tableNo <= 20; ++tableNo) {
            tableDAO.insert(TableNumber.of(tableNo));
            final Collection<Callable<Integer>> reservationRequests = new ArrayList<>();
            for (int iter = 0; iter < threads; ++iter) {
                reservationRequests.add(() -> reservedTables().length);
            }

            final Collection<Future<Integer>> reservationTasks = executor.invokeAll(reservationRequests, 500, TimeUnit.MILLISECONDS);

            int totalReserved = 0;
            for (Future<Integer> task : reservationTasks) {
                totalReserved += task.get(20, TimeUnit.MILLISECONDS);
            }

            assertEquals(1, totalReserved);
        }
    }

    private String[] reservedTables() {
        final String reservations = service.reserve(requestBuilder.build());
        return reservations.isEmpty() ? new String[0] : reservations.split("\n");
    }
}
