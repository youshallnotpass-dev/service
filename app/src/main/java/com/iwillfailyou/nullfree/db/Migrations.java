package com.iwillfailyou.nullfree.db;

public interface Migrations {
    void apply(int number, Db db) throws DbException;
}
