package com.iwillfailyou.nullfree.repo;

import java.util.HashMap;
import java.util.Map;

public interface Repos {
    Repo repo(String path);

    class Fake implements Repos {
        private final Map<String, Repo> pathToRepo;

        public Fake() {
            this(new HashMap<>());
        }

        public Fake(final Map<String, Repo> pathToRepo) {
            this.pathToRepo = pathToRepo;
        }

        @Override
        public Repo repo(final String path) {
            final Repo repo;
            if (pathToRepo.containsKey(path)) {
                repo = pathToRepo.get(path);
            } else {
                repo = new Repo.Fake();
                pathToRepo.put(path, repo);
            }
            return repo;
        }
    }
}
