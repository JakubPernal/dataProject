package com.pernal.connection;

import com.pernal.DataTestConfig;
import com.pernal.persistence.connection.DbConnection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DataTestConfig.class)
public class DatabaseConnectionTest {

    @Autowired
    private DbConnection dbConnection;

    @Test
    public void shouldConnectToDb() {
        Connection connection = dbConnection.getConnection();

        assertNotNull(connection);
    }
}
