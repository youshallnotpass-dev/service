package com.nikialeksey.nullfree.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MigrationsDb implements Db {

    private final Db origin;
    private final Migrations migrations;
    private final int dbVersion;

    public MigrationsDb(
        final Db origin,
        final Migrations migrations,
        final int dbVersion
    ) {
        this.origin = origin;
        this.migrations = migrations;
        this.dbVersion = dbVersion;
    }

    @Override
    public QueryResult read(final String query, final String[] args) throws DbException {
        ensureMigrations();
        return origin.read(query, args);
    }

    @Override
    public void write(final String query, final String[] args) throws DbException {
        ensureMigrations();
        origin.write(query, args);
    }

    private void ensureMigrations() throws DbException {
        ensureMigrationsTable();
        int oldVersion = oldVersion();
        if (oldVersion != dbVersion) {
            for (int version = oldVersion; version < dbVersion; version++) {
                migrations.apply(version, origin);
            }
            origin.write("UPDATE migrations SET version = " + dbVersion, new String[]{});
        }
    }

    private int oldVersion() throws DbException {
        try (
            final QueryResult result = origin.read("SELECT version FROM migrations", new String[]{})
        ) {
            final ResultSet rs = result.rs();
            rs.next();
            return rs.getInt("version");
        } catch (SQLException e) {
            throw new DbException("Can not get the old db version.", e);
        }
    }

    private void ensureMigrationsTable() throws DbException {
        origin.write("CREATE TABLE IF NOT EXISTS migrations (version INTEGER NOT NULL DEFAULT 0)", new String[]{});
        try (
            final QueryResult result = origin.read("SELECT 1 FROM migrations", new String[]{})
        ) {
            final ResultSet rs = result.rs();
            if (!rs.next()) {
                origin.write("INSERT INTO migrations (version) VALUES (0)", new String[]{});
            }
        } catch (SQLException e) {
            throw new DbException("Can not initialize migrations table.", e);
        }
    }
}
