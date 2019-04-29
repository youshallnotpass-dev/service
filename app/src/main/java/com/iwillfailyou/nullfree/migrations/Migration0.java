package com.iwillfailyou.nullfree.migrations;

import com.iwillfailyou.nullfree.db.Db;
import com.iwillfailyou.nullfree.db.DbException;
import com.iwillfailyou.nullfree.db.Migration;

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
