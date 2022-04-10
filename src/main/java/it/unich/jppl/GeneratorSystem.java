package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeTByReference;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class GeneratorSystem implements Iterable<Generator> {
    Pointer pplObj;

    public enum ZeroDimGeneratorSystem {
        EMPTY, UNSATISFIABLE
    }

    private static class GeneratorSystemCleaner implements Runnable {
        private Pointer pplObj;

        GeneratorSystemCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Generator_System(pplObj);
        }
    }

    private static class GeneratorSystemIteratorCleaner implements Runnable {
        private Pointer pplObj;

        GeneratorSystemIteratorCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Generator_System_const_iterator(pplObj);
        }
    }

    public class GeneratorSystemIterator implements Iterator<Generator> {
        private Pointer cit;
        private Pointer cend;

        GeneratorSystemIterator() {
            var pgsit = new PointerByReference();
            int result = ppl_new_Generator_System_const_iterator(pgsit);
            if (result < 0)
                throw new PPLError(result);
            cit = pgsit.getValue();
            PPL.cleaner.register(this, new GeneratorSystemIteratorCleaner(cit));
            result = ppl_Generator_System_begin(pplObj, cit);
            if (result < 0)
                throw new PPLError(result);
            result = ppl_new_Generator_System_const_iterator(pgsit);
            if (result < 0)
                throw new PPLError(result);
            cend = pgsit.getValue();
            PPL.cleaner.register(this, new GeneratorSystemIteratorCleaner(cend));
            result = ppl_Generator_System_end(pplObj, cend);
            if (result < 0)
                throw new PPLError(result);
        }

        @Override
        public boolean hasNext() {
            int result = ppl_Generator_System_const_iterator_equal_test(cit, cend);
            if (result < 0)
                throw new PPLError(result);
            return result == 0;
        }

        @Override
        public Generator next() {
            if (!hasNext())
                throw new NoSuchElementException();
            var pc = new PointerByReference();
            int result = ppl_Generator_System_const_iterator_dereference(cit, pc);
            if (result < 0)
                throw new PPLError(result);
            result = ppl_Generator_System_const_iterator_increment(cit);
            if (result < 0)
                throw new PPLError(result);
            return new Generator(pc.getValue());
        }
    }

    private void init(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new GeneratorSystemCleaner(pplObj));
    }

    public GeneratorSystem() {
        var pgs = new PointerByReference();
        int result = ppl_new_Generator_System(pgs);
        if (result < 0)
            throw new PPLError(result);
        init(pgs.getValue());
    }

    public GeneratorSystem(Generator g) {
        var pgs = new PointerByReference();
        int result = ppl_new_Generator_System_from_Generator(pgs, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pgs.getValue());
    }

    public GeneratorSystem(GeneratorSystem gs) {
        var pgs = new PointerByReference();
        int result = ppl_new_Generator_System_from_Generator_System(pgs, gs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pgs.getValue());
    }

    GeneratorSystem(Pointer pplObj) {
        init(pplObj);
    }

    public GeneratorSystem assign(GeneratorSystem gs) {
        int result = ppl_assign_Generator_System_from_Generator_System(pplObj, gs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Generator_System_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    public boolean isEmpty() {
        int result = ppl_Generator_System_empty(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean isOK() {
        int result = ppl_Generator_System_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public GeneratorSystem clear() {
        int result = ppl_Generator_System_clear(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public GeneratorSystem add(Generator g) {
        int result = ppl_Generator_System_insert_Generator(pplObj, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public Iterator<Generator> iterator() {
        return new GeneratorSystemIterator();
    }

    public String toString() {
        var pstr = new PointerByReference();
        int result = ppl_io_asprint_Generator_System(pstr, pplObj);
        if (result < 0)
            throw new PPLError(result);
        var p = pstr.getValue();
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }
}
