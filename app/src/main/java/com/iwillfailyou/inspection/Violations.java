package com.iwillfailyou.inspection;

import com.iwillfailyou.IwfyException;

import java.util.ArrayList;
import java.util.List;

public interface Violations {
    void clear() throws IwfyException;

    void add(Violation violation) throws IwfyException;

    int count() throws IwfyException;

    class Fake implements Violations {
        private final List<Violation> violations;

        public Fake() {
            this(new ArrayList<>());
        }

        public Fake(final List<Violation> violations) {
            this.violations = violations;
        }

        @Override
        public void clear() throws IwfyException {
            violations.clear();
        }

        @Override
        public void add(final Violation violation) throws IwfyException {
            violations.add(violation);
        }

        @Override
        public int count() throws IwfyException {
            return violations.size();
        }
    }
}
