package de.kieseltaucher.studies.persistence.restaurant.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;

import org.junit.jupiter.api.Test;

import de.kieseltaucher.studies.persistence.restaurant.db.InMemoryTableDao;
import de.kieseltaucher.studies.persistence.restaurant.model.TableNumber;

class ReservationServiceTest {

    private final TableDAO tableDAO = new InMemoryTableDao();
    private final ReservationService service = new ReservationService(tableDAO);

    @Test
    void noTables() {
        assertThat(service.listAllTables(), emptyString());
    }

    @Test
    void createdTableIsListed() {
        tableDAO.insert(TableNumber.of(1));
        assertThat(service.listAllTables(), containsString("Table 1"));
    }

}
