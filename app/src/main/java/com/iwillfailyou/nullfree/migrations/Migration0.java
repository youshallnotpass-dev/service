package com.iwillfailyou.nullfree.migrations;

import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.DbException;
import com.nikialeksey.jood.Migration;

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
