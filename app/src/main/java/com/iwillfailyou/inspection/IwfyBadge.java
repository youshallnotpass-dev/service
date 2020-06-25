package com.iwillfailyou.inspection;

import com.iwillfailyou.Badge;
import com.iwillfailyou.IwfyException;
import org.cactoos.Scalar;

public class IwfyBadge implements Badge {

    private final String title;
    private final Scalar<Boolean> isGreen;

    public IwfyBadge(final String title, final Scalar<Boolean> isGreen) {
        this.title = title;
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
            "https://img.shields.io/badge/%s-%s-%s.svg",
            title,
            message,
            color
        );
    }
}
