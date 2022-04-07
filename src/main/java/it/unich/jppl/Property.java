package it.unich.jppl;

import it.unich.jppl.Constraint.ConstraintType;

import java.util.Optional;

public interface Property<T extends Property<T>> {

    static class ExtremalOutput {
        public final Coefficient supN;
        public final Coefficient supD;
        public final boolean isMaximum;
        public final Generator point;

        ExtremalOutput(Coefficient supN, Coefficient supD, boolean isMaximum, Generator point) {
            this.supN = supN;
            this.supD = supD;
            this.isMaximum = isMaximum;
            this.point = point;
        }
    }

    public static class WideningToken {
        public int tokens;

        WideningToken(int tokens) {
            this.tokens = tokens;
        }
    }

    public T assign(T p);

    public long getSpaceDimension();

    public long getAffineDimension();

    public int getRelationWithConstraint(Constraint c);

    public int getRelationWithGenerator(Generator g);

    public ConstraintSystem getConstraints();

    public CongruenceSystem getCongruences();

    public ConstraintSystem getMinimizedConstraints();

    public CongruenceSystem getMinimizedCongruences();

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

    public Optional<ExtremalOutput> maximizeWithPoint(LinearExpression le);

    public Optional<ExtremalOutput> minimize(LinearExpression le);

    public Optional<ExtremalOutput> minimizeWithPoint(LinearExpression le);

    public boolean contains(T p);

    public boolean strictlyContains(T p);

    public boolean isDisjointFrom(T p);

    public boolean isOK();

    public long getExternalMemoryInBytes();

    public long getTotalMemoryInBytes();

    public T addConstraint(Constraint c);

    public T addCongruence(Congruence c);

    public T addConstraints(ConstraintSystem cs);

    public T addCongruences(CongruenceSystem cs);

    public T addReycledConstraints(ConstraintSystem cs);

    public T addRecycledCongruences(CongruenceSystem cs);

    public T refineWithConstraint(Constraint c);

    public T refineWithCongruence(Congruence c);

    public T refineWithConstraints(ConstraintSystem c);

    public T refineWithCongruences(CongruenceSystem c);

    public T intersectionAssign(T p);

    public T upperBoundAssign(T p);

    public T differenceAssign(T p);

    public T simplofyUsingContextAssign(T p);

    public T timeElapseAssign(T p);

    public T topologicalClosureAssign();

    public T unconstrainSpaceDimension(long var);

    public T unconstrainSpaceDimensions(long[] ds);

    public T affineImage(long var, LinearExpression le, Coefficient d);

    public T affinePreImage(long var, LinearExpression le, Coefficient d);

    public T boundedAffineImage(long var, LinearExpression lb, LinearExpression ub, Coefficient d);

    public T boundedAffinePreImage(long var, LinearExpression lb, LinearExpression ub, Coefficient d);

    public T generalizedAffineImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d);

    public T generalizedAffinePreImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d);

    public T generalizedAffineImageLhsRhs(LinearExpression lhs, ConstraintType relsym, LinearExpression rhs);

    public T generalizedAffinePreImageLhsRhs(LinearExpression lhs, ConstraintType relsym, LinearExpression rhs);

    public T concatenateAssign(T p);

    public T addSpaceDimensionsAndEmbed(long d);

    public T addSpaceDimensionsAndProject(long d);

    public T removeSpaceDimensions(long ds[]);

    public T removeHigherSpaceDimensions(long d);

    public T mapSpaceDimensions(long[] maps);

    public T expandSpaceDimension(long d, long m);

    public T foldSpaceDimensions(long[] ds, long d);
}
