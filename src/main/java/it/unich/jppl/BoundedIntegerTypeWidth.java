package it.unich.jppl;

public enum BoundedIntegerTypeWidth {
    PPL_BITS_8(8),
    PPL_BITS_16(16),
    PPL_BITS_32(32),
    PPL_BITS_64(64),
    PPL_BITS_128(128);

    int pplValue;

    BoundedIntegerTypeWidth (int pplValue) {
        this.pplValue = pplValue;
    }
}
