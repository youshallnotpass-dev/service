package com.iwillfailyou.nullfree.migrations;

import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.DbException;
import com.nikialeksey.jood.Migration;

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
