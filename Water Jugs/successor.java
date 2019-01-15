import java.util.*;

public class successor {

	public static class JugState {
		int[] Capacity = new int[] { 0, 0, 0 };
		int[] Content = new int[] { 0, 0, 0 };

		public JugState(JugState copyFrom) {
			this.Capacity[0] = copyFrom.Capacity[0];
			this.Capacity[1] = copyFrom.Capacity[1];
			this.Capacity[2] = copyFrom.Capacity[2];
			this.Content[0] = copyFrom.Content[0];
			this.Content[1] = copyFrom.Content[1];
			this.Content[2] = copyFrom.Content[2];
		}

		public JugState() {
		}

		public JugState(int A, int B, int C) {
			this.Capacity[0] = A;
			this.Capacity[1] = B;
			this.Capacity[2] = C;
		}

		public JugState(int A, int B, int C, int a, int b, int c) {
			this.Capacity[0] = A;
			this.Capacity[1] = B;
			this.Capacity[2] = C;
			this.Content[0] = a;
			this.Content[1] = b;
			this.Content[2] = c;
		}

		public void printContent() {
			System.out.println(this.Content[0] + " " + this.Content[1] + " " + this.Content[2]);
		}

		public ArrayList<JugState> getNextStates() {
			ArrayList<JugState> successors = new ArrayList<>();

			int[] capacity = this.Capacity;
			int[] content = this.Content;

			JugState original = new JugState(capacity[0], capacity[1], capacity[2], content[0], content[1], content[2]);

			// empty each jug
			for (int i = 0; i < original.Capacity.length; i++) {
				JugState temp = new JugState(original);

				if (temp.Content[i] != 0) {

					temp.Content[i] = 0;
					successors.add(temp);
				}
			}

			// fill each jug
			for (int i = 0; i < original.Capacity.length; i++) {
				JugState temp = new JugState(original);

				if (temp.Content[i] != temp.Capacity[i]) {
					temp.Content[i] = temp.Capacity[i];
					successors.add(temp);
				}
			}

			//if not full, move around
			for(int i = 0; i < original.Capacity.length; i++) {
				
				for(int j = 0 ; j < original.Capacity.length; j++) {
					JugState temp2 = new JugState(original);
					
					int sum = 0;
					if(i != j && temp2.Content[i] < temp2.Capacity[i]) {
						sum = temp2.Content[i] + temp2.Content[j];
						if(sum > temp2.Capacity[i]) {
							temp2.Content[i] = temp2.Capacity[i];
							temp2.Content[j] = sum - temp2.Capacity[i];
						}else {
							temp2.Content[i] = sum;
							temp2.Content[j] = 0;
						}
						successors.add(temp2);
					}
				}
			}
			return successors;
		}
	}

	public static void main(String[] args) {
		if (args.length != 6) {
			System.out.println("Usage: java successor [A] [B] [C] [a] [b] [c]");
			return;
		}

		// handle cases when content is larger than capacity
		for (int i = 0; i < args.length; i++) {
			System.out.print(args[i] + " ");
		}
		System.out.println();
		// parse command line arguments
		JugState a = new JugState();
		a.Capacity[0] = Integer.parseInt(args[0]);
		a.Capacity[1] = Integer.parseInt(args[1]);
		a.Capacity[2] = Integer.parseInt(args[2]);
		a.Content[0] = Integer.parseInt(args[3]);
		a.Content[1] = Integer.parseInt(args[4]);
		a.Content[2] = Integer.parseInt(args[5]);

		// Implement this function
		ArrayList<JugState> asist = a.getNextStates();

		// Print out generated successors
		for (int i = 0; i < asist.size(); i++) {
			asist.get(i).printContent();
		}

		return;
	}
}
