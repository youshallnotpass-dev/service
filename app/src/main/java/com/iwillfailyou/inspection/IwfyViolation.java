package com.iwillfailyou.inspection;

import com.iwillfailyou.IwfyException;

public class IwfyViolation implements Violation {

    private final String description;

    public IwfyViolation(final String description) {
        this.description = description;
    }

    @Override
    public String description() throws IwfyException {
        return description;
    }
}
