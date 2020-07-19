package com.iwillfailyou;

import com.iwillfailyou.migrations.Migration0;
import com.iwillfailyou.migrations.Migration1;
import com.iwillfailyou.migrations.Migration2;
import com.iwillfailyou.migrations.Migration3;
import com.iwillfailyou.migrations.Migration4;
import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdException;
import com.nikialeksey.jood.JdMigrations;
import com.nikialeksey.jood.MigrationsDb;
import com.nikialeksey.jood.QueryResult;
import com.nikialeksey.jood.Transaction;
import com.nikialeksey.jood.sql.Sql;

public class IwfyDb implements Db {

    private final Db origin;

    public IwfyDb(final Db db) {
        this(
            new MigrationsDb(
                db,
                new JdMigrations(
                    new Migration0(),
                    new Migration1(),
                    new Migration2(),
                    new Migration3(),
                    new Migration4()
                ),
                5
            )
        );
    }

    public IwfyDb(final MigrationsDb origin) {
        this.origin = origin;
    }

    @Override
    public QueryResult read(final Sql sql) throws JdException {
        return origin.read(sql);
    }

    @Override
    public void write(final Sql sql) throws JdException {
        origin.write(sql);
    }

    @Override
    public QueryResult writeReturnGenerated(final Sql sql) throws JdException {
        return origin.writeReturnGenerated(sql);
    }

    @Override
    public void run(final Transaction transaction) throws JdException {
        origin.run(transaction);
    }
}
