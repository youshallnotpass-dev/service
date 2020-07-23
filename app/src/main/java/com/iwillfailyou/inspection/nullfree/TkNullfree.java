package com.iwillfailyou.inspection.nullfree;

import com.iwillfailyou.IwfyException;
import com.iwillfailyou.inspection.Inspection;
import com.iwillfailyou.inspection.IwfyViolation;
import com.iwillfailyou.inspection.Violation;
import com.iwillfailyou.inspection.Violations;
import com.iwillfailyou.repo.Repos;
import org.cactoos.text.TextOf;
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

public final class TkNullfree implements TkRegex {

    private final Repos repos;

    public TkNullfree(final Repos repos) {
        this.repos = repos;
    }

    @Override
    public Response act(final RqRegex req) throws IOException {
        final String path = req.matcher().group("user") + "/" + req.matcher().group("repo");
        final String method = new RqMethod.Base(req).method();

        if ("POST".equals(method)) {
            try {
                final Inspection nullfree = repos.repo(path).nullfree();
                final RqFormBase form = new RqFormBase(req);
                final Iterator<String> thresholdIterator = form.param("threshold").iterator();
                final int threshold;
                if (thresholdIterator.hasNext()) {
                    threshold = Integer.parseInt(thresholdIterator.next());
                } else {
                    threshold = 0;
                }
                nullfree.updateThreshold(threshold);
                final Violations repoNulls = nullfree.violations();
                repoNulls.clear();
                final Iterable<String> nulls = form.param("null");
                for (final String nullDescription : nulls) {
                    repoNulls.add(new IwfyViolation(nullDescription));
                }
                nullfree.calcBadge();
                return new RsWithStatus(new RsWithBody("Saved!\n"), HttpURLConnection.HTTP_OK);
            } catch (final IwfyException e) {
                return new RsWithStatus(
                    new RsWithBody(
                        new Sprintf(
                            "%s\n%s",
                            "Can not save or update the badge.",
                            new TextOf(e)
                        )
                    ),
                    HttpURLConnection.HTTP_INTERNAL_ERROR
                );
            }
        } else {
            try {
                return new RsRedirect(repos.repo(path).nullfree().badgeUrl());
            } catch (final IwfyException e) {
                return new RsWithStatus(
                    new RsWithBody(
                        new Sprintf(
                            "%s\n%s",
                            "Could not not get the badge.",
                            new TextOf(e)
                        )
                    ),
                    HttpURLConnection.HTTP_INTERNAL_ERROR
                );
            }
        }
    }
}
