package eu.europa.ex.dynamicdiscovery;

import org.junit.Before;

import java.sql.Connection;

/**
 * Created by rodrfla on 06/10/2016.
 */
public abstract class AbstractTest {

    @Before
    public void init() throws Exception {
        if (!initialized) {
            Class.forName("org.h2.Driver");
            Connection conn = dataSource.getConnection();
            Liquibase liquibase = new Liquibase("liquibase/db.changelog-master.xml", new ClassLoaderResourceAccessor(), new JdbcConnection(conn));
            liquibase.update("");
            liquibase = new Liquibase("liquibase/db.changelog-test-data.xml", new ClassLoaderResourceAccessor(), new JdbcConnection(conn));
            liquibase.update("");
            initialized = true;
        }
        ((DnsMessageSenderServiceMock) dnsMessageSenderService).reset();
    }
}
