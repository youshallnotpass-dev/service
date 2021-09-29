package dev.youshallnotpass.readme;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.cactoos.Scalar;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.cactoos.scalar.Checked;
import org.cactoos.text.TextOf;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsHtml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;

public final class TkReadme implements Take {

    private final Parser mdParser;
    private final HtmlRenderer mdRenderer;
    private final Checked<InputStream, IOException> readme;
    private final String title;
    private final String link;

    public TkReadme(final Scalar<URL> readmeUrl, final String title, final String link) {
        this(
            new Checked<InputStream, IOException>(() -> readmeUrl.value().openStream(), IOException::new),
            title,
            link
        );
    }

    public TkReadme(final URL readmeUrl, final String title, final String link) {
        this(
            new Checked<InputStream, IOException>(readmeUrl::openStream, IOException::new),
            title,
            link
        );
    }

    public TkReadme(final String readme, final String title, final String link) {
        this(
            new Checked<InputStream, IOException>(() -> new ByteArrayInputStream(readme.getBytes()), IOException::new),
            title,
            link
        );
    }

    public TkReadme(final Checked<InputStream, IOException> readme, final String title, final String link) {
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
        final Checked<InputStream, IOException> readme,
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
            final VelocityEngine engine = new VelocityEngine();
            final ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
            final StringWriter writer = new StringWriter();
            engine.evaluate(
                new VelocityContext(
                    new MapOf<>(
                        new MapEntry<>("readme", html),
                        new MapEntry<>("title", title),
                        new MapEntry<>("link", link)
                    )
                ),
                writer,
                "",
                new InputStreamReader(
                    classLoader.getResourceAsStream("readme/readme.html")
                )
            );

            return new RsHtml(new TextOf(writer.toString()).asString());
        }
    }
}
