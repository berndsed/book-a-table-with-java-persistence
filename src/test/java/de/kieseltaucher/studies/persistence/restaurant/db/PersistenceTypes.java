package de.kieseltaucher.studies.persistence.restaurant.db;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

public class PersistenceTypes implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext extensionContext) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {
        return Arrays.stream(PersistenceType.values())
            .map(this::invoationContext);
    }

    private TestTemplateInvocationContext invoationContext(PersistenceType type) {

        final Persistence extension = new Persistence(type);
        return new TestTemplateInvocationContext() {

            @Override
            public String getDisplayName(int invocationIndex) {
                return type.toString();
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return Collections.singletonList(extension);
            }
        };
    }
}
