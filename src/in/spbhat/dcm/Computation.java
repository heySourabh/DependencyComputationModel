package in.spbhat.dcm;

public abstract class Computation {
    public final Variable[] inputs;

    public Computation(Variable... inputs) {
        this.inputs = inputs;
    }

    public abstract double compute();
}
