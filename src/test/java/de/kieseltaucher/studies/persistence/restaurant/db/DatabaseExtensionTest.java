package de.kieseltaucher.studies.persistence.restaurant.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(Database.class)
class DatabaseExtensionTest {

    private static Connection OTHER_CONNECTION;

    @Test
    void preconditions(Connection connection) {
        assertNotNull(connection, "connection");
    }

    @Nested
    class Connections {

        @Nested
        class ArePrivateForEachTest {

            private Connection connection;

            @BeforeEach
            void setUp(Connection connection) {
                this.connection = connection;
            }

            @AfterEach
            void tearDown() throws SQLException {
                connection.close();
            }

            @Test
            void test1() throws SQLException {
                insert("record-of-test-1");
                assertThat(readValues(), contains("record-of-test-1"));
            }

            @Test
            void test2() throws SQLException {
                insert("record-of-test-2");
                assertThat(readValues(), contains("record-of-test-2"));
            }

            @Test
            void isBoundToTest(Connection other) throws SQLException {
                insert("bound-to-test");
                this.connection = other;
                assertThat(readValues(), contains("bound-to-test"));
            }

            private void insert(String value) throws SQLException {
                try (PreparedStatement insert = connection.prepareStatement("insert into test_table values (?)")) {
                    insert.setString(1, value);
                    insert.executeUpdate();
                }
                connection.commit();
            }

            private Collection<String> readValues() throws SQLException {
                final Collection<String> values = new ArrayList<>();
                try (PreparedStatement query = connection.prepareStatement("select v from test_table");
                     ResultSet results = query.executeQuery()) {
                    while(results.next()) {
                        values.add(results.getString(1));
                    }
                }
                return values;
            }
        }

        @Nested
        class AreClosedAtEndOfTest {

            @Test
            @Order(1)
            void inTest(Connection connection) throws SQLException {
                OTHER_CONNECTION = connection;
                assertFalse(connection.isClosed(), "closed");
            }

            @Test
            @Order(2)
            void afterTest() throws SQLException {
                assert OTHER_CONNECTION != null;
                assertTrue(OTHER_CONNECTION.isClosed(), "closed");
            }

        }

    }

}
