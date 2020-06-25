package com.iwillfailyou.inspection.nullfree;

import com.iwillfailyou.IwfyException;
import com.iwillfailyou.inspection.Violation;
import com.iwillfailyou.inspection.Violations;
import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdException;
import com.nikialeksey.jood.QueryResult;
import com.nikialeksey.jood.args.StringArg;
import com.nikialeksey.jood.sql.JdSql;
import org.takes.misc.Sprintf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DbNulls implements Violations {

    private final Db db;
    private final String repo;

    public DbNulls(final Db db, final String repo) {
        this.db = db;
        this.repo = repo;
    }

    @Override
    public void clear() throws IwfyException {
        try {
            db.write(
                new JdSql(
                    "DELETE FROM null_description WHERE repo = ?",
                    new StringArg(repo)
                )
            );
            db.write(
                new JdSql(
                    "UPDATE nullfree SET badgeUrl = '' WHERE repo = ?",
                    new StringArg(repo)
                )
            );
        } catch (JdException e) {
            throw new IwfyException(
                new Sprintf(
                    "Can not clear the nulls for repo '%s'",
                    repo
                ).toString(),
                e
            );
        }
    }

    @Override
    public void add(final Violation violation) throws IwfyException {
        try {
            final String id = UUID.randomUUID().toString();
            db.write(
                new JdSql(
                    "INSERT OR REPLACE INTO null_description VALUES(?, ?, ?)",
                    new StringArg(id),
                    new StringArg(repo),
                    new StringArg(violation.description())
                )
            );
        } catch (JdException e) {
            throw new IwfyException(
                new Sprintf(
                    "Can not add the null '%s' in repo '%s'",
                    repo,
                    violation.description()
                ).toString(),
                e
            );
        }
    }

    @Override
    public int count() throws IwfyException {
        try (
            final QueryResult result = db.read(
                new JdSql(
                    "SELECT count(*) FROM null_description WHERE repo = ?",
                    new StringArg(repo)
                )
            );
        ) {
            final ResultSet rs = result.rs();
            if (!rs.next()) {
                throw new IwfyException("'SELECT count(*)' returned nothing.");
            }
            return rs.getInt(1);
        } catch (SQLException | JdException e) {
            throw new IwfyException("Can not get the nulls count.", e);
        }
    }
}
