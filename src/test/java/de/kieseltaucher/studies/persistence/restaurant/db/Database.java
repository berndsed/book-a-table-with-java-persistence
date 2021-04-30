package de.kieseltaucher.studies.persistence.restaurant.db;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import de.kieseltaucher.studies.persistence.restaurant.service.TableDAO;

public class Database implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        final Class<?> requestedType = parameterContext.getParameter().getType();
        return requestedType.equals(TableDAO.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return getStore(extensionContext)
            .getOrComputeIfAbsent(InMemoryTableDao.class);
    }

    private ExtensionContext.Store getStore(ExtensionContext extensionContext) {
        final ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(
            getClass(), extensionContext.getRequiredTestMethod()
        );
        return extensionContext.getStore(namespace);
    }
}
