package com.nikialeksey.nullfree.repo;

import com.nikialeksey.nullfree.IwfyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Repo {
    String badgeUrl() throws IwfyException;

    void updateBadge(String url) throws IwfyException;

    class Fake implements Repo {

        private final List<String> badge;

        public Fake() {
            this(new ArrayList<>());
        }

        public Fake(final String badgeUrl) {
            this(Arrays.asList(badgeUrl));
        }

        private Fake(final List<String> badge) {
            this.badge = badge;
        }

        @Override
        public String badgeUrl() throws IwfyException {
            if (badge.isEmpty()) {
                throw new IwfyException("Repo does not contain the badge.");
            }
            return badge.get(0);
        }

        @Override
        public void updateBadge(final String url) {
            if (badge.isEmpty()) {
                badge.add(url);
            } else {
                badge.set(0, url);
            }
        }
    }
}
