package com.iwillfailyou.nullfree;

import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsHtml;

import java.io.IOException;

public class TkIndex implements Take {
    @Override
    public Response act(final Request req) throws IOException {
        return new RsHtml(getClass().getClassLoader().getResourceAsStream("./nullfree/index.html"));
    }
}
