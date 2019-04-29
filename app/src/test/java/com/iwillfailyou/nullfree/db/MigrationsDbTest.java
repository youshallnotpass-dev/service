package com.nikialeksey.nullfree.db;

import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;

public class MigrationsDbTest {
    @Test
    public void simpleMigrations() throws Exception {
        final Db db = new MigrationsDb(
            new SqliteDb(),
            new SimpleMigrations(
                new Migration() {
                    @Override
                    public int number() {
                        return 0;
                    }

                    @Override
                    public void execute(final Db db) throws DbException {
                        db.write("CREATE TABLE names (name TEXT NOT NULL)", new String[]{});
                    }
                },
                new Migration() {
                    @Override
                    public int number() {
                        return 1;
                    }

                    @Override
                    public void execute(final Db db) throws DbException {
                        db.write("ALTER TABLE names ADD lastname TEXT NOT NULL DEFAULT ''", new String[]{});
                    }
                }
            ),
            2
        );

        db.write("INSERT INTO names VALUES(?, ?)", new String[]{"Alexey", "Nikitin"});
        try (
            final QueryResult queryResult = db.read("SELECT * FROM names", new String[]{})
        ) {
            final ResultSet rs = queryResult.rs();
            Assert.assertThat(rs.next(), IsEqual.equalTo(true));
            Assert.assertThat(rs.getString("name"), IsEqual.equalTo("Alexey"));
            Assert.assertThat(rs.getString("lastname"), IsEqual.equalTo("Nikitin"));
        }
    }
}