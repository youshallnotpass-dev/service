package com.nikialeksey.nullfree;

import org.takes.http.Exit;
import org.takes.http.FtBasic;

public class Main {
    public static void main(final String... args) throws Exception {
        new FtBasic(
            new App(),
            8080
        ).start(Exit.NEVER);
    }
}
