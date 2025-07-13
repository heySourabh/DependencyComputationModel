package in.spbhat.dcm;

import java.util.HashSet;
import java.util.Set;

public class Variable {
    private enum State {
        UP_TO_DATE, COMPUTING, STALE
    }

    private static final Set<String> allVariables = new HashSet<>();

    private static final double MIN_TOLERANCE = 1e-15;
    private static final boolean ALLOW_LOOPS = true;

    private final Set<Variable> dependentVariables = new HashSet<>(1);
    private double tolerance = MIN_TOLERANCE;

    private final String name;
    private Computation computation;
    private double value;
    private State state;

    public Variable(String name) {
        this(name, 0, null, State.STALE);
    }

    public Variable(String name, double value) {
        this(name, value, null, State.UP_TO_DATE);
    }

    public Variable(String name, Computation computation) {
        this(name, 0, computation, State.STALE);
    }

    private Variable(String name, double initialValue, Computation computation, State state) {
        if (allVariables.contains(name)) {
            throw new IllegalArgumentException(name + ": variable already exists.");
        }
        allVariables.add(name);

        this.name = name;
        this.value = initialValue;
        this.state = state;

        this.computation = computation;
        resetDependentVariables(computation);
    }

    public void setComputation(Computation computation) {

        if (this.computation == computation) {
            return;
        }

        resetDependentVariables(computation);

        this.computation = computation;
        setStaleState();
    }

    public Variable withTolerance(double tolerance) {
        this.tolerance = Math.max(tolerance, MIN_TOLERANCE);
        return this;
    }

    public double value() {
        if (state == State.COMPUTING) {
            if (ALLOW_LOOPS) {
                return this.value;
            }
            throw new IllegalStateException(name + ": loop detected during computation.");
        }

        if (state == State.STALE) {
//            System.out.println(name + ": computing ...");
            if (computation == null) {
                throw new IllegalStateException(name + ": value or computation is not defined.");
            }
            state = State.COMPUTING;
            this.value = computation.compute();
            state = State.UP_TO_DATE;
        }

        return value;
    }

    public void setValue(double value) {
        if (this.computation != null) {
            throw new IllegalStateException(name + ": cannot set value of a dependent variable.");
        }

        this.state = State.UP_TO_DATE;

        if (Math.abs(this.value - value) <= tolerance) {
            this.value = value;
            return;
        }

        this.value = value;
        setStaleStateOfDependents();
    }

    public static Set<String> declaredVariables() {
        return Set.copyOf(allVariables);
    }

    @Override
    public String toString() {
        return name + ": " + value();
    }

    private void resetDependentVariables(Computation computation) {
        if (this.computation != null) {
            for (Variable input : this.computation.inputs) {
                input.dependentVariables.remove(this);
            }
        }

        if (computation == null) {
            return;
        }

        for (Variable input : computation.inputs) {
            input.dependentVariables.add(this);
        }
    }

    private void setStaleStateOfDependents() {
        for (Variable dependentVariable : dependentVariables) {
            dependentVariable.setStaleState();
        }
    }

    private void setStaleState() {
        if (this.state == State.STALE) {
            return;
        }
//        System.out.println(name + ": stale");
        this.state = State.STALE;
        for (Variable dependentVariable : dependentVariables) {
            dependentVariable.setStaleState();
        }
    }
}
