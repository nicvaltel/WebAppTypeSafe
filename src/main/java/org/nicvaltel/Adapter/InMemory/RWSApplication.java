package org.nicvaltel.Adapter.InMemory;

import org.nicvaltel.Common.RWS;

public class RWSApplication implements RWS<Void,Void,State> {
    private State state;

    public RWSApplication(State state) {
        this.state = state;
    }

    @Override
    public State get() {
        return this.state;
    }

    @Override
    public void put(State state) {
        this.state = state;
    }

    @Override
    public Void ask() {
        return null;
    }

    @Override
    public void tell(Void writer) {

    }
}
