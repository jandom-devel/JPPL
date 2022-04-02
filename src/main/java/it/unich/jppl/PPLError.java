package it.unich.jppl;

class PPLError extends Error {
    int pplError;

    PPLError(int pplError) {
        this.pplError = pplError;
    }
}
