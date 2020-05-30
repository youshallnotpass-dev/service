package com.iwillfailyou.nullfree.repo;

import com.iwillfailyou.IwfyException;
import com.iwillfailyou.nullfree.NullfreeBadge;
import com.iwillfailyou.nullfree.nulls.DbNulls;
import com.iwillfailyou.nullfree.nulls.Nulls;
import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdException;
import com.nikialeksey.jood.QueryResult;
import com.nikialeksey.jood.args.IntArg;
import com.nikialeksey.jood.args.StringArg;
import com.nikialeksey.jood.sql.JdSql;
import org.takes.misc.Sprintf;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbRepo implements Repo {

    private final Db db;
    private final String path;

    public DbRepo(final Db db, final String path) {
        this.db = db;
        this.path = path;
    }

    @Override
    public String badgeUrl() throws IwfyException {
        try (
            final QueryResult result = db.read(
                new JdSql(
                    "SELECT * FROM repo WHERE path = ?",
                    new StringArg(path)
                )
            )
        ) {
            final ResultSet rs = result.rs();
            if (!rs.next()) {
                throw new IwfyException("Repo does not contain the badge.");
            }
            return rs.getString("badgeUrl");
        } catch (SQLException | JdException e) {
            throw new IwfyException("Can not get the badge.", e);
        }
    }

    @Override
    public void updateBadge(final String url) throws IwfyException {
        try {
            db.write(
                new JdSql(
                    "INSERT OR REPLACE INTO repo(path, badgeUrl, threshold) " +
                        "VALUES(" +
                        "?, " +
                        "?, " +
                        "IFNULL((SELECT threshold FROM repo WHERE path = ?), 0)" +
                        ")",
                    new StringArg(path),
                    new StringArg(url),
                    new StringArg(path)
                )
            );
        } catch (JdException e) {
            throw new IwfyException(
                new Sprintf(
                    "Can not update the badge in path '%s'",
                    path
                ).toString(),
                e
            );
        }
    }

    @Override
    public void updateThreshold(final int threshold) throws IwfyException {
        try {
            db.write(
                new JdSql(
                    "INSERT OR REPLACE INTO repo(path, badgeUrl, threshold) " +
                        "VALUES(" +
                        "?, " +
                        "IFNULL((SELECT badgeUrl FROM repo WHERE path = ?), ''), " +
                        "?)",
                    new StringArg(path),
                    new StringArg(path),
                    new IntArg(threshold)
                )
            );
        } catch (JdException e) {
            throw new IwfyException(
                new Sprintf(
                    "Can not update the badge in path '%s'",
                    path
                ).toString(),
                e
            );
        }
    }

    @Override
    public Nulls nulls() {
        return new DbNulls(db, path);
    }

    @Override
    public void calcBadge() throws IwfyException {
        try (
            final QueryResult result = db.read(
                new JdSql(
                    "SELECT * FROM repo WHERE path = ?",
                    new StringArg(path)
                )
            )
        ) {
            final ResultSet rs = result.rs();
            if (!rs.next()) {
                throw new IwfyException(
                    new Sprintf(
                        "Can not find the repo '%s'.",
                        path
                    ).toString()
                );
            }
            updateBadge(new NullfreeBadge(nulls(), rs.getInt("threshold")).asString());
        } catch (SQLException | JdException e) {
            throw new IwfyException(
                new Sprintf(
                    "Can not calc the badge by path '%s'",
                    path
                ).toString(),
                e
            );
        }
    }
}
