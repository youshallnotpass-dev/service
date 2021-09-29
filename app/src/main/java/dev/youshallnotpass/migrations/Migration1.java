package dev.youshallnotpass.migrations;

import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdException;
import com.nikialeksey.jood.Migration;
import com.nikialeksey.jood.sql.JdSql;

public final class Migration1 implements Migration {
    @Override
    public int number() {
        return 1;
    }

    @Override
    public void execute(final Db db) throws JdException {
        db.write(
            new JdSql(
                "CREATE TABLE null_description (" +
                    "id TEXT NOT NULL PRIMARY KEY," +
                    "repo TEXT NOT NULL," +
                    "description TEXT NOT NULL," +
                    "FOREIGN KEY(repo) REFERENCES repo(path) ON UPDATE CASCADE ON DELETE CASCADE" +
                    ")"
            )
        );
    }
}
