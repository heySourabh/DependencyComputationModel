package in.spbhat;

import in.spbhat.dcm.Computation;
import in.spbhat.dcm.Variable;

public class Main {
    public static void main(String[] args) {
        Variable P = new Variable("P", 2355);
        Variable N = new Variable("N", 5);
        Variable R = new Variable("R", 10);

        Variable I = new Variable("I", new Computation(P, N, R) {
            @Override
            public double compute() {
                Variable P = inputs[0];
                Variable N = inputs[1];
                Variable R = inputs[2];
                return P.value() * Math.pow(1 + R.value() / 100, N.value());
            }
        });

        Variable S = new Variable("S", 1);
        R.setComputation(new Computation(S) {
            @Override
            public double compute() {
                Variable S = inputs[0];
                return 10 + S.value();
            }
        });

        System.out.println(I);
        System.out.println(Variable.declaredVariables());
    }
}
