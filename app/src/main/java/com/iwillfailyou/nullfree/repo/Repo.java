package com.iwillfailyou.nullfree.repo;

import com.iwillfailyou.IwfyException;
import com.iwillfailyou.nullfree.nulls.Nulls;
import org.cactoos.list.ListOf;

import java.util.List;

public interface Repo {
    String badgeUrl() throws IwfyException;

    // @todo #5:30m Remove updateBadge method
    void updateBadge(String url) throws IwfyException;

    void updateThreshold(int threshold) throws IwfyException;

    Nulls nulls();

    void calcBadge() throws IwfyException;

    class Fake implements Repo {

        private final List<String> badge;
        private final Nulls nulls;
        private final List<Integer> threshold;

        public Fake() {
            this(new ListOf<>(), new Nulls.Fake(), new ListOf<>(0));
        }

        public Fake(final String badgeUrl, final Nulls nulls) {
            this(new ListOf<>(badgeUrl), nulls, new ListOf<>(0));
        }

        private Fake(final List<String> badge, final Nulls nulls, final List<Integer> threshold) {
            this.badge = badge;
            this.nulls = nulls;
            this.threshold = threshold;
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
        public void updateThreshold(final int threshold) throws IwfyException {
            this.threshold.set(0, threshold);
        }

        @Override
        public Nulls nulls() {
            return nulls;
        }

        @Override
        public void calcBadge() throws IwfyException {
            if (nulls.count() > threshold.get(0)) {
                updateBadge("red");
            } else {
                updateBadge("green");
            }
        }
    }
}
