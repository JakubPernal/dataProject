package com.pernal.persistence.repository;

import com.pernal.persistence.DataColumns;
import com.pernal.persistence.connection.DbConnection;
import com.pernal.persistence.entity.DataEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DataRepositoryImpl implements DataRepository {

    // SCHEMA NAME: testdb

    /*  CREATE TABLE QUERY

        CREATE TABLE data (
            primary_key VARCHAR2(120) PRIMARY KEY,
            name VARCHAR(120),
            description VARCHAR(120),
            updated_timestamp TIMESTAMP
        )
     */

    private Logger logger = LoggerFactory.getLogger(DataRepositoryImpl.class);

    private DbConnection dbConnection;

    public DataRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public DataEntity getDataByPrimaryKey(String key) {
        try (Connection connection = dbConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("select * from testdb.data where primary_key = ?")) {
            preparedStatement.setString(DataColumns.PRIMARY_KEY.getIndex(), key);
            ResultSet resultSet = preparedStatement.executeQuery();

            return retrieveDataEntityFromResultSet(resultSet);
        } catch (SQLException e) {
            logger.error("Error while selecting data by id: {}", key);
            return null;
        }
    }

    @Override
    public void deleteDataByPrimaryKey(String key) {
        try (Connection connection = dbConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("delete from testdb.data where primary_key = ?")) {
            preparedStatement.setString(DataColumns.PRIMARY_KEY.getIndex(), key);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while deleting data by id: {}", key);
        }
    }

    @Override
    public void createDataBatch(List<DataEntity> dataEntities) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("insert into testdb.data (primary_key, `name`, description, updated_timestamp) values (?,?,?,?)")) {

            prepareStatementToExecute(dataEntities, preparedStatement);

            logger.info("\n\nBatch insert started...\n\n");

            preparedStatement.executeBatch();

            logger.info("\n\nBatch insert finished\n\n");
        } catch (SQLException e) {
            logger.warn("Error while inserting data by batch - entities haven't been persisted. Error: {}", e.getMessage());
            processIndividualMode(dataEntities);
        }
    }

    private void prepareStatementToExecute(List<DataEntity> dataEntities, PreparedStatement preparedStatement) {
        dataEntities.forEach(dataEntity -> {
            try {
                preparedStatement.clearParameters();

                fillEachStatement(dataEntity, preparedStatement);

                preparedStatement.addBatch();
            } catch (SQLException e) {
                logger.error("Error while preparing statement: ", e);
            }
        });
    }

    private void processIndividualMode(List<DataEntity> dataEntities) {
        logger.info("\n\nIndividual inserts started...\n\n");

        dataEntities.forEach(this::createData);

        logger.info("\n\nIndividual inserts finished\n\n");
    }

    private void createData(DataEntity dataEntity) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("insert into testdb.data (primary_key, `name`, description, updated_timestamp) values (?,?,?,?)")) {

            fillEachStatement(dataEntity, preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while inserting data with primary key: {}, Error: {}", dataEntity.getPrimaryKey(), e.getMessage());
        }
    }

    private void fillEachStatement(DataEntity dataEntity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(DataColumns.PRIMARY_KEY.getIndex(), dataEntity.getPrimaryKey());
        preparedStatement.setString(DataColumns.NAME.getIndex(), dataEntity.getName());
        preparedStatement.setString(DataColumns.DESCRIPTION.getIndex(), dataEntity.getDescription());
        preparedStatement.setTimestamp(DataColumns.UPDATED_TIMESTAMP.getIndex(), dataEntity.getUpdateTimestamp());
    }

    private DataEntity retrieveDataEntityFromResultSet(ResultSet resultSet) throws SQLException {
        resultSet.next();
        DataEntity dataEntity = new DataEntity();

        dataEntity.setPrimaryKey(resultSet.getString(DataColumns.PRIMARY_KEY.toString()));
        dataEntity.setName(resultSet.getString(DataColumns.NAME.toString()));
        dataEntity.setDescription(resultSet.getString(DataColumns.DESCRIPTION.toString()));
        dataEntity.setUpdateTimestamp(resultSet.getTimestamp(DataColumns.UPDATED_TIMESTAMP.toString()));

        return dataEntity;
    }
}
