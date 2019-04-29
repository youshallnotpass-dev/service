package com.nikialeksey.nullfree.repo;

import com.nikialeksey.nullfree.IwfyException;

import java.util.HashMap;
import java.util.Map;

public interface Repos {
    Repo repo(String path) throws IwfyException;

    class Fake implements Repos {
        private final Map<String, Repo> pathToRepo;

        public Fake() {
            this(new HashMap<>());
        }

        public Fake(final Map<String, Repo> pathToRepo) {
            this.pathToRepo = pathToRepo;
        }

        @Override
        public Repo repo(final String path) throws IwfyException {
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
