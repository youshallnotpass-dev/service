package com.iwillfailyou;

import com.iwillfailyou.nullfree.db.MigrationsDb;
import com.iwillfailyou.nullfree.db.SimpleMigrations;
import com.iwillfailyou.nullfree.db.SqliteDb;
import com.iwillfailyou.nullfree.migrations.Migration0;
import com.iwillfailyou.nullfree.repo.DbRepos;
import com.iwillfailyou.nullfree.repo.RepoInfo;
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

public class App implements Take {

    private final Take origin;

    public App() {
        this(
            new TkFallback(
                new TkFork(
                    new FkRegex("/nullfree(/)?", "Nullfree"),
                    new FkRegex(
                        "/nullfree/.+",
                        new TkFork(
                            new FkRegex(
                                "/nullfree/(?<user>[^/]+)/(?<repo>[^/]+)",
                                new RepoInfo(
                                    new DbRepos(
                                        new MigrationsDb(
                                            new SqliteDb(new File("./iwfy.db")),
                                            new SimpleMigrations(
                                                new Migration0()
                                            ),
                                            1
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
            new App(),
            8080
        ).start(Exit.NEVER);
    }
}
