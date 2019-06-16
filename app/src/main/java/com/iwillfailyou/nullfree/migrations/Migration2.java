package com.iwillfailyou.nullfree.migrations;

import com.iwillfailyou.nullfree.db.Db;
import com.iwillfailyou.nullfree.db.DbException;
import com.iwillfailyou.nullfree.db.Migration;

public class Migration2 implements Migration {
    @Override
    public int number() {
        return 2;
    }

    @Override
    public void execute(final Db db) throws DbException {
        db.write(
            "ALTER TABLE repo ADD COLUMN threshold INTEGER NOT NULL DEFAULT 0",
            new String[]{}
        );
    }
}
