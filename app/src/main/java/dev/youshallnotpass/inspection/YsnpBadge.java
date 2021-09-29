package dev.youshallnotpass.inspection;

import dev.youshallnotpass.Badge;
import dev.youshallnotpass.YsnpException;
import org.cactoos.Scalar;

public final class YsnpBadge implements Badge {

    private final String title;
    private final Scalar<Boolean> isGreen;

    public YsnpBadge(final String title, final Scalar<Boolean> isGreen) {
        this.title = title;
        this.isGreen = isGreen;
    }

    @Override
    public String asString() throws YsnpException {
        final boolean green;
        try {
            green = isGreen.value();
        } catch (final Exception e) {
            throw new YsnpException(
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
