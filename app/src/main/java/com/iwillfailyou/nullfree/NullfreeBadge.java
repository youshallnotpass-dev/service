package com.iwillfailyou.nullfree;

import com.iwillfailyou.Badge;
import com.iwillfailyou.IwfyException;
import com.iwillfailyou.nullfree.nulls.Nulls;

public class NullfreeBadge implements Badge {

    private final Nulls nulls;

    public NullfreeBadge(final Nulls nulls) {
        this.nulls = nulls;
    }

    @Override
    public String asString() throws IwfyException {
        final int nullCount = nulls.count();
        final String message;
        if (nullCount == 0) {
            message = "approved";
        } else {
            message = "declined";
        }
        final String color;
        if (nullCount == 0) {
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
