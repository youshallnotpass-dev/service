package com.iwillfailyou.inspection;

import com.iwillfailyou.IwfyException;

public interface Violation {

    String description() throws IwfyException;

    class Fake implements Violation {
        private final String description;

        public Fake(final String description) {
            this.description = description;
        }

        @Override
        public String description() throws IwfyException {
            return description;
        }
    }
}
