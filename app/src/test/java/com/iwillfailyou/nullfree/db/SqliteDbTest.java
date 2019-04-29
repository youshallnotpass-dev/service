package com.nikialeksey.nullfree.db;

import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;

public class SqliteDbTest {
    @Test
    public void select() throws Exception {
        final Db db = new SqliteDb();
        db.write("CREATE TABLE a (`name` TEXT NOT NULL)", new String[]{});
        db.write("INSERT INTO a (`name`) VALUES (?)", new String[]{"A"});
        try (
            final QueryResult result = db.read("SELECT `name` FROM a", new String[]{})
        ) {
            final ResultSet rs = result.rs();
            Assert.assertThat(rs.next(), IsEqual.equalTo(true));
            Assert.assertThat(rs.getString("name"), IsEqual.equalTo("A"));
        }
    }
}