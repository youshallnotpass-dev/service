package com.iwillfailyou.nullfree.repo;

import com.iwillfailyou.SqliteDb;
import com.iwillfailyou.nullfree.migrations.Migration0;
import com.iwillfailyou.nullfree.migrations.Migration1;
import com.iwillfailyou.nullfree.migrations.Migration2;
import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdMigrations;
import com.nikialeksey.jood.MigrationsDb;
import com.nikialeksey.jood.QueryResult;
import com.nikialeksey.jood.args.StringArg;
import com.nikialeksey.jood.sql.JdSql;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

public class DbReposTest {

    @Test
    public void okAfterThirdMigration() throws Exception {
        final Db db = new SqliteDb();

        final MigrationsDb firstMigration = new MigrationsDb(
            db,
            new JdMigrations(
                new Migration0()
            ),
            1
        );
        firstMigration.write(
            new JdSql(
                "INSERT INTO repo (path, badgeUrl) VALUES(?, ?)",
                new StringArg("repo1"),
                new StringArg("badge1")
            )
        );

        final Repos thirdMigration = new DbRepos(
            new MigrationsDb(
                db,
                new JdMigrations(
                    new Migration0(),
                    new Migration1(),
                    new Migration2()
                ),
                3
            )
        );

        thirdMigration.repo("repo1").updateBadge("badge2");

        final QueryResult result = db.read(new JdSql("SELECT * FROM repo"));
        Assert.assertThat(result.rs().next(), IsEqual.equalTo(true));
        Assert.assertThat(result.rs().getString("badgeUrl"), IsEqual.equalTo("badge2"));
    }

    @Test
    public void addNullDescription() throws Exception {
        final Db db = new SqliteDb();
        final Repos repos = new DbRepos(
            new MigrationsDb(
                db,
                new JdMigrations(
                    new Migration0(),
                    new Migration1(),
                    new Migration2()
                ),
                3
            )
        );
        repos.repo("repo1").updateBadge("badge1");
        repos.repo("repo1").nulls().add("null1");

        final QueryResult qr = db.read(new JdSql("SELECT * FROM null_description"));
        Assert.assertThat(qr.rs().next(), IsEqual.equalTo(true));
        Assert.assertThat(qr.rs().getString("repo"), IsEqual.equalTo("repo1"));
        Assert.assertThat(qr.rs().getString("description"), IsEqual.equalTo("null1"));
    }

    @Test
    public void clearNullDescriptions() throws Exception {
        final Db db = new SqliteDb();
        final Repos repos = new DbRepos(
            new MigrationsDb(
                db,
                new JdMigrations(
                    new Migration0(),
                    new Migration1(),
                    new Migration2()
                ),
                3
            )
        );
        final String repoId = "repo1";
        repos.repo(repoId).updateBadge("badge1");
        repos.repo(repoId).nulls().add("null1");
        repos.repo(repoId).nulls().clear();

        final QueryResult nullRes = db.read(
            new JdSql("SELECT * FROM null_description")
        );
        Assert.assertThat(nullRes.rs().next(), IsEqual.equalTo(false));
        final QueryResult repoRes = db.read(
            new JdSql("SELECT * FROM repo WHERE path = ?", new StringArg(repoId))
        );
        Assert.assertThat(repoRes.rs().next(), IsEqual.equalTo(true));
        Assert.assertThat(repoRes.rs().getString("badgeUrl"), IsEqual.equalTo(""));
    }

}