package dev.youshallnotpass.inspection;

import dev.youshallnotpass.YsnpException;

public interface Violation {

    String description() throws YsnpException;

    final class Fake implements Violation {
        private final String description;

        public Fake(final String description) {
            this.description = description;
        }

        @Override
        public String description() throws YsnpException {
            return description;
        }
    }
}
