package dev.youshallnotpass.inspection.inheritancefree;

import dev.youshallnotpass.YsnpException;
import dev.youshallnotpass.inspection.Inspection;
import dev.youshallnotpass.inspection.YsnpBadge;
import dev.youshallnotpass.inspection.Violations;
import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdException;
import com.nikialeksey.jood.QueryResult;
import com.nikialeksey.jood.args.IntArg;
import com.nikialeksey.jood.args.StringArg;
import com.nikialeksey.jood.sql.JdSql;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.takes.misc.Sprintf;

public final class DbInheritanceFree implements Inspection {

    private final Db db;
    private final String id;

    public DbInheritanceFree(final Db db, final String id) {
        this.db = db;
        this.id = id;
    }

    @Override
    public String badgeUrl() throws YsnpException {
        try (
            final QueryResult result = db.read(
                new JdSql(
                    "SELECT badgeUrl FROM inheritancefree WHERE id = ?",
                    new StringArg(id)
                )
            )
        ) {
            final ResultSet rs = result.rs();
            if (!rs.next()) {
                throw new YsnpException(
                    "Inheritancefree does not exists for id " + id
                );
            }
            return rs.getString("badgeUrl");
        } catch (final SQLException | JdException e) {
            throw new YsnpException("Can not get the badge.", e);
        }
    }

    @Override
    public void calcBadge() throws YsnpException {
        try (
            final QueryResult result = db.read(
                new JdSql(
                    "SELECT threshold FROM inheritancefree WHERE id = ?",
                    new StringArg(id)
                )
            )
        ) {
            final ResultSet rs = result.rs();
            if (!rs.next()) {
                throw new YsnpException(
                    new Sprintf(
                        "Can not find the inheritancefree with id '%s'.",
                        id
                    ).toString()
                );
            }
            final int threshold = rs.getInt("threshold");
            db.write(
                new JdSql(
                    "UPDATE inheritancefree SET badgeUrl = ? WHERE id = ?",
                    new StringArg(
                        new YsnpBadge(
                            "inheritancefree",
                            () -> violations().count() <= threshold
                        ).asString()
                    ),
                    new StringArg(id)
                )
            );
        } catch (final SQLException | JdException e) {
            throw new YsnpException(
                new Sprintf(
                    "Can not calc the badge by inheritancefree '%s'",
                    id
                ).toString(),
                e
            );
        }
    }

    @Override
    public void updateThreshold(final int threshold) throws YsnpException {
        try {
            db.write(
                new JdSql(
                    "UPDATE inheritancefree SET threshold = ? WHERE id = ?",
                    new IntArg(threshold),
                    new StringArg(id)
                )
            );
        } catch (final JdException e) {
            throw new YsnpException(
                new Sprintf(
                    "Can not update the threshold in inheritancefree with id '%s'",
                    id
                ).toString(),
                e
            );
        }
    }

    @Override
    public Violations violations() throws YsnpException {
        try (
            final QueryResult qr = db.read(
                new JdSql(
                    "SELECT repo FROM inheritancefree WHERE id = ?",
                    new StringArg(id)
                )
            )
        ) {
            final ResultSet rs = qr.rs();
            if (!rs.next()) {
                throw new YsnpException(
                    "Could not find the inheritancefree by id " + id
                );
            }
            return new DbInheritances(db, rs.getString("repo"));
        } catch (final JdException | SQLException e) {
            throw new YsnpException(
                "Could not get the inheritancefree info by id " + id,
                e
            );
        }
    }
}
