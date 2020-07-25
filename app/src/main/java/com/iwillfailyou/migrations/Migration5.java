package com.iwillfailyou.migrations;

import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdException;
import com.nikialeksey.jood.Migration;
import com.nikialeksey.jood.sql.JdSql;

public final class Migration5 implements Migration {
    @Override
    public int number() {
        return 5;
    }

    @Override
    public void execute(final Db db) throws JdException {
        db.write(
            new JdSql(
                "CREATE TABLE allpublic (" +
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
                "CREATE TABLE allpublic_violation (" +
                    "id TEXT NOT NULL PRIMARY KEY," +
                    "repo TEXT NOT NULL," +
                    "description TEXT NOT NULL," +
                    "FOREIGN KEY(repo) REFERENCES repo(path) ON UPDATE CASCADE ON DELETE CASCADE" +
                    ")"
            )
        );
    }
}
