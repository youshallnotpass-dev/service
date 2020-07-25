package com.iwillfailyou;

import com.iwillfailyou.inspection.allfinal.TkAllfinal;
import com.iwillfailyou.inspection.allpublic.TkAllpublic;
import com.iwillfailyou.inspection.nullfree.TkNullfree;
import com.iwillfailyou.inspection.staticfree.TkStaticfree;
import com.iwillfailyou.readme.TkReadme;
import com.iwillfailyou.repo.DbRepos;
import com.iwillfailyou.repo.Repos;
import com.nikialeksey.jood.JdDb;
import org.cactoos.scalar.Solid;
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
import java.net.URL;
import java.sql.DriverManager;

public final class App implements Take {

    private final Take origin;

    public App(final Repos repos) {
        this(
            new TkFallback(
                new TkFork(
                    new FkRegex(
                        "(/)?",
                        new TkReadme(
                            () -> new URL("https://raw.githubusercontent.com/iwillfailyou/service/master/readme.md"),
                            "Iwillfailyou",
                            "https://github.com/iwillfailyou/service"
                        )
                    ),
                    new FkRegex(
                        "/nullfree(/)?",
                        new TkReadme(
                            () -> new URL("https://raw.githubusercontent.com/iwillfailyou/service/master/readme.md"),
                            "Nullfree",
                            "https://github.com/iwillfailyou/service"
                        )
                    ),
                    new FkRegex(
                        "/nullfree/.+",
                        new TkFork(
                            new FkRegex(
                                "/nullfree/(?<user>[^/]+)/(?<repo>[^/]+)",
                                new TkNullfree(
                                    repos
                                )
                            )
                        )
                    ),
                    new FkRegex(
                        "/staticfree(/)?",
                        new TkReadme(
                            () -> new URL("https://raw.githubusercontent.com/iwillfailyou/service/master/readme.md"),
                            "Staticfree",
                            "https://github.com/iwillfailyou/service"
                        )
                    ),
                    new FkRegex(
                        "/staticfree/.+",
                        new TkFork(
                            new FkRegex(
                                "/staticfree/(?<user>[^/]+)/(?<repo>[^/]+)",
                                new TkStaticfree(
                                    repos
                                )
                            )
                        )
                    ),
                    new FkRegex(
                        "/allfinal(/)?",
                        new TkReadme(
                            () -> new URL("https://raw.githubusercontent.com/iwillfailyou/service/master/readme.md"),
                            "Allfinal",
                            "https://github.com/iwillfailyou/service"
                        )
                    ),
                    new FkRegex(
                        "/allfinal/.+",
                        new TkFork(
                            new FkRegex(
                                "/allfinal/(?<user>[^/]+)/(?<repo>[^/]+)",
                                new TkAllfinal(
                                    repos
                                )
                            )
                        )
                    ),
                    new FkRegex(
                        "/allpublic(/)?",
                        new TkReadme(
                            () -> new URL("https://raw.githubusercontent.com/iwillfailyou/service/master/readme.md"),
                            "Allpublic",
                            "https://github.com/iwillfailyou/service"
                        )
                    ),
                    new FkRegex(
                        "/allpublic/.+",
                        new TkFork(
                            new FkRegex(
                                "/allpublic/(?<user>[^/]+)/(?<repo>[^/]+)",
                                new TkAllpublic(
                                    repos
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
    public Response act(final Request req) throws Exception {
        return origin.act(req);
    }

    @SuppressWarnings("staticfree")
    public static void main(final String... args) throws Exception {
        new FtBasic(
            new App(
                new DbRepos(
                    new IwfyDb(
                        new JdDb(
                            new Solid<>(
                                () -> DriverManager.getConnection(
                                    String.format(
                                        "jdbc:sqlite:%s",
                                        new File("./iwfy.db").getAbsolutePath()
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            8080
        ).start(Exit.NEVER);
    }
}
