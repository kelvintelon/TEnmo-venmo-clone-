package com.techelevator;

import org.junit.After;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;


import org.springframework.jdbc.datasource.SingleConnectionDataSource;



import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;


public abstract class BaseDaoTests {

    static SingleConnectionDataSource dataSource;

    @BeforeClass
    public static void setupDataSource() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:8080/");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        dataSource.setAutoCommit(false);
    }
    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
    }
    @AfterClass
    public static void closeDataSource() {
        dataSource.destroy();
    }
    protected DataSource getDataSource() {
        return dataSource;
    }


}
