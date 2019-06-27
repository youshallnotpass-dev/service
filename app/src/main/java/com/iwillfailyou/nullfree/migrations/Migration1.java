package com.iwillfailyou.nullfree.migrations;

import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.DbException;
import com.nikialeksey.jood.Migration;

public class Migration1 implements Migration {
    @Override
    public int number() {
        return 1;
    }

    @Override
    public void execute(final Db db) throws DbException {
        db.write(
            "CREATE TABLE null_description (" +
                "id TEXT NOT NULL PRIMARY KEY," +
                "repo TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "FOREIGN KEY(repo) REFERENCES repo(path) ON UPDATE CASCADE ON DELETE CASCADE" +
                ")",
            new String[]{}
        );
    }
}
