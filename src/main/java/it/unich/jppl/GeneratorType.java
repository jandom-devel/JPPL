package it.unich.jppl;

public enum GeneratorType {
    PPL_GENERATOR_TYPE_LINE(0),
    PPL_GENERATOR_TYPE_RAY(1),
    PPL_GENERATOR_TYPE_POINT(2),
    PPL_GENERATOR_TYPE_CLOSURE_POINT(3);

    int pplValue;

    GeneratorType (int pplValue) {
        this.pplValue = pplValue;
    }
}
