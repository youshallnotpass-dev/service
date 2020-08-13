package com.iwillfailyou.repo;

import com.iwillfailyou.IwfyException;
import com.iwillfailyou.inspection.Inspection;

public interface Repo {
    Inspection nullfree() throws IwfyException;
    Inspection staticfree() throws IwfyException;
    Inspection allfinal() throws IwfyException;
    Inspection allpublic() throws IwfyException;
    Inspection setterfree() throws IwfyException;
    Inspection nomultiplereturn() throws IwfyException;
    Inspection inheritancefree() throws IwfyException;

    final class Fake implements Repo {

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

        @Override
        public Inspection allpublic() throws IwfyException {
            return new Inspection.Fake();
        }

        @Override
        public Inspection setterfree() throws IwfyException {
            return new Inspection.Fake();
        }

        @Override
        public Inspection nomultiplereturn() throws IwfyException {
            return new Inspection.Fake();
        }

        @Override
        public Inspection inheritancefree() throws IwfyException {
            return new Inspection.Fake();
        }
    }
}
