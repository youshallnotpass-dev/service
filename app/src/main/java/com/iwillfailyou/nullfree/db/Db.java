package com.iwillfailyou.nullfree.db;

public interface Db {
    QueryResult read(String query, String[] args) throws DbException;

    void write(String query, String[] args) throws DbException;
}
