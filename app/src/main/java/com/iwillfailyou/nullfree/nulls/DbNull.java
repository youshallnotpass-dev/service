package com.iwillfailyou.nullfree.nulls;

import com.nikialeksey.jood.Db;

public class DbNull implements Null {
    private final Db db;
    private final String id;

    public DbNull(final Db db, final String id) {
        this.db = db;
        this.id = id;
    }
}
