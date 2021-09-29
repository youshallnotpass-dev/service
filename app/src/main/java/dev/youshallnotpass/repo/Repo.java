package dev.youshallnotpass.repo;

import dev.youshallnotpass.YsnpException;
import dev.youshallnotpass.inspection.Inspection;

public interface Repo {
    Inspection nullfree() throws YsnpException;
    Inspection staticfree() throws YsnpException;
    Inspection allfinal() throws YsnpException;
    Inspection allpublic() throws YsnpException;
    Inspection setterfree() throws YsnpException;
    Inspection nomultiplereturn() throws YsnpException;
    Inspection inheritancefree() throws YsnpException;

    final class Fake implements Repo {

        @Override
        public Inspection nullfree() throws YsnpException {
            return new Inspection.Fake();
        }

        @Override
        public Inspection staticfree() throws YsnpException {
            return new Inspection.Fake();
        }

        @Override
        public Inspection allfinal() throws YsnpException {
            return new Inspection.Fake();
        }

        @Override
        public Inspection allpublic() throws YsnpException {
            return new Inspection.Fake();
        }

        @Override
        public Inspection setterfree() throws YsnpException {
            return new Inspection.Fake();
        }

        @Override
        public Inspection nomultiplereturn() throws YsnpException {
            return new Inspection.Fake();
        }

        @Override
        public Inspection inheritancefree() throws YsnpException {
            return new Inspection.Fake();
        }
    }
}
