package de.kieseltaucher.studies.persistence.restaurant.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import de.kieseltaucher.studies.persistence.restaurant.model.Table;
import de.kieseltaucher.studies.persistence.restaurant.model.TableNumber;
import de.kieseltaucher.studies.persistence.restaurant.service.TableDAO;

class JdbcTableDao implements TableDAO {

    private final Supplier<Connection> connectionSource;

    JdbcTableDao(Supplier<Connection> connectionSource) {
        this.connectionSource = connectionSource;
    }

    @Override
    public Table insert(TableNumber number) {
        try (Connection con = open();
            PreparedStatement insert = con.prepareStatement("insert into restaurant_table (table_number) values (?)")) {
            insert.setInt(1, number.toInt());
            insert.execute();
        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }
        return new Table(number);
    }

    @Override
    public Collection<Table> findAll() {
        final Set<Table> all = new HashSet<>();
        try (Connection con = open();
            PreparedStatement select = con.prepareStatement("select table_number from restaurant_table");
             ResultSet results = select.executeQuery()) {
            while (results.next()) {
                final int tableNumberValue = results.getInt(1);
                final TableNumber tableNumber = TableNumber.of(tableNumberValue);
                all.add(new Table(tableNumber));
            }
        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }
        return all;
    }

    private Connection open() {
        return connectionSource.get();
    }
}
