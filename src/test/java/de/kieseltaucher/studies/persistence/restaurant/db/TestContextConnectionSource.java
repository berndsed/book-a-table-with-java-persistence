package de.kieseltaucher.studies.persistence.restaurant.db;

import java.sql.Connection;
import java.util.function.Supplier;

class TestContextConnectionSource implements Supplier<Connection> {

    private final Connection connection;

    TestContextConnectionSource(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection get() {
        return new ConnectionSession(connection);
    }

    private static class ConnectionSession extends ProxiedConnection {

        private boolean closed;

        ConnectionSession(Connection proxied) {
            super(proxied);
        }

        @Override
        public boolean isClosed() {
            return closed;
        }

        @Override
        public void close() {
            this.closed = true;
        }
    }

}
