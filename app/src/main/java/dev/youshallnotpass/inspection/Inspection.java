package dev.youshallnotpass.inspection;

import dev.youshallnotpass.YsnpException;
import org.cactoos.list.ListOf;

import java.util.List;

public interface Inspection {
    String badgeUrl() throws YsnpException;

    void calcBadge() throws YsnpException;

    void updateThreshold(final int threshold) throws YsnpException;

    Violations violations() throws YsnpException;

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
        public String badgeUrl() throws YsnpException {
            if (badge.isEmpty()) {
                throw new YsnpException("Repo does not contain the badge.");
            }
            return badge.get(0);
        }

        @Override
        public void updateThreshold(final int threshold) throws YsnpException {
            this.threshold.set(0, threshold);
        }

        @Override
        public Violations violations() {
            return violations;
        }

        @Override
        public void calcBadge() throws YsnpException {
            final String url;
            if (violations.count() > threshold.get(0)) {
                url = "red";
            } else {
                url = "green";
            }

            if (badge.isEmpty()) {
                badge.add(url);
            } else {
                badge.set(0, url);
            }
        }
    }
}
