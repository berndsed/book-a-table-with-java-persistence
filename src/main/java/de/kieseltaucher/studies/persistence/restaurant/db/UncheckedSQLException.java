package de.kieseltaucher.studies.persistence.restaurant.db;

import static java.util.function.Predicate.not;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Predicate;

class UncheckedSQLException extends RuntimeException {

    private static Predicate<Throwable> isSuppressed(final Throwable[] suppressed) {
        return throwable -> Arrays.stream(suppressed)
            .anyMatch(s -> s == throwable);
    }

    private static Predicate<Throwable> isSuppressed(final Collection<Throwable> suppressed) {
        return throwable -> suppressed.stream().anyMatch(s -> s == throwable);
    }

    UncheckedSQLException(SQLException cause) {
        super(cause);
        addChainedExceptions(cause);
    }

    private void addChainedExceptions(SQLException cause) {
        final Iterator<Throwable> chained = cause.iterator();
        final Predicate<Throwable> notAlreadySuppressed = not(isSuppressed(getSuppressed()));
        final Predicate<Throwable> notCause = curr -> curr != cause;
        addSuppressed(chained, notAlreadySuppressed.and(notCause));
    }

    private void addSuppressed(Iterator<Throwable> chained, Predicate<Throwable> relevant) {
        final Collection<Throwable> suppressedOfChained = new HashSet<>();
        final Predicate<Throwable> notAlreadySupppresedInChained = not(isSuppressed(suppressedOfChained));
        final Predicate<Throwable> shallAdd = relevant.and(notAlreadySupppresedInChained);
        while(chained.hasNext()) {
            final Throwable next = chained.next();
            suppressedOfChained.addAll(Arrays.asList(next.getSuppressed()));
            if (shallAdd.test(next)) {
                addSuppressed(next);
            }
        }
    }
}
