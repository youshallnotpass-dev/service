package dev.youshallnotpass.repo;

import dev.youshallnotpass.YsnpException;

import java.util.HashMap;
import java.util.Map;

public interface Repos {
    Repo repo(final String path) throws YsnpException;

    final class Fake implements Repos {
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
