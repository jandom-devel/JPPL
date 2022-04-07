package it.unich.jppl;

import static it.unich.jppl.nativelib.LibPPL.*;

import it.unich.jppl.nativelib.LibPPL.DimensionByReference;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class GridGeneratorSystem implements Iterable<Generator> {
    Pointer pplObj;

    private static class GridGeneratorSystemCleaner implements Runnable {
        private Pointer pplObj;

        GridGeneratorSystemCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Grid_Generator_System(pplObj);
        }
    }

    public class GridGeneratorSystemIterator implements Iterator<Generator> {
        private Pointer cit;
        private Pointer cend;

        GridGeneratorSystemIterator() {
            var pgsit = new PointerByReference();
            int result = ppl_new_Grid_Generator_System_const_iterator(pgsit);
            if (result < 0)
                throw new PPLError(result);
            cit = pgsit.getValue();
            result = ppl_Grid_Generator_System_begin(pplObj, cit);
            if (result < 0)
                throw new PPLError(result);
            result = ppl_new_Grid_Generator_System_const_iterator(pgsit);
            if (result < 0)
                throw new PPLError(result);
            cend = pgsit.getValue();
            result = ppl_Grid_Generator_System_end(pplObj, cend);
            if (result < 0)
                throw new PPLError(result);
        }

        @Override
        public boolean hasNext() {
            int result = ppl_Grid_Generator_System_const_iterator_equal_test(cit, cend);
            if (result < 0)
                throw new PPLError(result);
            return result == 0;
        }

        @Override
        public Generator next() {
            if (!hasNext())
                throw new NoSuchElementException();
            var pc = new PointerByReference();
            int result = ppl_Grid_Generator_System_const_iterator_dereference(cit, pc);
            if (result < 0)
                throw new PPLError(result);
            result = ppl_Grid_Generator_System_const_iterator_increment(cit);
            if (result < 0)
                throw new PPLError(result);
            return new Generator(pc.getValue());
        }
    }

    private void init(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new GridGeneratorSystemCleaner(pplObj));
    }

    public GridGeneratorSystem() {
        var pgs = new PointerByReference();
        int result = ppl_new_Grid_Generator_System(pgs);
        if (result < 0)
            throw new PPLError(result);
        init(pgs.getValue());
    }

    public GridGeneratorSystem(GridGenerator g) {
        var pgs = new PointerByReference();
        int result = ppl_new_Grid_Generator_System_from_Grid_Generator(pgs, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pgs.getValue());
    }

    public GridGeneratorSystem(GridGeneratorSystem gs) {
        this(gs.pplObj);
    }

    GridGeneratorSystem(Pointer gs) {
        var pgs = new PointerByReference();
        int result = ppl_new_Grid_Generator_System_from_Grid_Generator_System(pgs, gs);
        if (result < 0)
            throw new PPLError(result);
        init(pgs.getValue());
    }

    public GridGeneratorSystem assign(GridGeneratorSystem gs) {
        int result = ppl_assign_Grid_Generator_System_from_Grid_Generator_System(pplObj, gs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public long getSpaceDimension() {
        var m = new DimensionByReference();
        int result = ppl_Grid_Generator_System_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    public boolean isEmpty() {
        int result = ppl_Grid_Generator_System_empty(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean isOK() {
        int result = ppl_Grid_Generator_System_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public GridGeneratorSystem clear() {
        int result = ppl_Grid_Generator_System_clear(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public GridGeneratorSystem add(GridGenerator g) {
        int result = ppl_Grid_Generator_System_insert_Grid_Generator(pplObj, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public Iterator<Generator> iterator() {
        return new GridGeneratorSystemIterator();
    }

    public String toString() {
        var pstr = new PointerByReference();
        int result = ppl_io_asprint_Grid_Generator_System(pstr, pplObj);
        if (result < 0)
            throw new PPLError(result);
        var p = pstr.getValue();
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }
}
