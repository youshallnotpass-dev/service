package com.iwillfailyou.nullfree.nulls;

import com.iwillfailyou.IwfyException;

import java.util.ArrayList;
import java.util.List;

public interface Nulls {

    void clear() throws IwfyException;

    Null add(String description) throws IwfyException;

    int count() throws IwfyException;

    class Fake implements Nulls {
        private final List<Null> nulls;

        public Fake() {
            this(new ArrayList<>());
        }

        public Fake(final List<Null> nulls) {
            this.nulls = nulls;
        }

        @Override
        public void clear() throws IwfyException {
            nulls.clear();
        }

        @Override
        public Null add(final String description) throws IwfyException {
            final Null.Fake result = new Null.Fake(description);
            nulls.add(result);
            return result;
        }

        @Override
        public int count() throws IwfyException {
            return nulls.size();
        }
    }
}
