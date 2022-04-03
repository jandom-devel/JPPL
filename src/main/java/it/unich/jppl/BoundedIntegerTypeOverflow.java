package it.unich.jppl;

public enum BoundedIntegerTypeOverflow {
    PPL_OVERFLOW_WRAPS(0),
    PPL_OVERFLOW_UNDEFINED(1),
    PPL_OVERFLOW_IMPOSSIBLE(2);

    int pplValue;

    BoundedIntegerTypeOverflow (int pplValue) {
        this.pplValue = pplValue;
    }
}
