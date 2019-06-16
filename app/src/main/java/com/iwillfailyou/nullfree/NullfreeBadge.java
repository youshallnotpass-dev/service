package com.iwillfailyou.nullfree;

import com.iwillfailyou.Badge;
import com.iwillfailyou.IwfyException;
import com.iwillfailyou.nullfree.nulls.Nulls;

public class NullfreeBadge implements Badge {

    private final Nulls nulls;
    private final int threshold;

    public NullfreeBadge(final Nulls nulls) {
        this(nulls, 0);
    }

    public NullfreeBadge(final Nulls nulls, final int threshold) {
        this.nulls = nulls;
        this.threshold = threshold;
    }

    @Override
    public String asString() throws IwfyException {
        final int nullCount = nulls.count();
        final String message;
        if (nullCount <= threshold) {
            message = "approved";
        } else {
            message = "declined";
        }
        final String color;
        if (nullCount <= threshold) {
            color = "green";
        } else {
            color = "red";
        }
        return String.format(
            "https://img.shields.io/badge/nullfree-%s-%s.svg",
            message,
            color
        );
    }
}
