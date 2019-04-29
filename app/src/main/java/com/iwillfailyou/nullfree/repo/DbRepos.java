package com.nikialeksey.nullfree.repo;

import com.nikialeksey.nullfree.IwfyException;
import com.nikialeksey.nullfree.db.Db;

public class DbRepos implements Repos {

    private final Db db;

    public DbRepos(final Db db) {
        this.db = db;
    }

    @Override
    public Repo repo(final String path) throws IwfyException {
        return new DbRepo(db, path);
    }
}
