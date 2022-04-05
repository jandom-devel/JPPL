package it.unich.jppl;

import java.util.Optional;

public interface Property<T extends Property<T>> {

    static class ExtremalOutput {
        public final Coefficient supN;
        public final Coefficient supD;
        public final boolean isMaximum;

        ExtremalOutput(Coefficient supN, Coefficient supD, boolean isMaximum) {
            this.supN = supN;
            this.supD = supD;
            this.isMaximum = isMaximum;
        }
    }

    public T assign(T p);
    public long getSpaceDimension();
    public long getAffineDimension();
    public int getRelationWithConstraint(Constraint c);
    // public int getRelationWithGenerator(Generator g);
    public ConstraintSystem getConstraints();
    // public CongruenceSystem getCongruences() {
    public ConstraintSystem getMinimizedConstraints();
    // public ConstraintSystem getMinimizedCongruences();
    public boolean isEmpty();
    public boolean isUniverse();
    public boolean isBounded();
    public boolean containsIntegerPoint();
    public boolean isTopologicallyClosed();
    public boolean isDiscrete();
    public boolean constraints(long var);
    public boolean boundsFromAbove(LinearExpression le);
    public boolean boundsFromBelow(LinearExpression le);
    public Optional<ExtremalOutput> maximize(LinearExpression le);
    public Optional<ExtremalOutput> minimize(LinearExpression le);
    public boolean contains(T ph);
    public boolean strictlyContains(T ph);
    public boolean isDisjointFrom(T ph);
    public boolean isOK();
    public long getExternalMemoryInBytes();
    public long getTotalMemoryInBytes();

    public T refineWithConstraint(Constraint c);
}
