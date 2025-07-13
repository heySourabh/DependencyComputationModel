package in.spbhat.examples;

import in.spbhat.dcm.Computation;
import in.spbhat.dcm.Variable;

public class CompoundInterest {
    public static void main(String[] args) {
        Variable P = new Variable("P", 2355);
        Variable N = new Variable("N", 5);
        Variable R = new Variable("R", 8);

        Variable I = new Variable("I", new Computation(P, N, R) {
            @Override
            public double compute() {
                Variable P = inputs[0];
                Variable N = inputs[1];
                Variable R = inputs[2];
                return P.value() * Math.pow(1 + R.value() / 100, N.value());
            }
        });

        System.out.println(I);
        System.out.println(Variable.declaredVariables());
    }
}
