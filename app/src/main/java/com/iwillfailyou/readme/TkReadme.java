package com.iwillfailyou.readme;

import org.cactoos.Scalar;
import org.cactoos.scalar.IoCheckedScalar;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsVelocity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class TkReadme implements Take {

    private final Parser mdParser;
    private final HtmlRenderer mdRenderer;
    private final IoCheckedScalar<InputStream> readme;
    private final String title;
    private final String link;

    public TkReadme(final Scalar<URL> readmeUrl, final String title, final String link) {
        this(
            new IoCheckedScalar<>(() -> readmeUrl.value().openStream()),
            title,
            link
        );
    }

    public TkReadme(final URL readmeUrl, final String title, final String link) {
        this(
            new IoCheckedScalar<>(readmeUrl::openStream),
            title,
            link
        );
    }

    public TkReadme(final String readme, final String title, final String link) {
        this(
            new IoCheckedScalar<>(() -> new ByteArrayInputStream(readme.getBytes())),
            title,
            link
        );
    }

    public TkReadme(final IoCheckedScalar<InputStream> readme, final String title, final String link) {
        this(
            Parser.builder().build(),
            HtmlRenderer.builder().build(),
            readme,
            title,
            link
        );
    }

    public TkReadme(
        final Parser mdParser,
        final HtmlRenderer mdRenderer,
        final IoCheckedScalar<InputStream> readme,
        final String title,
        final String link
    ) {
        this.mdParser = mdParser;
        this.mdRenderer = mdRenderer;
        this.readme = readme;
        this.title = title;
        this.link = link;
    }

    @Override
    public Response act(final Request req) throws IOException {
        try (
            final InputStream urlStream = readme.value();
            final Reader urlReader = new InputStreamReader(urlStream)
        ) {
            final Node document = mdParser.parseReader(urlReader);
            final String html = mdRenderer.render(document);

            return new RsVelocity(
                getClass().getClassLoader().getResourceAsStream("./readme/readme.html"),
                new RsVelocity.Pair("readme", html),
                new RsVelocity.Pair("title", title),
                new RsVelocity.Pair("link", link)
            );
        }
    }
}
