package com.nikialeksey.nullfree.db;

public interface Migrations {
    void apply(int number, Db db) throws DbException;
}
