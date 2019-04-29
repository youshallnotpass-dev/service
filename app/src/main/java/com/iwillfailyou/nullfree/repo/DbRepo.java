package com.nikialeksey.nullfree.repo;

import com.nikialeksey.nullfree.IwfyException;
import com.nikialeksey.nullfree.db.Db;
import com.nikialeksey.nullfree.db.DbException;
import com.nikialeksey.nullfree.db.QueryResult;
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
            final QueryResult result = db.read("SELECT * FROM repo WHERE path = ?", new String[]{path});
        ) {
            final ResultSet rs = result.rs();
            if (!rs.next()) {
                throw new IwfyException("Repo does not contain the badge.");
            }
            return rs.getString("badgeUrl");
        } catch (SQLException | DbException e) {
            throw new IwfyException("Can not get the badge.", e);
        }
    }

    @Override
    public void updateBadge(final String url) throws IwfyException {
        try {
            db.write("INSERT OR REPLACE INTO repo VALUES(?, ?)", new String[]{path, url});
        } catch (DbException e) {
            throw new IwfyException(
                new Sprintf(
                    "Can not update the badge in path '%s'",
                    path
                ).toString(),
                e
            );
        }
    }
}
