package dev.youshallnotpass.repo;

import dev.youshallnotpass.YsnpDb;
import dev.youshallnotpass.SqliteDb;
import dev.youshallnotpass.inspection.Inspection;
import dev.youshallnotpass.inspection.YsnpViolation;
import dev.youshallnotpass.migrations.Migration0;
import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdMigrations;
import com.nikialeksey.jood.MigrationsDb;
import com.nikialeksey.jood.QueryResult;
import com.nikialeksey.jood.args.StringArg;
import com.nikialeksey.jood.sql.JdSql;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

public final class DbReposTest {

    @Test
    public void okAfterFourMigration() throws Exception {
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

        final Repos thirdMigration = new DbRepos(new YsnpDb(db));

        final String badgeUrl = thirdMigration.repo("repo1").nullfree().badgeUrl();

        Assert.assertThat(badgeUrl, IsEqual.equalTo("badge1"));
    }

    @Test
    public void addNullDescription() throws Exception {
        final Db db = new YsnpDb(new SqliteDb());
        final Repos repos = new DbRepos(db);
        repos.repo("repo1").nullfree().violations().add(new YsnpViolation("null1"));

        final QueryResult qr = db.read(new JdSql("SELECT * FROM null_description"));
        Assert.assertThat(qr.rs().next(), IsEqual.equalTo(true));
        Assert.assertThat(qr.rs().getString("repo"), IsEqual.equalTo("repo1"));
        Assert.assertThat(qr.rs().getString("description"), IsEqual.equalTo("null1"));
    }

    @Test
    public void clearNullDescriptions() throws Exception {
        final Db db = new YsnpDb(new SqliteDb());
        final Repos repos = new DbRepos(db);
        final String repoId = "repo1";
        final Inspection nullfree = repos.repo(repoId).nullfree();
        nullfree.violations().add(new YsnpViolation("null1"));
        nullfree.violations().clear();

        final QueryResult nullRes = db.read(
            new JdSql("SELECT * FROM null_description")
        );
        Assert.assertThat(nullRes.rs().next(), IsEqual.equalTo(false));
        final QueryResult repoRes = db.read(
            new JdSql(
                "SELECT * FROM nullfree WHERE repo = ?",
                new StringArg(repoId)
            )
        );
        Assert.assertThat(repoRes.rs().next(), IsEqual.equalTo(true));
        Assert.assertThat(repoRes.rs().getString("badgeUrl"), IsEqual.equalTo(""));
    }

}