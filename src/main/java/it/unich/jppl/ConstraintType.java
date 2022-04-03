package it.unich.jppl;

public enum ConstraintType {
    PPL_CONSTRAINT_TYPE_LESS_THAN(0),
    PPL_CONSTRAINT_TYPE_LESS_OR_EQUAL(1),
    PPL_CONSTRAINT_TYPE_EQUAL(2),
    PPL_CONSTRAINT_TYPE_GREATER_OR_EQUAL(3),
    PPL_CONSTRAINT_TYPE_GREATER_THAN(4);

    int pplValue;

    ConstraintType (int pplValue) {
        this.pplValue = pplValue;
    }
}

