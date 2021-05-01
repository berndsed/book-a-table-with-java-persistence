package de.kieseltaucher.studies.persistence.restaurant.db;

import java.sql.Connection;
import java.util.Set;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import de.kieseltaucher.studies.persistence.restaurant.service.TableDAO;

public class Database implements ParameterResolver {

    private static final Set<Class> SUPPORTED_TYPES = Set.of(TableDAO.class, Connection.class);

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
        return getStore(extensionContext)
            .getOrComputeIfAbsent(InMemoryTableDao.class);
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
