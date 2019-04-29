package com.nikialeksey.nullfree.repo;

import com.nikialeksey.nullfree.IwfyException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.takes.Response;
import org.takes.facets.fork.RqRegex;
import org.takes.facets.fork.TkRegex;
import org.takes.misc.Sprintf;
import org.takes.rq.RqMethod;
import org.takes.rq.form.RqFormBase;
import org.takes.rs.RsRedirect;
import org.takes.rs.RsWithBody;
import org.takes.rs.RsWithStatus;

import java.io.IOException;
import java.net.HttpURLConnection;

public class RepoInfo implements TkRegex {

    private final Repos repos;

    public RepoInfo(final Repos repos) {
        this.repos = repos;
    }

    @Override
    public Response act(final RqRegex req) throws IOException {
        final String user = req.matcher().group("user");
        final String repo = req.matcher().group("repo");
        final String path = user + "/" + repo;
        final String method = new RqMethod.Base(req).method();

        if ("POST".equals(method)) {
            try {
                repos.repo(path).updateBadge(new RqFormBase(req).param("badgeUrl").iterator().next());
                return new RsWithStatus(new RsWithBody("Saved!\n"), HttpURLConnection.HTTP_OK);
            } catch (IwfyException e) {
                return new RsWithStatus(
                    new RsWithBody(
                        new Sprintf(
                            "%s\n%s",
                            "Can not save or update the badge.",
                            ExceptionUtils.getStackTrace(e)
                        )
                    ),
                    HttpURLConnection.HTTP_INTERNAL_ERROR
                );
            }
        } else {
            try {
                return new RsRedirect(repos.repo(path).badgeUrl());
            } catch (IwfyException e) {
                return new RsWithStatus(
                    new RsWithBody(
                        new Sprintf(
                            "%s\n%s",
                            "Can not get the badge.",
                            ExceptionUtils.getStackTrace(e)
                        )
                    ),
                    HttpURLConnection.HTTP_INTERNAL_ERROR
                );
            }
        }
    }
}
