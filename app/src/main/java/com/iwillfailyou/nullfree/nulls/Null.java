package com.iwillfailyou.nullfree.nulls;

public interface Null {
    class Fake implements Null {
        private final String description;

        public Fake(final String description) {
            this.description = description;
        }
    }
}
