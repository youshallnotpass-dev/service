package com.iwillfailyou.inspection.allpublic;

import com.iwillfailyou.cactoos.IwfyFunc;
import com.iwillfailyou.inspection.TkViolation;
import com.iwillfailyou.repo.Repos;
import org.takes.Response;
import org.takes.facets.fork.RqRegex;
import org.takes.facets.fork.TkRegex;

import java.io.IOException;
import java.util.regex.Matcher;

public final class TkAllpublic implements TkRegex {

    private final TkViolation origin;

    public TkAllpublic(final Repos repos) {
        this(
            new TkViolation(
                new IwfyFunc<>((final RqRegex rq) -> {
                    final Matcher matcher = rq.matcher();
                    final String user = matcher.group("user");
                    final String repo = matcher.group("repo");
                    final String path = user + "/" + repo;
                    return repos.repo(path).allpublic();
                })
            )
        );
    }

    public TkAllpublic(final TkViolation origin) {
        this.origin = origin;
    }

    @Override
    public Response act(final RqRegex req) throws IOException {
        return origin.act(req);
    }
}
