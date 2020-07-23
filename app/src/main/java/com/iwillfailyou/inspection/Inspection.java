package com.iwillfailyou.inspection;

import com.iwillfailyou.IwfyException;
import org.cactoos.list.ListOf;

import java.util.List;

public interface Inspection {
    String badgeUrl() throws IwfyException;

    void calcBadge() throws IwfyException;

    void updateThreshold(final int threshold) throws IwfyException;

    Violations violations() throws IwfyException;

    final class Fake implements Inspection {

        private final List<String> badge;
        private final Violations violations;
        private final List<Integer> threshold;

        public Fake() {
            this(new ListOf<>(), new Violations.Fake(), new ListOf<>(0));
        }

        public Fake(final String badgeUrl, final Violations violations) {
            this(new ListOf<>(badgeUrl), violations, new ListOf<>(0));
        }

        private Fake(
            final List<String> badge,
            final Violations violations,
            final List<Integer> threshold
        ) {
            this.badge = badge;
            this.violations = violations;
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
        public void updateThreshold(final int threshold) throws IwfyException {
            this.threshold.set(0, threshold);
        }

        @Override
        public Violations violations() {
            return violations;
        }

        @Override
        public void calcBadge() throws IwfyException {
            if (violations.count() > threshold.get(0)) {
                updateBadge("red");
            } else {
                updateBadge("green");
            }
        }

        private void updateBadge(final String url) {
            if (badge.isEmpty()) {
                badge.add(url);
            } else {
                badge.set(0, url);
            }
        }
    }
}
