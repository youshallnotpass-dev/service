package com.iwillfailyou.nullfree.nulls;

import com.iwillfailyou.IwfyException;
import com.iwillfailyou.nullfree.db.Db;
import com.iwillfailyou.nullfree.db.DbException;
import com.iwillfailyou.nullfree.db.QueryResult;
import org.takes.misc.Sprintf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DbNulls implements Nulls {

    private final Db db;
    private final String repo;

    public DbNulls(final Db db, final String repo) {
        this.db = db;
        this.repo = repo;
    }

    @Override
    public void clear() throws IwfyException {
        try {
            db.write("DELETE FROM null_description WHERE repo = ?", new String[]{repo});
            db.write("UPDATE repo SET badgeUrl = '' WHERE path = ?", new String[]{repo});
        } catch (DbException e) {
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
    public Null add(final String description) throws IwfyException {
        try {
            final String id = UUID.randomUUID().toString();
            db.write(
                "INSERT OR REPLACE INTO null_description VALUES(?, ?, ?)",
                new String[]{id, repo, description}
            );
            return new DbNull(db, id);
        } catch (DbException e) {
            throw new IwfyException(
                new Sprintf(
                    "Can not add the null '%s' in repo '%s'",
                    repo,
                    description
                ).toString(),
                e
            );
        }
    }

    @Override
    public int count() throws IwfyException {
        try (
            final QueryResult result = db.read("SELECT count(*) FROM null_description WHERE repo = ?", new String[]{repo});
        ) {
            final ResultSet rs = result.rs();
            if (!rs.next()) {
                throw new IwfyException("'SELECT count(*)' returned nothing.");
            }
            return rs.getInt(1);
        } catch (SQLException | DbException e) {
            throw new IwfyException("Can not get the nulls count.", e);
        }
    }
}
