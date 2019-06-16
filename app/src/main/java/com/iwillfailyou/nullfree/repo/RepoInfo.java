package com.iwillfailyou.nullfree.repo;

import com.iwillfailyou.IwfyException;
import com.iwillfailyou.nullfree.nulls.Nulls;
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
import java.util.Iterator;

public class RepoInfo implements TkRegex {

    private final Repos repos;

    public RepoInfo(final Repos repos) {
        this.repos = repos;
    }

    @Override
    public Response act(final RqRegex req) throws IOException {
        final String path = req.matcher().group("user") + "/" + req.matcher().group("repo");
        final Repo repo = repos.repo(path);
        final String method = new RqMethod.Base(req).method();

        if ("POST".equals(method)) {
            try {
                final RqFormBase form = new RqFormBase(req);
                final Iterable<String> badgeUrl = form.param("badgeUrl");
                if (badgeUrl.iterator().hasNext()) {
                    repo.updateBadge(badgeUrl.iterator().next());
                } else {
                    final Iterator<String> thresholdIterator = form.param("threshold").iterator();
                    final int threshold;
                    if (thresholdIterator.hasNext()) {
                        threshold = Integer.parseInt(thresholdIterator.next());
                    } else {
                        threshold = 0;
                    }
                    repo.updateThreshold(threshold);
                    final Nulls repoNulls = repo.nulls();
                    repoNulls.clear();
                    final Iterable<String> nulls = form.param("null");
                    for (final String nullDescription : nulls) {
                        repoNulls.add(nullDescription);
                    }
                    repo.calcBadge();
                }
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
                return new RsRedirect(repo.badgeUrl());
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
