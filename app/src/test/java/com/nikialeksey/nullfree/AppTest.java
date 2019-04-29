package com.nikialeksey.nullfree;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;
import org.takes.http.FtRemote;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class AppTest {

    @Test
    public void checkSystemWithCurrentProject() throws Exception {
        final String user = "user";
        final String repo = "repo";
        new FtRemote(new App()).exec((final URI home) -> {
            try {
                new SimpleNullfree(
                    new SimpleSources(
                        new File("../"),
                        new JavaSourceFileFactory()
                    ),
                    ShieldsIoBadge::new
                ).badge().send(
                    new URL(
                        String.format(
                            "%s/nullfree/%s/%s",
                            home.toString(),
                            user,
                            repo
                        )
                    )
                );

                final URL saved = new URL(home.toString() + "/nullfree/" + user + "/" + repo);
                final HttpGet request = new HttpGet(saved.toURI());
                final CloseableHttpClient httpClient = HttpClients.createDefault();
                final HttpClientContext context = HttpClientContext.create();
                final CloseableHttpResponse response = httpClient.execute(
                    request,
                    context
                );

                Assert.assertThat(
                    response.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}