package org.nicvaltel.Common;

public interface RWS<R,W,S> {
    S get();
    void put(S state);
    R ask();
    void tell(W writer);
}
