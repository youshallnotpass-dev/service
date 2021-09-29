package dev.youshallnotpass.inspection;

import dev.youshallnotpass.YsnpException;
import dev.youshallnotpass.cactoos.YsnpFunc;
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

public final class TkViolation implements TkRegex {

    private final YsnpFunc<RqRegex, Inspection> reqInspection;

    public TkViolation(final YsnpFunc<RqRegex, Inspection> reqInspection) {
        this.reqInspection = reqInspection;
    }

    @Override
    public Response act(final RqRegex req) throws IOException {
        final String method = new RqMethod.Base(req).method();
        final Response response;

        if ("POST".equals(method)) {
            @SuppressWarnings("allfinal")
            Response postResponse;
            try {
                final Inspection inspection = reqInspection.apply(req);
                final RqFormBase form = new RqFormBase(req);
                final Iterator<String> thresholdIterator = form.param("threshold").iterator();
                final int threshold;
                if (thresholdIterator.hasNext()) {
                    threshold = Integer.parseInt(thresholdIterator.next());
                } else {
                    threshold = 0;
                }
                inspection.updateThreshold(threshold);
                final Violations violations = inspection.violations();
                violations.clear();
                final Iterable<String> newViolations = form.param("violation");
                for (final String violation : newViolations) {
                    violations.add(new YsnpViolation(violation));
                }
                inspection.calcBadge();
                postResponse = new RsWithStatus(
                    new RsWithBody("Saved!\n"),
                    HttpURLConnection.HTTP_OK
                );
            } catch (final YsnpException e) {
                postResponse = new RsWithStatus(
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
            response = postResponse;
        } else {
            @SuppressWarnings("allfinal")
            Response getResponse;
            try {
                getResponse = new RsRedirect(
                    reqInspection.apply(req).badgeUrl()
                );
            } catch (final YsnpException e) {
                getResponse = new RsWithStatus(
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
            response = getResponse;
        }
        return response;
    }
}
