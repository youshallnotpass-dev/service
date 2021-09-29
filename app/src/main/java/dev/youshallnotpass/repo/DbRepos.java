package dev.youshallnotpass.repo;

import dev.youshallnotpass.YsnpException;
import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdException;
import com.nikialeksey.jood.QueryResult;
import com.nikialeksey.jood.args.StringArg;
import com.nikialeksey.jood.sql.JdSql;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class DbRepos implements Repos {

    private final Db db;

    public DbRepos(final Db db) {
        this.db = db;
    }

    @Override
    public Repo repo(final String path) throws YsnpException {
        try (
            final QueryResult qr = db.read(
                new JdSql(
                    "SELECT 1 FROM repo WHERE path = ?",
                    new StringArg(path)
                )
            )
        ) {
            final ResultSet rs = qr.rs();
            if (!rs.next()) {
                db.write(
                    new JdSql(
                        "INSERT INTO repo VALUES(?)",
                        new StringArg(path)
                    )
                );
            }
            return new DbRepo(db, path);
        } catch (final JdException | SQLException e) {
            throw new YsnpException(
                "Could not get the repo by path " + path,
                e
            );
        }
    }
}
