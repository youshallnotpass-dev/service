package com.iwillfailyou;

import com.iwillfailyou.nullfree.db.Db;
import com.iwillfailyou.nullfree.db.MigrationsDb;
import com.iwillfailyou.nullfree.db.SimpleMigrations;
import com.iwillfailyou.nullfree.db.SqliteDb;
import com.iwillfailyou.nullfree.migrations.Migration0;
import com.iwillfailyou.nullfree.migrations.Migration1;
import com.iwillfailyou.nullfree.repo.DbRepos;
import com.iwillfailyou.nullfree.repo.RepoInfo;
import com.iwillfailyou.readme.TkReadme;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.fallback.FbChain;
import org.takes.facets.fallback.FbStatus;
import org.takes.facets.fallback.TkFallback;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.Exit;
import org.takes.http.FtBasic;
import org.takes.rs.RsText;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class App implements Take {

    private final Take origin;

    public App(final Db db) {
        this(
            new TkFallback(
                new TkFork(
                    new FkRegex(
                        "/nullfree(/)?",
                        new TkReadme(
                            () -> new URL("https://raw.githubusercontent.com/nikialeksey/nullfree/master/readme.md"),
                            "Nullfree",
                            "https://github.com/nikialeksey/nullfree/"
                        )
                    ),
                    new FkRegex(
                        "/nullfree/.+",
                        new TkFork(
                            new FkRegex(
                                "/nullfree/(?<user>[^/]+)/(?<repo>[^/]+)",
                                new RepoInfo(
                                    new DbRepos(
                                        new MigrationsDb(
                                            db,
                                            new SimpleMigrations(
                                                new Migration0(),
                                                new Migration1()
                                            ),
                                            2
                                        )
                                    )
                                )
                            )
                        )
                    )
                ),
                new FbChain(
                    new FbStatus(404, new RsText("sorry, page is absent")),
                    new FbStatus(405, new RsText("this method is not allowed here"))
                )
            )
        );
    }

    private App(final Take origin) {
        this.origin = origin;
    }

    @Override
    public Response act(final Request req) throws IOException {
        return origin.act(req);
    }

    public static void main(final String... args) throws Exception {
        new FtBasic(
            new App(new SqliteDb(new File("./iwfy.db"))),
            8080
        ).start(Exit.NEVER);
    }
}
