package de.kieseltaucher.studies.persistence.restaurant.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import de.kieseltaucher.studies.persistence.restaurant.model.Table;
import de.kieseltaucher.studies.persistence.restaurant.model.TableNumber;

class ReservationServiceTest {

    private final Collection<Table> tables = new ArrayList<>();

    private final TableDAO tableDAO = new TableDAO() {

        @Override
        public Table insert(TableNumber number) {
            final Table table = new Table(number);
            tables.add(table);
            return table;
        }

        @Override
        public Collection<Table> findAll() {
            return tables;
        }
    };
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
