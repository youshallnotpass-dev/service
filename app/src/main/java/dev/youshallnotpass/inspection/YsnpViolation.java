package dev.youshallnotpass.inspection;

import dev.youshallnotpass.YsnpException;

public final class YsnpViolation implements Violation {

    private final String description;

    public YsnpViolation(final String description) {
        this.description = description;
    }

    @Override
    public String description() throws YsnpException {
        return description;
    }
}
