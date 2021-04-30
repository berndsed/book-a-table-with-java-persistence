package de.kieseltaucher.studies.persistence.restaurant.db;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.kieseltaucher.studies.persistence.restaurant.model.Table;
import de.kieseltaucher.studies.persistence.restaurant.model.TableNumber;
import de.kieseltaucher.studies.persistence.restaurant.service.TableDAO;

class InMemoryTableDao implements TableDAO {

    private final Map<TableNumber, Table> tablesByNumber = new ConcurrentHashMap<>();

    @Override
    public Table insert(TableNumber number) {
        final Table table = new Table(number);
        tablesByNumber.put(number, table);
        return table;
    }

    @Override
    public Collection<Table> findAll() {
        return Collections.unmodifiableCollection(tablesByNumber.values());
    }
}
