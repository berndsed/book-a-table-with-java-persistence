package de.kieseltaucher.studies.persistence.restaurant.db;

import static de.kieseltaucher.studies.persistence.restaurant.db.PersistenceType.IN_MEMORY;

import java.sql.Connection;
import java.util.Set;
import java.util.function.Supplier;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import de.kieseltaucher.studies.persistence.restaurant.db.jdbcutil.JdbcTemplate;
import de.kieseltaucher.studies.persistence.restaurant.service.TableDAO;

public class Persistence implements ParameterResolver {

    private static final Set<Class> SUPPORTED_TYPES = Set.of(TableDAO.class, Connection.class);

    private final PersistenceType persistenceType;

    Persistence() {
        this(IN_MEMORY);
    }

    Persistence(PersistenceType persistenceType) {
        this.persistenceType = persistenceType;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        final Class<?> requestedType = parameterContext.getParameter().getType();
        return SUPPORTED_TYPES.contains(requestedType);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        final Class<?> requestedType = parameterContext.getParameter().getType();
        if (requestedType.equals(TableDAO.class)) {
            return resolveTableDao(extensionContext);
        } else if (requestedType.equals(Connection.class)) {
            return resolveConnection(extensionContext);
        } else {
            throw new UnsupportedOperationException(String.format("Not able to resolve %s", requestedType));
        }
    }

    private TableDAO resolveTableDao(ExtensionContext extensionContext) {
        final Supplier<Connection> connectionSource = new TestContextConnectionSource(this.resolveConnection(extensionContext));
        return getStore(extensionContext)
            .getOrComputeIfAbsent(persistenceType, type -> newTableDao(type, connectionSource), TableDAO.class);
    }

    private TableDAO newTableDao(PersistenceType type, Supplier<Connection> connectionSource) {
        switch (type) {
            case JDBC:
                return new JdbcTableDao(connectionSource);
            case JDBC_TEMPLATE:
                final JdbcTemplate jdbcTemplate = new JdbcTemplate(connectionSource);
                return new JdbcTemplateTableDao(jdbcTemplate);
            case IN_MEMORY:
                return new InMemoryTableDao();
            default:
                throw new IllegalArgumentException(type.toString());
        }
    }

    private Connection resolveConnection(ExtensionContext extensionContext) {
        final DatabaseInstance databaseInstance = (DatabaseInstance) getStore(extensionContext)
            .getOrComputeIfAbsent(DatabaseInstance.class, unusedKey -> DatabaseInstance.newInstance());
         return databaseInstance.connection();
    }

    private ExtensionContext.Store getStore(ExtensionContext extensionContext) {
        final ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(
            getClass(), extensionContext.getRequiredTestMethod()
        );
        return extensionContext.getStore(namespace);
    }
}
