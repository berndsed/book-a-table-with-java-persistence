package de.kieseltaucher.studies.persistence.restaurant.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.kieseltaucher.studies.persistence.restaurant.db.Database;
import de.kieseltaucher.studies.persistence.restaurant.model.TableNumber;

@ExtendWith(Database.class)
class ReservationServiceTest {

    private ReservationService service;

    @BeforeEach
    void setUp(TableDAO tableDAO) {
        service = new ReservationService(tableDAO);
    }

    @Test
    void noTables() {
        assertThat(service.listAllTables(), emptyString());
    }

    @Test
    void createdTableIsListed(TableDAO tableDAO) {
        tableDAO.insert(TableNumber.of(1));
        assertThat(service.listAllTables(), containsString("Table 1"));
    }

}
