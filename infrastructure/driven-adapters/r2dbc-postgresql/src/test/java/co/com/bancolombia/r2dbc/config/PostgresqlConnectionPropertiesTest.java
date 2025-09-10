package co.com.bancolombia.r2dbc.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostgresqlConnectionPropertiesTest {

    @Test
    void testConstructor() {
        String host = "localhost";
        Integer port = 5432;
        String database = "testdb";
        String schema = "public";
        String username = "user";
        String password = "pass";
        String options = "ssl=true";

        PostgresqlConnectionProperties props = new PostgresqlConnectionProperties(
                host, port, database, schema, username, password, options);

        assertEquals(host, props.host());
        assertEquals(port, props.port());
        assertEquals(database, props.database());
        assertEquals(schema, props.schema());
        assertEquals(username, props.username());
        assertEquals(password, props.password());
        assertEquals(options, props.options());
    }
}
