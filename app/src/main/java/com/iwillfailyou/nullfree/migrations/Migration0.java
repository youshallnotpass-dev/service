package com.iwillfailyou.nullfree.migrations;

import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdException;
import com.nikialeksey.jood.Migration;
import com.nikialeksey.jood.sql.JdSql;

public class Migration0 implements Migration {
    @Override
    public int number() {
        return 0;
    }

    @Override
    public void execute(final Db db) throws JdException {
        db.write(
            new JdSql(
                "CREATE TABLE repo (" +
                    "path TEXT NOT NULL PRIMARY KEY, " +
                    "badgeUrl TEXT NOT NULL" +
                    ")"
            )
        );
    }
}
