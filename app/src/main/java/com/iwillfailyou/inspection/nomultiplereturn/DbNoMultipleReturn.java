package com.iwillfailyou.inspection.nomultiplereturn;

import com.iwillfailyou.IwfyException;
import com.iwillfailyou.inspection.Inspection;
import com.iwillfailyou.inspection.IwfyBadge;
import com.iwillfailyou.inspection.Violations;
import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdException;
import com.nikialeksey.jood.QueryResult;
import com.nikialeksey.jood.args.IntArg;
import com.nikialeksey.jood.args.StringArg;
import com.nikialeksey.jood.sql.JdSql;
import org.takes.misc.Sprintf;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class DbNoMultipleReturn implements Inspection {

    private final Db db;
    private final String id;

    public DbNoMultipleReturn(final Db db, final String id) {
        this.db = db;
        this.id = id;
    }

    @Override
    public String badgeUrl() throws IwfyException {
        try (
            final QueryResult result = db.read(
                new JdSql(
                    "SELECT badgeUrl FROM nomultiplereturn WHERE id = ?",
                    new StringArg(id)
                )
            )
        ) {
            final ResultSet rs = result.rs();
            if (!rs.next()) {
                throw new IwfyException(
                    "Nomultiplereturn does not exists for id " + id
                );
            }
            return rs.getString("badgeUrl");
        } catch (final SQLException | JdException e) {
            throw new IwfyException("Can not get the badge.", e);
        }
    }

    @Override
    public void calcBadge() throws IwfyException {
        try (
            final QueryResult result = db.read(
                new JdSql(
                    "SELECT threshold FROM nomultiplereturn WHERE id = ?",
                    new StringArg(id)
                )
            )
        ) {
            final ResultSet rs = result.rs();
            if (!rs.next()) {
                throw new IwfyException(
                    new Sprintf(
                        "Can not find the nomultiplereturn with id '%s'.",
                        id
                    ).toString()
                );
            }
            final int threshold = rs.getInt("threshold");
            db.write(
                new JdSql(
                    "UPDATE nomultiplereturn SET badgeUrl = ? WHERE id = ?",
                    new StringArg(
                        new IwfyBadge(
                            "nomultiplereturn",
                            () -> violations().count() <= threshold
                        ).asString()
                    ),
                    new StringArg(id)
                )
            );
        } catch (final SQLException | JdException e) {
            throw new IwfyException(
                new Sprintf(
                    "Can not calc the badge by nomultiplereturn '%s'",
                    id
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
                    "UPDATE nomultiplereturn SET threshold = ? WHERE id = ?",
                    new IntArg(threshold),
                    new StringArg(id)
                )
            );
        } catch (final JdException e) {
            throw new IwfyException(
                new Sprintf(
                    "Can not update the threshold in nomultiplereturn with id '%s'",
                    id
                ).toString(),
                e
            );
        }
    }

    @Override
    public Violations violations() throws IwfyException {
        try (
            final QueryResult qr = db.read(
                new JdSql(
                    "SELECT repo FROM nomultiplereturn WHERE id = ?",
                    new StringArg(id)
                )
            )
        ) {
            final ResultSet rs = qr.rs();
            if (!rs.next()) {
                throw new IwfyException(
                    "Could not find the nomultiplereturn by id " + id
                );
            }
            return new DbMultipleReturns(db, rs.getString("repo"));
        } catch (final JdException | SQLException e) {
            throw new IwfyException(
                "Could not get the nomultiplereturn info by id " + id,
                e
            );
        }
    }
}
