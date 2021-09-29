package dev.youshallnotpass;

import dev.youshallnotpass.migrations.Migration0;
import dev.youshallnotpass.migrations.Migration1;
import dev.youshallnotpass.migrations.Migration2;
import dev.youshallnotpass.migrations.Migration3;
import dev.youshallnotpass.migrations.Migration4;
import dev.youshallnotpass.migrations.Migration5;
import dev.youshallnotpass.migrations.Migration6;
import dev.youshallnotpass.migrations.Migration7;
import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.JdException;
import com.nikialeksey.jood.JdMigrations;
import com.nikialeksey.jood.MigrationsDb;
import com.nikialeksey.jood.QueryResult;
import com.nikialeksey.jood.Transaction;
import com.nikialeksey.jood.sql.Sql;

public final class YsnpDb implements Db {

    private final Db origin;

    public YsnpDb(final Db db) {
        this(
            new MigrationsDb(
                db,
                new JdMigrations(
                    new Migration0(),
                    new Migration1(),
                    new Migration2(),
                    new Migration3(),
                    new Migration4(),
                    new Migration5(),
                    new Migration6(),
                    new Migration7()
                ),
                8
            )
        );
    }

    public YsnpDb(final MigrationsDb origin) {
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
