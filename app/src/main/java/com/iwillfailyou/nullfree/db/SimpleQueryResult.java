package com.nikialeksey.nullfree.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SimpleQueryResult implements QueryResult {

    private final Statement statement;
    private final ResultSet resultSet;

    public SimpleQueryResult(final Statement statement, final ResultSet resultSet) {
        this.statement = statement;
        this.resultSet = resultSet;
    }

    @Override
    public ResultSet rs() {
        return resultSet;
    }

    @Override
    public Statement stmnt() {
        return statement;
    }

    @Override
    public void close() throws DbException {
        try {
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new DbException("Can not close the result set and statement.", e);
        }
    }
}
