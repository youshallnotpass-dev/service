package com.iwillfailyou.nullfree.db;

public interface Migration {
    int number();

    void execute(Db db) throws DbException;
}