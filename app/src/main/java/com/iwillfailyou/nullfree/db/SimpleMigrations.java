package com.nikialeksey.nullfree.db;

import org.cactoos.list.ListOf;

import java.util.Collection;

public class SimpleMigrations implements Migrations {

    private final Collection<Migration> migrations;

    public SimpleMigrations(final Migration... migrations) {
        this(new ListOf<>(migrations));
    }

    public SimpleMigrations(final Collection<Migration> migrations) {
        this.migrations = migrations;
    }

    @Override
    public void apply(
        final int number,
        final Db db
    ) throws DbException {
        for (final Migration migration : migrations) {
            if (migration.number() == number) {
                migration.execute(db);
            }
        }
    }
}
