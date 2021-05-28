package de.kieseltaucher.studies.persistence.restaurant.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyArray;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(Persistence.class)
class UncheckedSQLExceptionTest {

    @Test
    void stacktraceContainsChainedSQLExceptions() {
        final SQLException cause = new SQLException("Root cause");
        final SQLException chainedException = new SQLException("Another trouble-maker");
        cause.setNextException(chainedException);

        assertThat(stacktraceOf(new UncheckedSQLException(cause)), containsString(chainedException.getMessage()));
    }

    @Test
    void batchExceptions(Connection con) throws SQLException {
        final Statement stm = con.createStatement();
        stm.addBatch("insert into UNKNOWNTABLE1 values(1)");
        stm.addBatch("insert into UNKNOWNTABLE2 values(2)");

        final SQLException sqlException = assertThrows(SQLException.class, stm::executeBatch);
        final String stackTrace = stacktraceOf(new UncheckedSQLException(sqlException));
        assertThat(stackTrace, containsString("UNKNOWNTABLE1"));
        assertThat(stackTrace, containsString("UNKNOWNTABLE2"));

    }

    @Test
    void noChainedException() {
        final var unchecked = new UncheckedSQLException(new SQLException("any"));
        assertThat(unchecked.getSuppressed(), emptyArray());
    }

    @Test
    void willNotDuplicateAlreadySuppressedExceptions() {
        final SQLException root = new SQLException("root");
        final SQLException chainedAndSuppressed = new SQLException("Another trouble-maker");
        root.setNextException(chainedAndSuppressed);
        root.addSuppressed(chainedAndSuppressed);

        final var unchecked = new UncheckedSQLException(root);
        assertThat(unchecked.getSuppressed(), emptyArray());
        assertThat(stacktraceOf(unchecked), containsString("Another trouble-maker"));
    }

    private String stacktraceOf(Exception e) {
        final StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

}
