package dev.youshallnotpass.inspection.nullfree;

import dev.youshallnotpass.cactoos.YsnpFunc;
import dev.youshallnotpass.inspection.TkViolation;
import dev.youshallnotpass.repo.Repos;
import org.takes.Response;
import org.takes.facets.fork.RqRegex;
import org.takes.facets.fork.TkRegex;

import java.io.IOException;
import java.util.regex.Matcher;

public final class TkNullfree implements TkRegex {

    private final TkViolation origin;

    public TkNullfree(final Repos repos) {
        this(
            new TkViolation(
                new YsnpFunc<>((final RqRegex rq) -> {
                    final Matcher matcher = rq.matcher();
                    final String user = matcher.group("user");
                    final String repo = matcher.group("repo");
                    final String path = user + "/" + repo;
                    return repos.repo(path).nullfree();
                })
            )
        );
    }

    public TkNullfree(final TkViolation origin) {
        this.origin = origin;
    }

    @Override
    public Response act(final RqRegex req) throws IOException {
        return this.origin.act(req);
    }
}
