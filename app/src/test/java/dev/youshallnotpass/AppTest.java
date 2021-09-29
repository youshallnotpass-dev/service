package dev.youshallnotpass;

import dev.youshallnotpass.repo.DbRepos;
import com.nikialeksey.jood.Db;
import com.nikialeksey.jood.QueryResult;
import com.nikialeksey.jood.sql.JdSql;
import com.nikialeksey.nullfree.SimpleNullfree;
import com.nikialeksey.nullfree.badge.SimpleBadge;
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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public final class AppTest {

    @Test
    public void checkNullfreeCurrentProject() throws Exception {
        final String user = "user";
        final String repo = "repo";
        new FtRemote(new App(new DbRepos(new YsnpDb(new SqliteDb())))).exec((final URI home) -> {
            try {
                new SimpleNullfree(
                    new SimpleSources(
                        new File("../"),
                        new JavaSourceFileFactory()
                    ),
                    SimpleBadge::new
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
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void sendNullDescriptions() throws Exception {
        final String user = "user";
        final String repo = "repo";
        new FtRemote(new App(new DbRepos(new YsnpDb(new SqliteDb())))).exec((final URI home) -> {
            try (
                final CloseableHttpClient httpClient = HttpClients.createDefault()
            ) {
                final URL queryUrl = new URL(home.toString() + "/nullfree/" + user + "/" + repo);

                final List<NameValuePair> form = new ArrayList<>();
                form.add(new BasicNameValuePair("violation", "null 1"));
                form.add(new BasicNameValuePair("violation", "null 2"));

                final HttpPost sendDescription = new HttpPost(queryUrl.toURI());
                sendDescription.setEntity(new UrlEncodedFormEntity(form, Consts.UTF_8));
                final HttpResponse sendResponse = httpClient.execute(sendDescription);
                Assert.assertThat(
                    EntityUtils.toString(sendResponse.getEntity()),
                    sendResponse.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );

                final HttpResponse response = httpClient.execute(
                    new HttpGet(queryUrl.toURI()),
                    HttpClientContext.create()
                );

                final String responseEntity = EntityUtils.toString(response.getEntity());
                Assert.assertThat(
                    responseEntity,
                    response.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );
                Assert.assertThat(
                    responseEntity,
                    StringContains.containsString("declined")
                );
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void sendNullDescriptionsWithThreshold() throws Exception {
        final String user = "user";
        final String repo = "repo";
        final Db db = new YsnpDb(new SqliteDb());
        new FtRemote(new App(new DbRepos(db))).exec((final URI home) -> {
            try (
                final CloseableHttpClient httpClient = HttpClients.createDefault()
            ) {
                final URL queryUrl = new URL(home.toString() + "/nullfree/" + user + "/" + repo);

                final List<NameValuePair> form = new ArrayList<>();
                form.add(new BasicNameValuePair("null", "null 1"));
                form.add(new BasicNameValuePair("null", "null 2"));
                form.add(new BasicNameValuePair("threshold", "2"));

                final HttpPost sendDescription = new HttpPost(queryUrl.toURI());
                sendDescription.setEntity(new UrlEncodedFormEntity(form, Consts.UTF_8));
                final HttpResponse sendResponse = httpClient.execute(sendDescription);
                Assert.assertThat(
                    EntityUtils.toString(sendResponse.getEntity()),
                    sendResponse.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );

                final HttpResponse response = httpClient.execute(
                    new HttpGet(queryUrl.toURI()),
                    HttpClientContext.create()
                );

                try (
                    final QueryResult qr = db.read(
                        new JdSql(
                            "SELECT threshold FROM nullfree " +
                                "WHERE repo = 'user/repo'"
                        )
                    )
                ) {
                    final ResultSet rs = qr.rs();
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    final int threshold = rs.getInt("threshold");
                    Assert.assertThat(threshold, IsEqual.equalTo(2));
                }

                final String responseEntity = EntityUtils.toString(response.getEntity());
                Assert.assertThat(
                    responseEntity,
                    response.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );
                Assert.assertThat(
                    responseEntity,
                    StringContains.containsString("approved")
                );
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void sendStaticDescriptionsWithThreshold() throws Exception {
        final String user = "user";
        final String repo = "repo";
        final Db db = new YsnpDb(new SqliteDb());
        new FtRemote(new App(new DbRepos(db))).exec((final URI home) -> {
            try (
                final CloseableHttpClient httpClient = HttpClients.createDefault()
            ) {
                final URL queryUrl = new URL(home.toString() + "/staticfree/" + user + "/" + repo);

                final List<NameValuePair> form = new ArrayList<>();
                form.add(new BasicNameValuePair("violation", "violation 1"));
                form.add(new BasicNameValuePair("violation", "violation 2"));
                form.add(new BasicNameValuePair("threshold", "2"));

                final HttpPost sendDescription = new HttpPost(queryUrl.toURI());
                sendDescription.setEntity(new UrlEncodedFormEntity(form, Consts.UTF_8));
                final HttpResponse sendResponse = httpClient.execute(sendDescription);
                Assert.assertThat(
                    EntityUtils.toString(sendResponse.getEntity()),
                    sendResponse.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );

                try (
                    final QueryResult qr = db.read(
                        new JdSql(
                            "SELECT threshold FROM staticfree " +
                                "WHERE repo = 'user/repo'"
                        )
                    )
                ) {
                    final ResultSet rs = qr.rs();
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    final int threshold = rs.getInt("threshold");
                    Assert.assertThat(threshold, IsEqual.equalTo(2));
                }

                try (
                    final QueryResult qr = db.read(
                        new JdSql(
                            "SELECT description FROM staticfree_violation " +
                                "WHERE repo = 'user/repo'"
                        )
                    )
                ) {
                    final ResultSet rs = qr.rs();
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    Assert.assertThat(
                        rs.getString("description"),
                        StringContains.containsString("1")
                    );
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    Assert.assertThat(
                        rs.getString("description"),
                        StringContains.containsString("2")
                    );
                }

                final HttpResponse response = httpClient.execute(
                    new HttpGet(queryUrl.toURI()),
                    HttpClientContext.create()
                );
                final String responseEntity = EntityUtils.toString(response.getEntity());
                Assert.assertThat(
                    responseEntity,
                    response.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );
                Assert.assertThat(
                    responseEntity,
                    StringContains.containsString("approved")
                );
                Assert.assertThat(
                    responseEntity,
                    StringContains.containsString("staticfree")
                );
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void sendNonfinalDescriptionsWithThreshold() throws Exception {
        final String user = "user";
        final String repo = "repo";
        final Db db = new YsnpDb(new SqliteDb());
        new FtRemote(new App(new DbRepos(db))).exec((final URI home) -> {
            try (
                final CloseableHttpClient httpClient = HttpClients.createDefault()
            ) {
                final URL queryUrl = new URL(home.toString() + "/allfinal/" + user + "/" + repo);

                final List<NameValuePair> form = new ArrayList<>();
                form.add(new BasicNameValuePair("violation", "violation 1"));
                form.add(new BasicNameValuePair("violation", "violation 2"));
                form.add(new BasicNameValuePair("threshold", "2"));

                final HttpPost sendDescription = new HttpPost(queryUrl.toURI());
                sendDescription.setEntity(new UrlEncodedFormEntity(form, Consts.UTF_8));
                final HttpResponse sendResponse = httpClient.execute(sendDescription);
                Assert.assertThat(
                    EntityUtils.toString(sendResponse.getEntity()),
                    sendResponse.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );

                try (
                    final QueryResult qr = db.read(
                        new JdSql(
                            "SELECT threshold FROM allfinal " +
                                "WHERE repo = 'user/repo'"
                        )
                    )
                ) {
                    final ResultSet rs = qr.rs();
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    final int threshold = rs.getInt("threshold");
                    Assert.assertThat(threshold, IsEqual.equalTo(2));
                }

                try (
                    final QueryResult qr = db.read(
                        new JdSql(
                            "SELECT description FROM allfinal_violation " +
                                "WHERE repo = 'user/repo'"
                        )
                    )
                ) {
                    final ResultSet rs = qr.rs();
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    Assert.assertThat(
                        rs.getString("description"),
                        StringContains.containsString("1")
                    );
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    Assert.assertThat(
                        rs.getString("description"),
                        StringContains.containsString("2")
                    );
                }

                final HttpResponse response = httpClient.execute(
                    new HttpGet(queryUrl.toURI()),
                    HttpClientContext.create()
                );
                final String responseEntity = EntityUtils.toString(response.getEntity());
                Assert.assertThat(
                    responseEntity,
                    response.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );
                Assert.assertThat(
                    responseEntity,
                    StringContains.containsString("approved")
                );
                Assert.assertThat(
                    responseEntity,
                    StringContains.containsString("allfinal")
                );
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void sendNonpublicDescriptionsWithThreshold() throws Exception {
        final String user = "user";
        final String repo = "repo";
        final Db db = new YsnpDb(new SqliteDb());
        new FtRemote(new App(new DbRepos(db))).exec((final URI home) -> {
            try (
                final CloseableHttpClient httpClient = HttpClients.createDefault()
            ) {
                final URL queryUrl = new URL(home.toString() + "/allpublic/" + user + "/" + repo);

                final List<NameValuePair> form = new ArrayList<>();
                form.add(new BasicNameValuePair("violation", "violation 1"));
                form.add(new BasicNameValuePair("violation", "violation 2"));
                form.add(new BasicNameValuePair("threshold", "2"));

                final HttpPost sendDescription = new HttpPost(queryUrl.toURI());
                sendDescription.setEntity(new UrlEncodedFormEntity(form, Consts.UTF_8));
                final HttpResponse sendResponse = httpClient.execute(sendDescription);
                Assert.assertThat(
                    EntityUtils.toString(sendResponse.getEntity()),
                    sendResponse.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );

                try (
                    final QueryResult qr = db.read(
                        new JdSql(
                            "SELECT threshold FROM allpublic " +
                                "WHERE repo = 'user/repo'"
                        )
                    )
                ) {
                    final ResultSet rs = qr.rs();
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    final int threshold = rs.getInt("threshold");
                    Assert.assertThat(threshold, IsEqual.equalTo(2));
                }

                try (
                    final QueryResult qr = db.read(
                        new JdSql(
                            "SELECT description FROM allpublic_violation " +
                                "WHERE repo = 'user/repo'"
                        )
                    )
                ) {
                    final ResultSet rs = qr.rs();
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    Assert.assertThat(
                        rs.getString("description"),
                        StringContains.containsString("1")
                    );
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    Assert.assertThat(
                        rs.getString("description"),
                        StringContains.containsString("2")
                    );
                }

                final HttpResponse response = httpClient.execute(
                    new HttpGet(queryUrl.toURI()),
                    HttpClientContext.create()
                );
                final String responseEntity = EntityUtils.toString(response.getEntity());
                Assert.assertThat(
                    responseEntity,
                    response.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );
                Assert.assertThat(
                    responseEntity,
                    StringContains.containsString("approved")
                );
                Assert.assertThat(
                    responseEntity,
                    StringContains.containsString("allpublic")
                );
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void sendSetterFreeDescriptionsWithThreshold() throws Exception {
        final String user = "user";
        final String repo = "repo";
        final Db db = new YsnpDb(new SqliteDb());
        new FtRemote(new App(new DbRepos(db))).exec((final URI home) -> {
            try (
                final CloseableHttpClient httpClient = HttpClients.createDefault()
            ) {
                final URL queryUrl = new URL(home.toString() + "/setterfree/" + user + "/" + repo);

                final List<NameValuePair> form = new ArrayList<>();
                form.add(new BasicNameValuePair("violation", "violation 1"));
                form.add(new BasicNameValuePair("violation", "violation 2"));
                form.add(new BasicNameValuePair("threshold", "2"));

                final HttpPost sendDescription = new HttpPost(queryUrl.toURI());
                sendDescription.setEntity(new UrlEncodedFormEntity(form, Consts.UTF_8));
                final HttpResponse sendResponse = httpClient.execute(sendDescription);
                Assert.assertThat(
                    EntityUtils.toString(sendResponse.getEntity()),
                    sendResponse.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );

                try (
                    final QueryResult qr = db.read(
                        new JdSql(
                            "SELECT threshold FROM setterfree " +
                                "WHERE repo = 'user/repo'"
                        )
                    )
                ) {
                    final ResultSet rs = qr.rs();
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    final int threshold = rs.getInt("threshold");
                    Assert.assertThat(threshold, IsEqual.equalTo(2));
                }

                try (
                    final QueryResult qr = db.read(
                        new JdSql(
                            "SELECT description FROM setterfree_violation " +
                                "WHERE repo = 'user/repo'"
                        )
                    )
                ) {
                    final ResultSet rs = qr.rs();
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    Assert.assertThat(
                        rs.getString("description"),
                        StringContains.containsString("1")
                    );
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    Assert.assertThat(
                        rs.getString("description"),
                        StringContains.containsString("2")
                    );
                }

                final HttpResponse response = httpClient.execute(
                    new HttpGet(queryUrl.toURI()),
                    HttpClientContext.create()
                );
                final String responseEntity = EntityUtils.toString(response.getEntity());
                Assert.assertThat(
                    responseEntity,
                    response.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );
                Assert.assertThat(
                    responseEntity,
                    StringContains.containsString("approved")
                );
                Assert.assertThat(
                    responseEntity,
                    StringContains.containsString("setterfree")
                );
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void sendMultipleReturnDescriptionsWithThreshold() throws Exception {
        final String user = "user";
        final String repo = "repo";
        final Db db = new YsnpDb(new SqliteDb());
        new FtRemote(new App(new DbRepos(db))).exec((final URI home) -> {
            try (
                final CloseableHttpClient httpClient = HttpClients.createDefault()
            ) {
                final URL queryUrl = new URL(home.toString() + "/nomultiplereturn/" + user + "/" + repo);

                final List<NameValuePair> form = new ArrayList<>();
                form.add(new BasicNameValuePair("violation", "violation 1"));
                form.add(new BasicNameValuePair("violation", "violation 2"));
                form.add(new BasicNameValuePair("threshold", "2"));

                final HttpPost sendDescription = new HttpPost(queryUrl.toURI());
                sendDescription.setEntity(new UrlEncodedFormEntity(form, Consts.UTF_8));
                final HttpResponse sendResponse = httpClient.execute(sendDescription);
                Assert.assertThat(
                    EntityUtils.toString(sendResponse.getEntity()),
                    sendResponse.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );

                try (
                    final QueryResult qr = db.read(
                        new JdSql(
                            "SELECT threshold FROM nomultiplereturn " +
                                "WHERE repo = 'user/repo'"
                        )
                    )
                ) {
                    final ResultSet rs = qr.rs();
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    final int threshold = rs.getInt("threshold");
                    Assert.assertThat(threshold, IsEqual.equalTo(2));
                }

                try (
                    final QueryResult qr = db.read(
                        new JdSql(
                            "SELECT description FROM nomultiplereturn_violation " +
                                "WHERE repo = 'user/repo'"
                        )
                    )
                ) {
                    final ResultSet rs = qr.rs();
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    Assert.assertThat(
                        rs.getString("description"),
                        StringContains.containsString("1")
                    );
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    Assert.assertThat(
                        rs.getString("description"),
                        StringContains.containsString("2")
                    );
                }

                final HttpResponse response = httpClient.execute(
                    new HttpGet(queryUrl.toURI()),
                    HttpClientContext.create()
                );
                final String responseEntity = EntityUtils.toString(response.getEntity());
                Assert.assertThat(
                    responseEntity,
                    response.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );
                Assert.assertThat(
                    responseEntity,
                    StringContains.containsString("approved")
                );
                Assert.assertThat(
                    responseEntity,
                    StringContains.containsString("nomultiplereturn")
                );
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void sendInheritanceFreeDescriptionsWithThreshold() throws Exception {
        final String user = "user";
        final String repo = "repo";
        final Db db = new YsnpDb(new SqliteDb());
        new FtRemote(new App(new DbRepos(db))).exec((final URI home) -> {
            try (
                final CloseableHttpClient httpClient = HttpClients.createDefault()
            ) {
                final URL queryUrl = new URL(home.toString() + "/inheritancefree/" + user + "/" + repo);

                final List<NameValuePair> form = new ArrayList<>();
                form.add(new BasicNameValuePair("violation", "violation 1"));
                form.add(new BasicNameValuePair("violation", "violation 2"));
                form.add(new BasicNameValuePair("threshold", "2"));

                final HttpPost sendDescription = new HttpPost(queryUrl.toURI());
                sendDescription.setEntity(new UrlEncodedFormEntity(form, Consts.UTF_8));
                final HttpResponse sendResponse = httpClient.execute(sendDescription);
                Assert.assertThat(
                    EntityUtils.toString(sendResponse.getEntity()),
                    sendResponse.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );

                try (
                    final QueryResult qr = db.read(
                        new JdSql(
                            "SELECT threshold FROM inheritancefree " +
                                "WHERE repo = 'user/repo'"
                        )
                    )
                ) {
                    final ResultSet rs = qr.rs();
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    final int threshold = rs.getInt("threshold");
                    Assert.assertThat(threshold, IsEqual.equalTo(2));
                }

                try (
                    final QueryResult qr = db.read(
                        new JdSql(
                            "SELECT description FROM inheritancefree_violation " +
                                "WHERE repo = 'user/repo'"
                        )
                    )
                ) {
                    final ResultSet rs = qr.rs();
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    Assert.assertThat(
                        rs.getString("description"),
                        StringContains.containsString("1")
                    );
                    Assert.assertThat(rs.next(), IsEqual.equalTo(true));
                    Assert.assertThat(
                        rs.getString("description"),
                        StringContains.containsString("2")
                    );
                }

                final HttpResponse response = httpClient.execute(
                    new HttpGet(queryUrl.toURI()),
                    HttpClientContext.create()
                );
                final String responseEntity = EntityUtils.toString(response.getEntity());
                Assert.assertThat(
                    responseEntity,
                    response.getStatusLine().getStatusCode(),
                    IsEqual.equalTo(HttpURLConnection.HTTP_OK)
                );
                Assert.assertThat(
                    responseEntity,
                    StringContains.containsString("approved")
                );
                Assert.assertThat(
                    responseEntity,
                    StringContains.containsString("inheritancefree")
                );
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}