package dev.youshallnotpass.inspection.inheritancefree;

import dev.youshallnotpass.YsnpException;
import dev.youshallnotpass.inspection.Violation;
import dev.youshallnotpass.inspection.Violations;
import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdException;
import com.nikialeksey.jood.QueryResult;
import com.nikialeksey.jood.args.StringArg;
import com.nikialeksey.jood.sql.JdSql;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.takes.misc.Sprintf;

public final class DbInheritances implements Violations {

    private final Db db;
    private final String repo;

    public DbInheritances(final Db db, final String repo) {
        this.db = db;
        this.repo = repo;
    }

    @Override
    public void clear() throws YsnpException {
        try {
            db.write(
                new JdSql(
                    "DELETE FROM inheritancefree_violation WHERE repo = ?",
                    new StringArg(repo)
                )
            );
            db.write(
                new JdSql(
                    "UPDATE inheritancefree SET badgeUrl = '' WHERE repo = ?",
                    new StringArg(repo)
                )
            );
        } catch (final JdException e) {
            throw new YsnpException(
                new Sprintf(
                    "Can not clear the inheritances for repo '%s'",
                    repo
                ).toString(),
                e
            );
        }
    }

    @Override
    public void add(final Violation violation) throws YsnpException {
        try {
            final String id = UUID.randomUUID().toString();
            db.write(
                new JdSql(
                    "INSERT OR REPLACE INTO inheritancefree_violation VALUES(?, ?, ?)",
                    new StringArg(id),
                    new StringArg(repo),
                    new StringArg(violation.description())
                )
            );
        } catch (final JdException e) {
            throw new YsnpException(
                new Sprintf(
                    "Can not add the inheritances '%s' in repo '%s'",
                    repo,
                    violation.description()
                ).toString(),
                e
            );
        }
    }

    @Override
    public int count() throws YsnpException {
        try (
            final QueryResult result = db.read(
                new JdSql(
                    "SELECT count(*) FROM inheritancefree_violation WHERE repo = ?",
                    new StringArg(repo)
                )
            );
        ) {
            final ResultSet rs = result.rs();
            if (!rs.next()) {
                throw new YsnpException("'SELECT count(*)' returned nothing.");
            }
            return rs.getInt(1);
        } catch (final SQLException | JdException e) {
            throw new YsnpException("Can not get the inheritance count.", e);
        }
    }
}
