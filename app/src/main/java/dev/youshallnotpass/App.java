package dev.youshallnotpass;

import dev.youshallnotpass.inspection.allfinal.TkAllfinal;
import dev.youshallnotpass.inspection.allpublic.TkAllpublic;
import dev.youshallnotpass.inspection.inheritancefree.TkInheritanceFree;
import dev.youshallnotpass.inspection.nomultiplereturn.TkNoMultipleReturn;
import dev.youshallnotpass.inspection.nullfree.TkNullfree;
import dev.youshallnotpass.inspection.setterfree.TkSetterFree;
import dev.youshallnotpass.inspection.staticfree.TkStaticfree;
import dev.youshallnotpass.readme.TkReadme;
import dev.youshallnotpass.repo.DbRepos;
import dev.youshallnotpass.repo.Repos;
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
                            () -> new URL("https://raw.githubusercontent.com/youshallnotpass-dev/service/master/readme.md"),
                            "Youshallnotpass",
                            "https://github.com/youshallnotpass-dev/service"
                        )
                    ),
                    new FkRegex(
                        "/nullfree(/)?",
                        new TkReadme(
                            () -> new URL("https://raw.githubusercontent.com/youshallnotpass-dev/service/master/readme.md"),
                            "NullFree",
                            "https://github.com/youshallnotpass-dev/service"
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
                            () -> new URL("https://raw.githubusercontent.com/youshallnotpass-dev/service/master/readme.md"),
                            "StaticFree",
                            "https://github.com/youshallnotpass-dev/service"
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
                            () -> new URL("https://raw.githubusercontent.com/youshallnotpass-dev/service/master/readme.md"),
                            "AllFinal",
                            "https://github.com/youshallnotpass-dev/service"
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
                            () -> new URL("https://raw.githubusercontent.com/youshallnotpass-dev/service/master/readme.md"),
                            "AllPublic",
                            "https://github.com/youshallnotpass-dev/service"
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
                    ),

                    new FkRegex(
                        "/nomultiplereturn(/)?",
                        new TkReadme(
                            () -> new URL("https://raw.githubusercontent.com/youshallnotpass-dev/service/master/readme.md"),
                            "NoMultipleReturn",
                            "https://github.com/youshallnotpass-dev/service"
                        )
                    ),
                    new FkRegex(
                        "/nomultiplereturn/.+",
                        new TkFork(
                            new FkRegex(
                                "/nomultiplereturn/(?<user>[^/]+)/(?<repo>[^/]+)",
                                new TkNoMultipleReturn(
                                    repos
                                )
                            )
                        )
                    ),

                    new FkRegex(
                        "/setterfree(/)?",
                        new TkReadme(
                            () -> new URL("https://raw.githubusercontent.com/youshallnotpass-dev/service/master/readme.md"),
                            "SetterFree",
                            "https://github.com/youshallnotpass-dev/service"
                        )
                    ),
                    new FkRegex(
                        "/setterfree/.+",
                        new TkFork(
                            new FkRegex(
                                "/setterfree/(?<user>[^/]+)/(?<repo>[^/]+)",
                                new TkSetterFree(
                                    repos
                                )
                            )
                        )
                    ),

                    new FkRegex(
                        "/inheritancefree(/)?",
                        new TkReadme(
                            () -> new URL("https://raw.githubusercontent.com/youshallnotpass-dev/service/master/readme.md"),
                            "InheritanceFree",
                            "https://github.com/youshallnotpass-dev/service"
                        )
                    ),
                    new FkRegex(
                        "/inheritancefree/.+",
                        new TkFork(
                            new FkRegex(
                                "/inheritancefree/(?<user>[^/]+)/(?<repo>[^/]+)",
                                new TkInheritanceFree(
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
                    new YsnpDb(
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
