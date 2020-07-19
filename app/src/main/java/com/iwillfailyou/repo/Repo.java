package com.iwillfailyou.repo;

import com.iwillfailyou.IwfyException;
import com.iwillfailyou.inspection.Inspection;

public interface Repo {
    Inspection nullfree() throws IwfyException;
    Inspection staticfree() throws IwfyException;
    Inspection allfinal() throws IwfyException;

    class Fake implements Repo {

        @Override
        public Inspection nullfree() throws IwfyException {
            return new Inspection.Fake();
        }

        @Override
        public Inspection staticfree() throws IwfyException {
            return new Inspection.Fake();
        }

        @Override
        public Inspection allfinal() throws IwfyException {
            return new Inspection.Fake();
        }
    }
}
