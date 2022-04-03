package it.unich.jppl;

public enum GridGeneratorType {
    PPL_GRID_GENERATOR_TYPE_LINE(0),
    PPL_GRID_GENERATOR_TYPE_PARAMETER(1),
    PPL_GRID_GENERATOR_TYPE_POINT(2);

    int pplValue;

    GridGeneratorType (int pplValue) {
        this.pplValue = pplValue;
    }
}
