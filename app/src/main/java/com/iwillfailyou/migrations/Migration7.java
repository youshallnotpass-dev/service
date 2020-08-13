package com.iwillfailyou.migrations;

import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdException;
import com.nikialeksey.jood.Migration;
import com.nikialeksey.jood.sql.JdSql;

public final class Migration7 implements Migration {
    @Override
    public int number() {
        return 7;
    }

    @Override
    public void execute(final Db db) throws JdException {
        db.write(
            new JdSql(
                "CREATE TABLE inheritancefree (" +
                    "id TEXT NOT NULL PRIMARY KEY, " +
                    "repo TEXT NOT NULL, " +
                    "threshold INTEGER NOT NULL DEFAULT 0, " +
                    "badgeUrl TEXT NOT NULL DEFAULT '', " +
                    "FOREIGN KEY(repo) REFERENCES repo(path) ON UPDATE CASCADE ON DELETE CASCADE" +
                    ")"
            )
        );
        db.write(
            new JdSql(
                "CREATE TABLE inheritancefree_violation (" +
                    "id TEXT NOT NULL PRIMARY KEY," +
                    "repo TEXT NOT NULL," +
                    "description TEXT NOT NULL," +
                    "FOREIGN KEY(repo) REFERENCES repo(path) ON UPDATE CASCADE ON DELETE CASCADE" +
                    ")"
            )
        );
    }
}
