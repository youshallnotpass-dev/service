package com.iwillfailyou.migrations;

import com.iwillfailyou.SqliteDb;
import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdMigrations;
import com.nikialeksey.jood.MigrationsDb;
import com.nikialeksey.jood.QueryResult;
import com.nikialeksey.jood.sql.JdSql;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;

public class Migration3Test {

    @Test
    public void okFrom2to3Migration() throws Exception {
        final Db db = new SqliteDb();

        final Db dbBefore = new MigrationsDb(
            db,
            new JdMigrations(
                new Migration0(),
                new Migration1(),
                new Migration2()
            ),
            3
        );

        dbBefore.write(
            new JdSql(
                "INSERT INTO repo (path, badgeUrl, threshold) " +
                    "VALUES('path1', 'badge', 2)"
            )
        );
        dbBefore.write(
            new JdSql(
                "INSERT INTO null_description (id, repo, description) " +
                    "VALUES ('1', 'path', 'violation 1')," +
                    "('2', 'path1', 'violation 2')"
            )
        );
        dbBefore.write(
            new JdSql(
                "INSERT INTO repo (path, badgeUrl, threshold) " +
                    "VALUES('path2', 'badge', 0)"
            )
        );

        final Db dbAfter = new MigrationsDb(
            db,
            new JdMigrations(
                new Migration0(),
                new Migration1(),
                new Migration2(),
                new Migration3()
            ),
            4
        );

        try (
            final QueryResult qr = dbAfter.read(
                new JdSql("SELECT * FROM nullfree")
            )
        ) {
            final ResultSet rs = qr.rs();
            Assert.assertThat(rs.next(), IsEqual.equalTo(true));
            Assert.assertThat(rs.getString("repo"), IsEqual.equalTo("path1"));
            Assert.assertThat(rs.next(), IsEqual.equalTo(true));
            Assert.assertThat(rs.getString("repo"), IsEqual.equalTo("path2"));
        }
    }
}