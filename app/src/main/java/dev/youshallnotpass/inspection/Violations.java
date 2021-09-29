package dev.youshallnotpass.inspection;

import dev.youshallnotpass.YsnpException;

import java.util.ArrayList;
import java.util.List;

public interface Violations {
    void clear() throws YsnpException;

    void add(final Violation violation) throws YsnpException;

    int count() throws YsnpException;

    final class Fake implements Violations {
        private final List<Violation> violations;

        public Fake() {
            this(new ArrayList<>());
        }

        public Fake(final List<Violation> violations) {
            this.violations = violations;
        }

        @Override
        public void clear() throws YsnpException {
            violations.clear();
        }

        @Override
        public void add(final Violation violation) throws YsnpException {
            violations.add(violation);
        }

        @Override
        public int count() throws YsnpException {
            return violations.size();
        }
    }
}
