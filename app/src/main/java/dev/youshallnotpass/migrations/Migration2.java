package dev.youshallnotpass.migrations;

import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdException;
import com.nikialeksey.jood.Migration;
import com.nikialeksey.jood.sql.JdSql;

public final class Migration2 implements Migration {
    @Override
    public int number() {
        return 2;
    }

    @Override
    public void execute(final Db db) throws JdException {
        db.write(
            new JdSql(
                "ALTER TABLE repo ADD COLUMN threshold INTEGER NOT NULL DEFAULT 0"
            )
        );
    }
}
