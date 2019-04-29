package com.nikialeksey.nullfree.db;

import java.sql.ResultSet;
import java.sql.Statement;

public interface QueryResult extends AutoCloseable {
    ResultSet rs();

    Statement stmnt();

    void close() throws DbException;
}
