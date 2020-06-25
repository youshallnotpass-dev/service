package com.iwillfailyou.repo;

import com.iwillfailyou.IwfyException;
import com.iwillfailyou.inspection.Inspection;
import com.iwillfailyou.inspection.nullfree.DbNullfree;
import com.iwillfailyou.inspection.staticfree.DbStaticfree;
import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdException;
import com.nikialeksey.jood.QueryResult;
import com.nikialeksey.jood.args.StringArg;
import com.nikialeksey.jood.sql.JdSql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DbRepo implements Repo {

    private final Db db;
    private final String path;

    public DbRepo(final Db db, final String path) {
        this.db = db;
        this.path = path;
    }

    @Override
    public Inspection nullfree() throws IwfyException {
        try (
            final QueryResult qr = db.read(
                new JdSql(
                    "SELECT id FROM nullfree WHERE repo = ?",
                    new StringArg(path)
                )
            )
        ) {
            final String id;
            final ResultSet rs = qr.rs();
            if (!rs.next()) {
                id = UUID.randomUUID().toString();
                db.write(
                    new JdSql(
                        "INSERT INTO nullfree (id, repo) VALUES(?, ?)",
                        new StringArg(id),
                        new StringArg(path)
                    )
                );
            } else {
                id = rs.getString("id");
            }
            return new DbNullfree(db, id);
        } catch (JdException | SQLException e) {
            throw new IwfyException(
                "Could not get the nullfree for repo " + path,
                e
            );
        }
    }

    @Override
    public Inspection staticfree() throws IwfyException {
        try (
            final QueryResult qr = db.read(
                new JdSql(
                    "SELECT id FROM staticfree WHERE repo = ?",
                    new StringArg(path)
                )
            )
        ) {
            final String id;
            final ResultSet rs = qr.rs();
            if (!rs.next()) {
                id = UUID.randomUUID().toString();
                db.write(
                    new JdSql(
                        "INSERT INTO staticfree (id, repo) VALUES(?, ?)",
                        new StringArg(id),
                        new StringArg(path)
                    )
                );
            } else {
                id = rs.getString("id");
            }
            return new DbStaticfree(db, id);
        } catch (JdException | SQLException e) {
            throw new IwfyException(
                "Could not get the staticfree for repo " + path,
                e
            );
        }
    }
}
