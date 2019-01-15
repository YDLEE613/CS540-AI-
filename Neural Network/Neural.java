import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Neural {
	public static double Max(double z) {
		if (z > 0) {
			return z;
		}
		return 0;
	}

	public static double Sigmoid(double z) {
		double tmp = 1 / (1 + Math.exp(-z));

		return tmp;
	}

	public static String fiveDig(double a) {
		return String.format("%.5f", a);
	}

	public static void main(String[] args) {
		String a = "800 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 0.1 10000";
		String[] b = a.split(" ");
		args = b;
		
		File fileTrain = new File("./hw2_midterm_A_train.txt");
		File fileEval = new File("./hw2_midterm_A_eval.txt");
		File fileTest = new File("./hw2_midterm_A_test.txt");

		double[] edges = new double[args.length];

		// add args to edges
		for (int i = 0; i < args.length; i++) {
			edges[i] = Double.parseDouble(args[i]);
		}

		int option = Integer.parseInt(args[0]) / 100;

		double u_a = 0.0;
		double v_a = 0.0;

		double u_b = 0.0;
		double v_b = 0.0;

		double u_c = 0.0;
		double v_c = 0.0;

		double x_1 = 0.0;
		double x_2 = 0.0;
		double y = 0.0;
		double one = 1.0;
		double stepSize = 0.0;
		double T = 0.0;

		if (args.length == 11) {// 600
			stepSize = edges[10];
		}
		if (args.length == 12) {// 700
			if (option != 7 && option != 8) {
				x_1 = edges[10];
				x_2 = edges[11];
			} else {
				stepSize = edges[10];
				T = edges[11];
			}
		}

		if (args.length == 13) {
			x_1 = edges[10];
			x_2 = edges[11];
			y = edges[12];
		}

		if (args.length == 14) {
			x_1 = edges[10];
			x_2 = edges[11];
			y = edges[12];
			stepSize = edges[13];
		}

		u_a = edges[1] * one + edges[2] * x_1 + edges[3] * x_2;
		v_a = Max(u_a);

		u_b = edges[4] * one + edges[5] * x_1 + edges[6] * x_2;
		v_b = Max(u_b);

		u_c = edges[7] * one + edges[8] * v_a + edges[9] * v_b;
		v_c = Sigmoid(u_c);

		// FLAG 100
		if (option == 1) {
			System.out.println(fiveDig(u_a) + " " + fiveDig(v_a) + " " + fiveDig(u_b) + " " + fiveDig(v_b) + " "
					+ fiveDig(u_c) + " " + fiveDig(v_c));
		}

		// FLAG 200
		if (option == 2) {
			double eVal = 0.5 * Math.pow(v_c - y, 2);
			double eDerv_ol = v_c - y; // E/v_c
			double eDerv_i = eDerv_ol * (v_c * (1 - v_c));// E/u_c
			System.out.println(fiveDig(eVal) + " " + fiveDig(eDerv_ol) + " " + fiveDig(eDerv_i));
		}

		double w_ac = edges[8];
		double w_bc = edges[9];

		double eDerv_ol_a = w_ac * (v_c - y) * (v_c * (1 - v_c));
		double eDerv_i_a = 0.0;
		if (u_a >= 0) {
			eDerv_i_a = eDerv_ol_a * 1;
		}

		double eDerv_ol_b = w_bc * (v_c - y) * (v_c * (1 - v_c));
		double eDerv_i_b = 0.0;
		if (u_b >= 0) {
			eDerv_i_b = eDerv_ol_b * 1;
		}

		double eDerv_i_c = (v_c - y) * (v_c * (1 - v_c));

		// FLAG 300
		if (option == 3) {
			System.out.println(fiveDig(eDerv_ol_a) + " " + fiveDig(eDerv_i_a) + " " + fiveDig(eDerv_ol_b) + " "
					+ fiveDig(eDerv_i_b));
		}

		// FLAG 400
		double w1a = eDerv_i_a * one;
		double w2a = eDerv_i_a * x_1;
		double w3a = eDerv_i_a * x_2;
		double w4b = eDerv_i_b * one;
		double w5b = eDerv_i_b * x_1;
		double w6b = eDerv_i_b * x_2;
		double w7c = eDerv_i_c * one;
		double w8c = eDerv_i_c * v_a;
		double w9c = eDerv_i_c * v_b;

		if (option == 4) {
			System.out.println(fiveDig(w1a) + " " + fiveDig(w2a) + " " + fiveDig(w3a) + " " + fiveDig(w4b) + " "
					+ fiveDig(w5b) + " " + fiveDig(w6b) + " " + fiveDig(w7c) + " " + fiveDig(w8c) + " " + fiveDig(w9c));
		}

		ArrayList<Double> newEdge = new ArrayList<Double>();
		double[] error = { w1a, w2a, w3a, w4b, w5b, w6b, w7c, w8c, w9c };

		// FLAG 500
		if (option == 5) {

			u_c = edges[7] * one + edges[8] * v_a + edges[9] * v_b;
			v_c = Sigmoid(u_c);

			for (int i = 1; i < 10; i++) {
				System.out.print(fiveDig(edges[i]) + " ");
			}
			System.out.println();
			System.out.println(fiveDig(0.5 * Math.pow(v_c - y, 2)));

			newEdge.add(0.0);
			for (int i = 1; i < 10; i++) {
				newEdge.add(edges[i] - stepSize * error[i - 1]);
				System.out.print(fiveDig(newEdge.get(i)) + " ");
			}

			System.out.println();

			u_a = newEdge.get(1) * one + newEdge.get(2) * x_1 + newEdge.get(3) * x_2;
			v_a = Max(u_a);
			u_b = newEdge.get(4) * one + newEdge.get(5) * x_1 + newEdge.get(6) * x_2;
			v_b = Max(u_b);
			u_c = newEdge.get(7) * one + newEdge.get(8) * v_a + newEdge.get(9) * v_b;
			v_c = Sigmoid(u_c);

			System.out.println(fiveDig(0.5 * Math.pow(v_c - y, 2)));
		}

		// FLAG 600
		if (option == 6) {
			ArrayList<double[]> train = data(fileTrain);
			ArrayList<double[]> eval = data(fileEval);
			double setError = 0;

			for (int i = 0; i < train.size(); i++) {

				double newX1 = train.get(i)[0];
				double newX2 = train.get(i)[1];
				double newY = train.get(i)[2];

				System.out.println(fiveDig(newX1) + " " + fiveDig(newX2) + " " + fiveDig(newY));

				u_a = edges[1] * one + edges[2] * newX1 + edges[3] * newX2;
				v_a = Max(u_a);

				u_b = edges[4] * one + edges[5] * newX1 + edges[6] * newX2;
				v_b = Max(u_b);

				u_c = edges[7] * one + edges[8] * v_a + edges[9] * v_b;
				v_c = Sigmoid(u_c);
				
				double w_ac2 = edges[8];
				double w_bc2 = edges[9];

				double eDerv_ol_a2 = w_ac2 * (v_c - newY) * (v_c * (1 - v_c));
				double eDerv_i_a2 = 0.0;
				if (u_a >= 0) {
					eDerv_i_a2 = eDerv_ol_a2 * 1;
				}

				double eDerv_ol_b2 = w_bc2 * (v_c - newY) * (v_c * (1 - v_c));
				double eDerv_i_b2 = 0.0;
				if (u_b >= 0) {
					eDerv_i_b2 = eDerv_ol_b2 * 1;
				}

				double eDerv_i_c2 = (v_c - newY) * (v_c * (1 - v_c));

				double newW1a = eDerv_i_a2 * one;
				double newW2a = eDerv_i_a2 * newX1;
				double newW3a = eDerv_i_a2 * newX2;
				double newW4b = eDerv_i_b2 * one;
				double newW5b = eDerv_i_b2 * newX1;
				double newW6b = eDerv_i_b2 * newX2;
				double newW7c = eDerv_i_c2 * one;
				double newW8c = eDerv_i_c2 * v_a;
				double newW9c = eDerv_i_c2 * v_b;
				
				double[] newError = { newW1a, newW2a, newW3a, newW4b, newW5b, newW6b, newW7c, newW8c, newW9c };
				for (int j = 0; j < newError.length; j++) {
					
					double tmp = 0;
					tmp = edges[j + 1] - stepSize * newError[j];
					System.out.print(fiveDig(tmp) + " ");
					edges[j + 1] = tmp;
				}
				System.out.println();

				setError = 0;
				for (int j = 0; j < eval.size(); j++) {
					double x1Eval = eval.get(j)[0];
					double x2Eval = eval.get(j)[1];
					double yEval = eval.get(j)[2];

					u_a = edges[1] * one + edges[2] * x1Eval + edges[3] * x2Eval;
					v_a = Max(u_a);

					u_b = edges[4] * one + edges[5] * x1Eval + edges[6] * x2Eval;
					v_b = Max(u_b);

					u_c = edges[7] * one + edges[8] * v_a + edges[9] * v_b;
					v_c = Sigmoid(u_c);

					setError += 0.5 * Math.pow(v_c - yEval, 2);
				}
				System.out.println(fiveDig(setError));
			}
		}

		// FLAG 700 || FLAG 800
		if (option == 7 || option == 8) {
			// 800
			double prevE = 0.0;
			double err = Double.MAX_VALUE;
			int count = 0;

			for (int k = 0; k < T; k++) {
				count++;// 800
				prevE = err;
				ArrayList<double[]> train = data(fileTrain);
				ArrayList<double[]> eval = data(fileEval);
				double setError = 0;

				for (int i = 0; i < train.size(); i++) {

					double newX1 = train.get(i)[0];
					double newX2 = train.get(i)[1];
					double newY = train.get(i)[2];

					u_a = edges[1] * one + edges[2] * newX1 + edges[3] * newX2;
					v_a = Max(u_a);

					u_b = edges[4] * one + edges[5] * newX1 + edges[6] * newX2;
					v_b = Max(u_b);

					u_c = edges[7] * one + edges[8] * v_a + edges[9] * v_b;
					v_c = Sigmoid(u_c);

					double w_ac2 = edges[8];
					double w_bc2 = edges[9];

					double eDerv_ol_a2 = w_ac2 * (v_c - newY) * (v_c * (1 - v_c));
					double eDerv_i_a2 = 0.0;
					if (u_a >= 0) {
						eDerv_i_a2 = eDerv_ol_a2 * 1;
					}

					double eDerv_ol_b2 = w_bc2 * (v_c - newY) * (v_c * (1 - v_c));
					double eDerv_i_b2 = 0.0;
					if (u_b >= 0) {
						eDerv_i_b2 = eDerv_ol_b2 * 1;
					}

					double eDerv_i_c2 = (v_c - newY) * (v_c * (1 - v_c));

					double newW1a = eDerv_i_a2 * one;
					double newW2a = eDerv_i_a2 * newX1;
					double newW3a = eDerv_i_a2 * newX2;
					double newW4b = eDerv_i_b2 * one;
					double newW5b = eDerv_i_b2 * newX1;
					double newW6b = eDerv_i_b2 * newX2;
					double newW7c = eDerv_i_c2 * one;
					double newW8c = eDerv_i_c2 * v_a;
					double newW9c = eDerv_i_c2 * v_b;

					double[] newError = { newW1a, newW2a, newW3a, newW4b, newW5b, newW6b, newW7c, newW8c, newW9c };
					for (int j = 0; j < newError.length; j++) {
						double tmp = 0;
						tmp = edges[j + 1] - stepSize * newError[j];
						edges[j + 1] = tmp;
					}

					setError = 0;
					for (int j = 0; j < eval.size(); j++) {
						double x1Eval = eval.get(j)[0];
						double x2Eval = eval.get(j)[1];
						double yEval = eval.get(j)[2];

						u_a = edges[1] * one + edges[2] * x1Eval + edges[3] * x2Eval;
						v_a = Max(u_a);

						u_b = edges[4] * one + edges[5] * x1Eval + edges[6] * x2Eval;
						v_b = Max(u_b);

						u_c = edges[7] * one + edges[8] * v_a + edges[9] * v_b;
						v_c = Sigmoid(u_c);

						setError += 0.5 * Math.pow(v_c - yEval, 2);

					}
				}
				err = setError;

				if (option == 8) {
					if (prevE < err) {
						System.out.println(count);
						for (int j = 0; j < 9; j++) {
							if(j == 8) {
								System.out.print(fiveDig(edges[j + 1]) + "\n");
							}else {
								System.out.print(fiveDig(edges[j + 1]) + " ");
							}
						}
						System.out.println(fiveDig(setError));
						break;
					}
				} else {
					for (int j = 0; j < 9; j++) {
						System.out.print(fiveDig(edges[j + 1]) + " ");
					}
					System.out.println();
					System.out.println(fiveDig(setError));
				}
			}
			if (option == 8) {
				ArrayList<double[]> test = data(fileTest);
				double count2 = 0;
				for (int i = 0; i < test.size(); i++) {
					double x1Test = test.get(i)[0];
					double x2Test = test.get(i)[1];
					double yTest = test.get(i)[2];

					u_a = edges[1] * one + edges[2] * x1Test + edges[3] * x2Test;
					v_a = Max(u_a);

					u_b = edges[4] * one + edges[5] * x1Test + edges[6] * x2Test;
					v_b = Max(u_b);

					u_c = edges[7] * one + edges[8] * v_a + edges[9] * v_b;
					v_c = Sigmoid(u_c);

					if (yTest == 1 && v_c >= 0.5) {
						count2++;
					}else if (yTest == 0 && v_c < 0.5) {
						count2++;
					}
				}
				System.out.println("count2: " + count2);
				System.out.println(test.size());
				System.out.println(fiveDig(count2 / test.size()));
			}

		}

	}

	public static ArrayList<double[]> data(File file) {
		ArrayList<double[]> dataSet = new ArrayList<double[]>();

		Scanner scnr = null;
		try {
			scnr = new Scanner(file);
			while (scnr.hasNextLine()) {
				String tmp = scnr.nextLine();
				String[] eachLine = tmp.split(" ");
				double[] numSet = new double[eachLine.length];

				for (int i = 0; i < numSet.length; i++) {
					numSet[i] = Double.parseDouble(eachLine[i]);
				}

				dataSet.add(numSet);
			}
		} catch (FileNotFoundException ex) {
			System.out.println("File Not Found Exception");
		} 

		return dataSet;
	}

}
