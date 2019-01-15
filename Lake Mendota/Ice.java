import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Ice {
	public static void main(String[] args) {
		int option = Integer.parseInt(args[0]) / 100;

		// read file
		File file = new File("./data.txt");

		ArrayList<Integer> years = new ArrayList<Integer>();
		ArrayList<Integer> ice = new ArrayList<Integer>();

		Scanner content = null;

		if (file != null) {
			try {
				content = new Scanner(file);

				while (content.hasNext()) {
					String tmp = content.nextLine();
					String[] temp = tmp.split(" ");
					years.add(Integer.parseInt(temp[0]));
					ice.add(Integer.parseInt(temp[1]));
				}
			} catch (FileNotFoundException e) {
				System.out.println("File Not Found");
			}
		}

		double yearsSum = 0.0;
		double iceSum = 0.0;
		double stdDev = 0.0;
		double arg1 = 0.0;
		double arg2 = 0.0;

		if (args.length == 2) {
			arg1 = Double.parseDouble(args[1]);
		}
		if (args.length == 3) {
			arg1 = Double.parseDouble(args[1]);
			arg2 = Double.parseDouble(args[2]);
		}

		for (int i = 0; i < years.size(); i++) {
			yearsSum += years.get(i);
			iceSum += ice.get(i);
		}

		double yearsMean = yearsSum / years.size();
		double iceMean = iceSum / ice.size();
		
		// FLAG 100
		if (option == 1) {
			for (int i = 0; i < years.size(); i++) {
				System.out.println(years.get(i) + " " + ice.get(i));
			}
		}

		// FLAG 200
		if (option == 2) {
			System.out.println(ice.size());

			System.out.format("%.2f%n", iceMean);

			for (int i = 0; i < ice.size(); i++) {
				stdDev += Math.pow(((double) ice.get(i) - iceMean), 2);
			}
			stdDev = Math.sqrt(stdDev / (double) (ice.size() - 1));
			System.out.format("%.2f%n", stdDev);
		}

		// FLAG 300
		if (option == 3) {
			double mse = 0;
			for (int i = 0; i < years.size(); i++) {
				mse += Math.pow((arg1 + arg2 * years.get(i) - ice.get(i)), 2);
			}
			System.out.format("%.2f%n", mse / years.size());
		}

		// FLAG 400
		if (option == 4) {
			double mse1 = 0;
			double mse2 = 0;

			for (int i = 0; i < years.size(); i++) {
				mse1 += arg1 + arg2 * years.get(i) - ice.get(i);
				mse2 += (arg1 + arg2 * years.get(i) - ice.get(i)) * years.get(i);
			}
			System.out.format("%.2f%n", mse1 * (2.0 / years.size()));
			System.out.format("%.2f%n", mse2 * (2.0 / years.size()));
		}

		// FLAG 500
		if (option == 5) {
			double b_0 = 0.0;
			double b_1 = 0.0;

			for (int i = 0; i < arg2; i++) {
				double tmp = 0;
				double tmp1 = 0;
				double mseB = 0.0;

				for (int j = 0; j < years.size(); j++) {
					tmp += b_0 + b_1 * years.get(j) - ice.get(j);
					tmp1 += (b_0 + b_1 * years.get(j) - ice.get(j)) * years.get(j);
				}

				b_0 -= arg1 * 2 * tmp / years.size();
				b_1 -= arg1 * 2 * tmp1 / years.size();

				for (int j = 0; j < years.size(); j++) {
					mseB += Math.pow((b_0 + b_1 * years.get(j) - ice.get(j)), 2);
				}
				System.out.print(i + 1 + " ");
				System.out.format("%.2f ", b_0);
				System.out.format("%.2f ", b_1);
				System.out.format("%.2f%n", mseB / years.size());
			}
		}

		// FLAG 600 + FLAG 700
		if (option == 6 || option == 7) {
			double b_0 = 0.0;
			double b_1 = 0.0;
			double tmp = 0.0;

			for (int i = 0; i < years.size(); i++) {
				b_1 += (years.get(i) - yearsMean) * (ice.get(i) - iceMean);
			}
			for (int i = 0; i < years.size(); i++) {
				tmp += Math.pow(years.get(i) - yearsMean, 2);
			}
			b_1 = b_1 / tmp;
			b_0 = iceMean - b_1 * yearsMean;

			if (option == 6) {
				System.out.format("%.2f ", b_0);
				System.out.format("%.2f", b_1);
			}else {
				System.out.format("%.2f", b_0 + (b_1 * arg1));
			}
		}

		// FLAG 800
		if (option == 8) {
			double b_0 = 0.0;
			double b_1 = 0.0;
			double std = 0;
			double sum = 0.0;

			for (int i = 0; i < years.size(); i++) {
				sum += Math.pow(years.get(i) - yearsMean, 2);
			}
			std = Math.sqrt(sum / (years.size() - 1));
			for (int i = 0; i < arg2; i++) {
				double tmp = 0;
				double tmp1 = 0;
				double tmp2 = 0.0;

				for (int j = 0; j < years.size(); j++) {
					tmp += b_0 + b_1 * ((years.get(j) - yearsMean) / std) - ice.get(j);
					tmp1 += (b_0 + b_1 * ((years.get(j) - yearsMean) / std) - ice.get(j)) * (years.get(j) - yearsMean)
							/ std;
				}

				b_0 -= arg1 * 2 * tmp / years.size();
				b_1 -= arg1 * 2 * tmp1 / years.size();

				for (int j = 0; j < years.size(); j++) {
					tmp2 += Math.pow((b_0 + b_1 * ((years.get(j) - yearsMean) / std) - ice.get(j)), 2);
				}

				System.out.print(i + 1 + " ");
				System.out.format("%.2f ", b_0);
				System.out.format("%.2f ", b_1);
				System.out.format("%.2f%n", tmp2 / years.size());
			}
		}

		// FLAG 900
		if (option == 9) {
			double b_0 = 0.0;
			double b_1 = 0.0;
			double sum = 0.0;
			double std = 0.0;

			Random rand = new Random();
			for (int i = 0; i < years.size(); i++) {
				sum += Math.pow(years.get(i) - yearsMean, 2);
			}
			std = Math.sqrt(sum / (years.size() - 1));
			for (int i = 0; i < arg2; i++) {
				double tmp = 0.0;
				double tmp1 = 0.0;
				double tmp2 = 0.0;
				int randNum = rand.nextInt(years.size() - 1) + 1;

				for (int j = 0; j < years.size(); j++) {
					tmp += b_0 + b_1 * ((years.get(randNum) - yearsMean) / std) - ice.get(randNum);
					tmp1 += (b_0 + b_1 * ((years.get(randNum) - yearsMean) / std) - ice.get(randNum))
							* (years.get(randNum) - yearsMean) / std;
				}

				b_0 -= arg1 * 2 * tmp;
				b_1 -= arg1 * 2 * tmp1;

				for (int j = 0; j < years.size(); j++) {
					tmp2 += Math.pow((b_0 + b_1 * ((years.get(j) - yearsMean) / std) - ice.get(j)), 2);
				}

				System.out.print(i + 1 + " ");
				System.out.format("%.2f ", b_0);
				System.out.format("%.2f ", b_1);
				System.out.format("%.2f%n", tmp2 / years.size());
			}
		}
	}
}
