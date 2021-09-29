package dev.youshallnotpass.readme;

import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Test;
import org.takes.rq.RqFake;
import org.takes.rs.RsPrint;

public final class TkReadmeTest {
    @Test
    public void mdReadme() throws Exception {
        Assert.assertThat(
            new RsPrint(
                new TkReadme(
                    "This is *Sparta*",
                    "Title",
                    "https://google.com"
                ).act(
                    new RqFake()
                )
            ).print(),
            StringContains.containsString("<em>Sparta</em>")
         );
    }

}