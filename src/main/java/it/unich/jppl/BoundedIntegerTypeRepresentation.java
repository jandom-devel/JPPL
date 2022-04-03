package it.unich.jppl;

public enum BoundedIntegerTypeRepresentation {
    PPL_UNSIGNED(0),
    PPL_SIGNED_2_COMPLEMENT(1);

    int pplValue;

    BoundedIntegerTypeRepresentation (int pplValue) {
        this.pplValue = pplValue;
    }
}
