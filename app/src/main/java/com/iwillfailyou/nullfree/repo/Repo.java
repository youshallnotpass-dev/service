package com.iwillfailyou.nullfree.repo;

import com.iwillfailyou.IwfyException;
import com.iwillfailyou.nullfree.nulls.Nulls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Repo {
    String badgeUrl() throws IwfyException;

    // @todo #5:30m Remove updateBadge method
    void updateBadge(String url) throws IwfyException;

    Nulls nulls();

    void calcBadge() throws IwfyException;

    class Fake implements Repo {

        private final List<String> badge;
        private final Nulls nulls;

        public Fake() {
            this(new ArrayList<>(), new Nulls.Fake());
        }

        public Fake(final String badgeUrl, final Nulls nulls) {
            this(Arrays.asList(badgeUrl), nulls);
        }

        private Fake(final List<String> badge, final Nulls nulls) {
            this.badge = badge;
            this.nulls = nulls;
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

        @Override
        public Nulls nulls() {
            return nulls;
        }

        @Override
        public void calcBadge() throws IwfyException {
            if (nulls.count() > 0) {
                badge.set(0, "red");
            } else {
                badge.set(0, "green");
            }
        }
    }
}
