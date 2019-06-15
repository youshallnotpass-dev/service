package com.iwillfailyou;

import com.iwillfailyou.nullfree.db.SqliteDb;
import com.nikialeksey.nullfree.SimpleNullfree;
import com.nikialeksey.nullfree.badge.ShieldsIoBadge;
import com.nikialeksey.nullfree.sources.SimpleSources;
import com.nikialeksey.nullfree.sources.java.JavaSourceFileFactory;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Test;
import org.takes.http.FtRemote;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AppTest {

    @Test
    public void checkNullfreeCurrentProject() throws Exception {
        final String user = "user";
        final String repo = "repo";
        new FtRemote(new App(new SqliteDb())).exec((final URI home) -> {
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

    @Test
    public void sendNullDescriptions() throws Exception {
        final String user = "user";
        final String repo = "repo";
        new FtRemote(new App(new SqliteDb())).exec((final URI home) -> {
            try (
                final CloseableHttpClient httpClient = HttpClients.createDefault()
            ) {
                final URL queryUrl = new URL(home.toString() + "/nullfree/" + user + "/" + repo);

                final List<NameValuePair> form = new ArrayList<>();
                form.add(new BasicNameValuePair("null", "null 1"));
                form.add(new BasicNameValuePair("null", "null 2"));

                final HttpPost sendDescription = new HttpPost(queryUrl.toURI());
                sendDescription.setEntity(new UrlEncodedFormEntity(form, Consts.UTF_8));
                httpClient.execute(sendDescription);

                final HttpResponse response = httpClient.execute(
                    new HttpGet(queryUrl.toURI()),
                    HttpClientContext.create()
                );

                Assert.assertThat(
                    response.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );
                Assert.assertThat(
                    EntityUtils.toString(response.getEntity()),
                    StringContains.containsString("declined")
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}