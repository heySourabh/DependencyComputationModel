package in.spbhat.dcm.examples;

import in.spbhat.dcm.Computation;
import in.spbhat.dcm.Variable;

public class Laplacian {
    public static void main(String[] args) {
        int N = 11;
        double tolerance = 1e-3;
        Variable[][] phi = createPhiGrid(N, tolerance);
        setupInnerComputation(phi);
        setupBoundaryConditions(phi);

        int maxIterations = 50;

        System.out.println("phi[0][0] = " + phi[0][0].computeConvergedValue(maxIterations));
        System.out.println("phi[1][1] = " + phi[1][1].computeConvergedValue(maxIterations));
        System.out.println("phi[2][2] = " + phi[2][2].computeConvergedValue(maxIterations));
        System.out.println("phi[3][3] = " + phi[3][3].computeConvergedValue(maxIterations));
        System.out.println("phi[4][4] = " + phi[4][4].computeConvergedValue(maxIterations));
        System.out.println("phi[5][5] = " + phi[5][5].computeConvergedValue(maxIterations));
        System.out.println("phi[6][6] = " + phi[6][6].computeConvergedValue(maxIterations));
        System.out.println("phi[7][7] = " + phi[7][7].computeConvergedValue(maxIterations));
        System.out.println("phi[8][8] = " + phi[8][8].computeConvergedValue(maxIterations));
        System.out.println("phi[9][9] = " + phi[9][9].computeConvergedValue(maxIterations));
        System.out.println("phi[10][10] = " + phi[10][10].computeConvergedValue(maxIterations));
    }

    private static Variable[][] createPhiGrid(int N, double tolerance) {
        Variable[][] phi = new Variable[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                String variableName = "phi[%d][%d]".formatted(i, j);
                phi[i][j] = new Variable(variableName).withTolerance(tolerance);
            }
        }

        return phi;
    }

    private static void setupInnerComputation(Variable[][] phi) {
        int N = phi.length;
        for (int i = 1; i < N - 1; i++) {
            for (int j = 1; j < N - 1; j++) {
                Variable p = phi[i][j];
                Variable p_im1 = phi[i - 1][j];
                Variable p_ip1 = phi[i + 1][j];
                Variable p_jm1 = phi[i][j - 1];
                Variable p_jp1 = phi[i][j + 1];
                p.setComputation(new Computation(p_im1, p_ip1, p_jm1, p_jp1) {
                    @Override
                    public double compute() {
                        return 0.25 * (p_im1.value() + p_ip1.value() + p_jm1.value() + p_jp1.value());
                    }
                });
            }
        }
    }

    private static void setupBoundaryConditions(Variable[][] phi) {
        double lower = 0;
        double upper = 1;
        double left = 0;
        double right = 1;

        int N = phi.length;
        // lower and upper BC
        for (int i = 1; i < N - 1; i++) {
            int jl = 0; // lower j
            phi[i][jl].setValue(lower);

            int ju = N - 1;  // upper j
            phi[i][ju].setValue(upper);
        }

        // left and right BC
        for (int j = 0; j < N; j++) {
            int il = 0;  // left i
            phi[il][j].setValue(left);

            int ir = N - 1; // right i
            phi[ir][j].setValue(right);
        }
    }
}
