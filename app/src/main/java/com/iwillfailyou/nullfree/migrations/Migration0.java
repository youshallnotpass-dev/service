package com.nikialeksey.nullfree.migrations;

import com.nikialeksey.nullfree.db.Db;
import com.nikialeksey.nullfree.db.DbException;
import com.nikialeksey.nullfree.db.Migration;

public class Migration0 implements Migration {
    @Override
    public int number() {
        return 0;
    }

    @Override
    public void execute(final Db db) throws DbException {
        db.write("CREATE TABLE repo (path TEXT NOT NULL PRIMARY KEY, badgeUrl TEXT NOT NULL)", new String[]{});
    }
}
