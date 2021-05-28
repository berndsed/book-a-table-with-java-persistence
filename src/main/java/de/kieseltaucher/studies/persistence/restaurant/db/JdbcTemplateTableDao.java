package de.kieseltaucher.studies.persistence.restaurant.db;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.kieseltaucher.studies.persistence.restaurant.db.jdbcutil.JdbcTemplate;
import de.kieseltaucher.studies.persistence.restaurant.model.Table;
import de.kieseltaucher.studies.persistence.restaurant.model.TableNumber;
import de.kieseltaucher.studies.persistence.restaurant.service.TableDAO;

class JdbcTemplateTableDao implements TableDAO {

    private final JdbcTemplate jdbcTemplate;

    JdbcTemplateTableDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Table insert(TableNumber number) {
        return jdbcTemplate.withPreparedStatement(
            "insert into restaurant_table (table_number) values (?)",
            insert -> {
                insert.setInt(1, number.toInt());
                insert.execute();
                return new Table(number);
            }
        );
    }

    @Override
    public Collection<Table> findAll() {
        return jdbcTemplate.withPreparedStatement(
            "select table_number from restaurant_table",
            query -> jdbcTemplate.withQuery(
                query,
                results -> {
                    final Set<Table> all = new HashSet<>();
                    while (results.next()) {
                        final int tableNumberValue = results.getInt(1);
                        final TableNumber tableNumber = TableNumber.of(tableNumberValue);
                        all.add(new Table(tableNumber));
                    }
                    return all;
                })
        );
    }
}
