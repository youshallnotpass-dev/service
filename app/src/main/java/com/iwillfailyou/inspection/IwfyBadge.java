package com.iwillfailyou.inspection;

import com.iwillfailyou.Badge;
import com.iwillfailyou.IwfyException;
import org.cactoos.Scalar;

public class IwfyBadge implements Badge {

    private final Scalar<Boolean> isGreen;

    public IwfyBadge(final Scalar<Boolean> isGreen) {
        this.isGreen = isGreen;
    }

    @Override
    public String asString() throws IwfyException {
        final boolean green;
        try {
            green = isGreen.value();
        } catch (Exception e) {
            throw new IwfyException(
                "Could not determine the badge color.",
                e
            );
        }
        final String message;
        final String color;
        if (green) {
            message = "approved";
            color = "green";
        } else {
            message = "declined";
            color = "red";
        }
        return String.format(
            "https://img.shields.io/badge/nullfree-%s-%s.svg",
            message,
            color
        );
    }
}
