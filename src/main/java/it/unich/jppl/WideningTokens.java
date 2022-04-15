package it.unich.jppl;

/**
 * Class holding the number of tokens available for a widening with token.
 */
public class WideningTokens {
    /**
     * Number of abailable tokens.
     */
    int tokens;

    /**
     * Returns the number of available tokens.
     */
    public int getTokens() {
        return tokens;
    }

    /**
     * Sets the number of available tokens.
     */
    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    /**
     * Creates a {@code WideningTokens} object specifying the number of available
     * tokens.
     */
    public WideningTokens(int tokens) {
        this.tokens = tokens;
    }
}
