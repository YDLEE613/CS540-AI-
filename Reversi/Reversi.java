import java.util.*;

class State {
	char[] board;

	int score;
	boolean is_terminal;
	State[] successors;

	static String firstSucc;

	public State(char[] arr) {
		this.board = Arrays.copyOf(arr, arr.length);
	}

	public int getScore() {
		State origin = new State(this.board);

		int num1 = 0;
		int num2 = 0;

		for (int i = 0; i < origin.board.length; i++) {
			if (origin.board[i] == '1') {
				num1++;
			} else if (origin.board[i] == '2') {
				num2++;
			}
		}
		if (num2 < num1) {
			// 1 if dark (1) wins,
			score = 1;
		} else if (num1 == num2) {
			//0 if same,
			score = 0;
		} else {
			// -1 if light (2) wins
			score = -1;
		}
		return score;
	}

	public boolean isTerminal() {

		char player = '1';
		char enemy = '2';

		boolean terminal = false;

		State[] playerSucc = this.getSuccessors(player);
		State[] enemySucc = this.getSuccessors(enemy);

		if (enemySucc.length == 0 && playerSucc.length == 0) {
			terminal = true;
		}

		return terminal;
	}

	public State[] getSuccessors(char player) {

		// get all successors and return them in proper order
		State start = new State(this.board);

		// array list for successors
		ArrayList<State> succ = new ArrayList<>();

		// array list for possible position
		ArrayList<Integer> possIndex = new ArrayList<>();

		// player position
		ArrayList<Integer> playerIndex = new ArrayList<>();

		// enemy position
		ArrayList<Integer> enemyIndex = new ArrayList<>();

		for (int i = 0; i < start.board.length; i++) {
			if (start.board[i] == player) {
				playerIndex.add(i);
			}
			if (start.board[i] != '0' && start.board[i] != player) {
				enemyIndex.add(i);
			}
		}

		possIndex = getPossiblePos(start, playerIndex, player);

		Collections.sort(possIndex);

		for (int i = 0; i < this.board.length; i++) {

			if (possIndex.size() > 0) {
				// add successors for possible position
				for (int j = 0; j < possIndex.size(); j++) {
					char[] tempBoard = this.board.clone();

					if (i == possIndex.get(j)) {
						// put the player in one of possible position in order
						tempBoard[i] = player;

						// add to playerPos
						playerIndex.add(i);

						// new state with new positions
						State tempState = new State(tempBoard);

						// replace enemy with player
						tempState = enemyBetweenPlayer(possIndex.get(j), player, tempState);

						// add to temp
						if (succ.size() == 0) {
							succ.add(tempState);
						} else {
							boolean isSucc = true;
							for (int m = 0; m < succ.size(); m++) {
								if (succ.get(m).equals(tempState)) {
									isSucc = false;
								}
							}
							if (isSucc) {
								succ.add(tempState);
							}
						}
					}
				}
			}
		}

		this.successors = new State[succ.size()];
		for (int i = 0; i < successors.length; i++) {
			this.successors[i] = succ.get(i);
		}

		return this.successors;
	}

	public void printState(int option, char player) {
		State origin = new State(this.board);

		if (option == 1) {
			State[] playerSucc = getSuccessors(player);

			if (playerSucc.length == 0 && !origin.isTerminal()) {
				System.out.println(origin.getBoard());
			} else {
				for (int i = 0; i < playerSucc.length; i++) {
					System.out.println(playerSucc[i].getBoard());
				}
			}
		}
		if (option == 2) {
			if (origin.isTerminal()) {
				System.out.println(this.getScore());
			} else {
				System.out.println("non-terminal");
			}
		}

		if (option == 3) {
			System.out.println(Minimax.run(origin, player));
			System.out.println(Minimax.Max_Min_Val);

		}
		if (option == 4) {
			Minimax.run(origin, player);
			if (State.firstSucc != null) {
				System.out.println(State.firstSucc);
			}
		}
		if (option == 5) {
			System.out.println(Minimax.run_with_pruning(origin, player));
			System.out.println(Minimax.Max_Min_Val);
		}
		if (option == 6) {
			Minimax.run_with_pruning(origin, player);
			if (State.firstSucc != null) {
				System.out.println(State.firstSucc);
			}
		}
	}

	public String getBoard() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 16; i++) {
			builder.append(this.board[i]);
		}
		return builder.toString().trim();
	}

	public boolean equals(State src) {
		for (int i = 0; i < 16; i++) {
			if (this.board[i] != src.board[i])
				return false;
		}
		return true;
	}

	public ArrayList<Integer> getPossiblePos(State start, ArrayList<Integer> playerPos, char player) {
		ArrayList<Integer> possIndex = new ArrayList<>();
		char opponent;
		char empty = '0';
		if (player == '1') {
			opponent = '2';
		} else {
			opponent = '1';
		}

		for (int i = 0; i < playerPos.size(); i++) {
			int pos = playerPos.get(i);

			// if player is at column 1 or 4
			if (pos % 4 == 0 || (pos + 1) % 4 == 0) {
				// first column
				if (pos % 4 == 0) {
					// if there is one enemy (left & right)
					if (start.board[pos + 1] == opponent && start.board[pos + 2] == empty) {
						if (!contains(possIndex, pos + 2)) {
							possIndex.add(pos + 2);
						}
					}
					// if there are two enemies (left & right)
					if (start.board[pos + 1] == opponent && start.board[pos + 2] == opponent
							&& start.board[pos + 3] == empty) {
						if (!contains(possIndex, pos + 3)) {
							possIndex.add(pos + 3);
						}
					}

					// at first row
					if (pos / 4 == 0) {
						// if there is one enemy (up & down)
						if (start.board[pos + 4] == opponent && start.board[pos + 8] == empty) {
							if (!contains(possIndex, pos + 8)) {
								possIndex.add(pos + 8);
							}
						}
						// if there are two enemies (up & down)
						if (start.board[pos + 4] == opponent && start.board[pos + 8] == opponent
								&& start.board[pos + 12] == empty) {
							if (!contains(possIndex, pos + 12)) {
								possIndex.add(pos + 12);
							}
						}
						// if there are one enemy (diagonal)
						if (start.board[pos + 5] == opponent && start.board[pos + 10] == empty) {
							if (!contains(possIndex, pos + 10)) {
								possIndex.add(pos + 10);
							}
						}
						// if there are one enemies (diagonal)
						if (start.board[pos + 5] == opponent && start.board[pos + 10] == opponent
								&& start.board[pos + 15] == empty) {
							if (!contains(possIndex, pos + 15)) {
								possIndex.add(pos + 15);
							}
						}
					}
					// at second row
					if (pos / 4 == 1) {
						// if there is one enemy (up & down)
						if (start.board[pos + 4] == opponent && start.board[pos + 8] == empty) {
							if (!contains(possIndex, pos + 8)) {
								possIndex.add(pos + 8);
							}
						}
						// if there are one enemy (diagonal)
						if (start.board[pos + 5] == opponent && start.board[pos + 10] == empty) {
							if (!contains(possIndex, pos + 10)) {
								possIndex.add(pos + 10);
							}
						}
					}

					// at third row
					if (pos / 4 == 2) {
						// if there is one enemy (up & down)
						if (start.board[pos - 4] == opponent && start.board[pos - 8] == empty) {
							if (!contains(possIndex, pos - 8)) {
								possIndex.add(pos - 8);
							}
						}
						// if there are one enemy (diagonal)
						if (start.board[pos - 3] == opponent && start.board[pos - 6] == empty) {
							if (!contains(possIndex, pos - 6)) {
								possIndex.add(pos - 6);
							}
						}
					}
					// at fourth row
					if (pos / 4 == 3) {
						// if there is one enemy (up & down)
						if (start.board[pos - 4] == opponent && start.board[pos - 8] == empty) {
							if (!contains(possIndex, pos - 8)) {
								possIndex.add(pos - 8);
							}
						}
						// if there are two enemies (up & down)
						if (start.board[pos - 4] == opponent && start.board[pos - 8] == opponent
								&& start.board[pos - 12] == empty) {
							if (!contains(possIndex, pos - 12)) {
								possIndex.add(pos - 12);
							}
						}
						// if there are one enemy (diagonal)
						if (start.board[pos - 3] == opponent && start.board[pos - 6] == empty) {
							if (!contains(possIndex, pos - 6)) {
								possIndex.add(pos - 6);
							}
						}
						// if there are two enemies(diagonal)
						if (start.board[pos - 3] == opponent && start.board[pos - 6] == opponent
								&& start.board[pos - 9] == empty) {
							if (!contains(possIndex, pos - 9)) {
								possIndex.add(pos - 9);
							}
						}
					}

				} else {
					// column 4
					// if there is one enemy (left & right)
					if (start.board[pos - 1] == opponent && start.board[pos - 2] == empty) {
						if (!contains(possIndex, pos - 2)) {
							possIndex.add(pos - 2);
						}
					}
					// if there are two enemies (left & right)
					if (start.board[pos - 1] == opponent && start.board[pos - 2] == opponent
							&& start.board[pos - 3] == empty) {
						if (!contains(possIndex, pos - 3)) {
							possIndex.add(pos - 3);
						}
					}
					// at first row
					if (pos / 4 == 0) {
						// if there is one enemy (up & down)
						if (start.board[pos + 4] == opponent && start.board[pos + 8] == empty) {
							if (!contains(possIndex, pos + 8)) {
								possIndex.add(pos + 8);
							}
						}
						// if there are two enemies (up & down)
						if (start.board[pos + 4] == opponent && start.board[pos + 8] == opponent
								&& start.board[pos + 12] == empty) {
							if (!contains(possIndex, pos + 12)) {
								possIndex.add(pos + 12);
							}
						}
						// if there are one enemy (diagonal)
						if (start.board[pos + 3] == opponent && start.board[pos + 6] == empty) {
							if (!contains(possIndex, pos + 6)) {
								possIndex.add(pos + 6);
							}
						}
						// if there are one enemies (diagonal)
						if (start.board[pos + 3] == opponent && start.board[pos + 6] == opponent
								&& start.board[pos + 9] == empty) {
							if (!contains(possIndex, pos + 9)) {
								possIndex.add(pos + 9);
							}
						}
					}
					// at second row
					if (pos / 4 == 1) {
						// if there is one enemy (up & down)
						if (start.board[pos + 4] == opponent && start.board[pos + 8] == empty) {
							if (!contains(possIndex, pos + 8)) {
								possIndex.add(pos + 8);
							}
						}
						// if there are one enemy (diagonal)
						if (start.board[pos + 3] == opponent && start.board[pos + 6] == empty) {
							if (!contains(possIndex, pos + 6)) {
								possIndex.add(pos + 6);
							}
						}
					}
					// at third row
					if (pos / 4 == 2) {
						// if there is one enemy (up & down)
						if (start.board[pos - 4] == opponent && start.board[pos - 8] == empty) {
							if (!contains(possIndex, pos - 8)) {
								possIndex.add(pos - 8);
							}
						}
						// if there are one enemy (diagonal)
						if (start.board[pos - 5] == opponent && start.board[pos - 10] == empty) {
							if (!contains(possIndex, pos - 10)) {
								possIndex.add(pos - 10);
							}
						}
					}
					// at fourth row
					if (pos / 4 == 3) {
						// if there is one enemy (up & down)
						if (start.board[pos - 4] == opponent && start.board[pos - 8] == empty) {
							if (!contains(possIndex, pos - 8)) {
								possIndex.add(pos - 8);
							}
						}
						// if there are two enemies (up & down)
						if (start.board[pos - 4] == opponent && start.board[pos - 8] == opponent
								&& start.board[pos - 12] == empty) {
							if (!contains(possIndex, pos - 12)) {
								possIndex.add(pos - 12);
							}
						}
						// if there are one enemy (diagonal)
						if (start.board[pos - 5] == opponent && start.board[pos - 10] == empty) {
							if (!contains(possIndex, pos - 10)) {
								possIndex.add(pos - 10);
							}
						}
						// if there are two enemies(diagonal)
						if (start.board[pos - 5] == opponent && start.board[pos - 10] == opponent
								&& start.board[pos - 15] == empty) {
							if (!contains(possIndex, pos - 15)) {
								possIndex.add(pos - 15);
							}
						}
					}

				}
			} else {
				// column 2
				if (pos % 4 == 1) {
					// if there is one enemy (left & right)
					if (start.board[pos + 1] == opponent && start.board[pos + 2] == empty) {
						if (!contains(possIndex, pos + 2)) {
							possIndex.add(pos + 2);
						}
					}

					// at first row
					if (pos / 4 == 0) {
						// if there is one enemy (up & down)
						if (start.board[pos + 4] == opponent && start.board[pos + 8] == empty) {
							if (!contains(possIndex, pos + 8)) {
								possIndex.add(pos + 8);
							}
						}
						// if there are two enemies (up & down)
						if (start.board[pos + 4] == opponent && start.board[pos + 8] == opponent
								&& start.board[pos + 12] == empty) {
							if (!contains(possIndex, pos + 12)) {
								possIndex.add(pos + 12);
							}
						}
						// if there are one enemy (diagonal)
						if (start.board[pos + 5] == opponent && start.board[pos + 10] == empty) {
							if (!contains(possIndex, pos + 10)) {
								possIndex.add(pos + 10);
							}
						}
					}
					// at second row
					if (pos / 4 == 1) {
						// if there is one enemy (up & down)
						if (start.board[pos + 4] == opponent && start.board[pos + 8] == empty) {
							if (!contains(possIndex, pos + 8)) {
								possIndex.add(pos + 8);
							}
						}
						// if there are one enemy (diagonal)
						if (start.board[pos + 5] == opponent && start.board[pos + 10] == empty) {
							if (!contains(possIndex, pos + 10)) {
								possIndex.add(pos + 10);
							}
						}
					}

					// at third row
					if (pos / 4 == 2) {
						// if there is one enemy (up & down)
						if (start.board[pos - 4] == opponent && start.board[pos - 8] == empty) {
							if (!contains(possIndex, pos - 8)) {
								possIndex.add(pos - 8);
							}
						}
						// if there are one enemy (diagonal)
						if (start.board[pos - 3] == opponent && start.board[pos - 6] == empty) {
							if (!contains(possIndex, pos - 6)) {
								possIndex.add(pos - 6);
							}
						}
					}
					// at fourth row
					if (pos / 4 == 3) {
						// if there is one enemy (up & down)
						if (start.board[pos - 4] == opponent && start.board[pos - 8] == empty) {
							if (!contains(possIndex, pos - 8)) {
								possIndex.add(pos - 8);
							}
						}
						// if there are two enemies (up & down)
						if (start.board[pos - 4] == opponent && start.board[pos - 8] == opponent
								&& start.board[pos - 12] == empty) {
							if (!contains(possIndex, pos - 12)) {
								possIndex.add(pos - 12);
							}
						}
						// if there are one enemy (diagonal)
						if (start.board[pos - 3] == opponent && start.board[pos - 6] == empty) {
							if (!contains(possIndex, pos - 6)) {
								possIndex.add(pos - 6);
							}
						}
					}
				} else {
					// column 3
					// if there is one enemy (left & right)
					if (start.board[pos - 1] == opponent && start.board[pos - 2] == empty) {
						if (!contains(possIndex, pos - 2)) {
							possIndex.add(pos - 2);
						}
					}

					// at first row
					if (pos / 4 == 0) {
						// if there is one enemy (up & down)
						if (start.board[pos + 4] == opponent && start.board[pos + 8] == empty) {
							if (!contains(possIndex, pos + 8)) {
								possIndex.add(pos + 8);
							}
						}
						// if there are two enemies (up & down)
						if (start.board[pos + 4] == opponent && start.board[pos + 8] == opponent
								&& start.board[pos + 12] == empty) {
							if (!contains(possIndex, pos + 12)) {
								possIndex.add(pos + 12);
							}
						}
						// if there are one enemy (diagonal)
						if (start.board[pos + 3] == opponent && start.board[pos + 6] == empty) {
							if (!contains(possIndex, pos + 6)) {
								possIndex.add(pos + 6);
							}
						}
					}
					// at second row
					if (pos / 4 == 1) {
						// if there is one enemy (up & down)
						if (start.board[pos + 4] == opponent && start.board[pos + 8] == empty) {
							if (!contains(possIndex, pos + 8)) {
								possIndex.add(pos + 8);
							}
						}
						// if there are one enemy (diagonal)
						if (start.board[pos + 3] == opponent && start.board[pos + 6] == empty) {
							if (!contains(possIndex, pos + 6)) {
								possIndex.add(pos + 6);
							}
						}
					}

					// at third row
					if (pos / 4 == 2) {
						// if there is one enemy (up & down)
						if (start.board[pos - 4] == opponent && start.board[pos - 8] == empty) {
							if (!contains(possIndex, pos - 8)) {
								possIndex.add(pos - 8);
							}
						}
						// if there are one enemy (diagonal)
						if (start.board[pos - 5] == opponent && start.board[pos - 10] == empty) {
							if (!contains(possIndex, pos - 10)) {
								possIndex.add(pos - 10);
							}
						}
					}
					// at fourth row
					if (pos / 4 == 3) {
						// if there is one enemy (up & down)
						if (start.board[pos - 4] == opponent && start.board[pos - 8] == empty) {
							if (!contains(possIndex, pos - 8)) {
								possIndex.add(pos - 8);
							}
						}
						// if there are two enemies (up & down)
						if (start.board[pos - 4] == opponent && start.board[pos - 8] == opponent
								&& start.board[pos - 12] == empty) {
							if (!contains(possIndex, pos - 12)) {
								possIndex.add(pos - 12);
							}
						}
						// if there are one enemy (diagonal)
						if (start.board[pos - 5] == opponent && start.board[pos - 10] == empty) {
							if (!contains(possIndex, pos - 10)) {
								possIndex.add(pos - 10);
							}
						}
					}
				}
			}
		}
		return possIndex;
	}

	public State enemyBetweenPlayer(int playerIndex, char player, State newState) {

		State tempState = newState;

		char opponent;
		if (player == '1') {
			opponent = '2';
		} else {
			opponent = '1';
		}
		// if player puts at the far left or right column
		if (playerIndex % 4 == 0 || (playerIndex + 1) % 4 == 0) {

			// if player puts in the first row
			if (playerIndex / 4 == 0) {
				// if there is just one enemy (up & down)
				if (tempState.board[playerIndex + 4] == opponent && tempState.board[playerIndex + 8] == player) {
					tempState.board[playerIndex + 4] = player;
				}
				// if there are two enemies (up & down)
				if (tempState.board[playerIndex + 4] == opponent && tempState.board[playerIndex + 8] == opponent
						&& tempState.board[playerIndex + 12] == player) {
					tempState.board[playerIndex + 4] = player;
					tempState.board[playerIndex + 8] = player;
				}

				// if player puts at index 0 (diagonal)
				if (playerIndex % 4 == 0) {
					// if there is just one enemy (left & right)
					if (tempState.board[playerIndex + 1] == opponent && tempState.board[playerIndex + 2] == player) {
						tempState.board[playerIndex + 1] = player;
					}
					// if there are two enemies (left & right)
					if (tempState.board[playerIndex + 1] == opponent && tempState.board[playerIndex + 2] == opponent
							&& tempState.board[playerIndex + 3] == player) {
						tempState.board[playerIndex + 1] = player;
						tempState.board[playerIndex + 2] = player;
					}

					// one enemy between (diagonal)
					if (tempState.board[playerIndex + 5] == opponent && tempState.board[playerIndex + 10] == player) {
						tempState.board[playerIndex + 5] = player;
					}
					// two enemies between (diagonal)
					if (tempState.board[playerIndex + 5] == opponent && tempState.board[playerIndex + 10] == opponent
							&& tempState.board[playerIndex + 15] == player) {
						tempState.board[playerIndex + 5] = player;
						tempState.board[playerIndex + 10] = player;
					}
				} else {
					// if player puts at index 3
					// if there is just one enemy (left & right)
					if (tempState.board[playerIndex - 1] == opponent && tempState.board[playerIndex - 2] == player) {
						tempState.board[playerIndex - 1] = player;
					}
					// if there are two enemies (left & right)
					if (tempState.board[playerIndex - 1] == opponent && tempState.board[playerIndex - 2] == opponent
							&& tempState.board[playerIndex - 3] == player) {
						tempState.board[playerIndex - 1] = player;
						tempState.board[playerIndex - 2] = player;
					}
					// one enemy between (diagonal)
					if (tempState.board[playerIndex + 3] == opponent && tempState.board[playerIndex + 6] == player) {
						tempState.board[playerIndex + 3] = player;
					}
					// two enemies between (diagonal)
					if (tempState.board[playerIndex + 3] == opponent && tempState.board[playerIndex + 6] == opponent
							&& tempState.board[playerIndex + 9] == player) {
						tempState.board[playerIndex + 3] = player;
						tempState.board[playerIndex + 6] = player;
					}
				}
			}

			// if player puts in the second row
			if (playerIndex / 4 == 1) {
				// if there is one enemy between (up & down)
				if (tempState.board[playerIndex + 4] == opponent && tempState.board[playerIndex + 8] == player) {
					tempState.board[playerIndex + 4] = player;
				}

				// if player puts at index 4
				if (playerIndex % 4 == 0) {
					// if there are one enemy (left & right)
					if (tempState.board[playerIndex + 1] == opponent && tempState.board[playerIndex + 2] == player) {
						tempState.board[playerIndex + 1] = player;
					}
					// if there are two enemies (left & right)
					if (tempState.board[playerIndex + 1] == opponent && tempState.board[playerIndex + 2] == opponent
							&& tempState.board[playerIndex + 3] == player) {
						tempState.board[playerIndex + 1] = player;
						tempState.board[playerIndex + 2] = player;
					}

					// one enemy between (diagonal)
					if (tempState.board[playerIndex + 5] == opponent && tempState.board[playerIndex + 10] == player) {
						tempState.board[playerIndex + 5] = player;
					}
				} else {
					// if player puts at index 7
					// if there are one enemy (left & right)
					if (tempState.board[playerIndex - 1] == opponent && tempState.board[playerIndex - 2] == player) {
						tempState.board[playerIndex - 1] = player;
					}
					// if there are two enemies (left & right)
					if (tempState.board[playerIndex - 1] == opponent && tempState.board[playerIndex - 2] == opponent
							&& tempState.board[playerIndex - 3] == player) {
						tempState.board[playerIndex - 1] = player;
						tempState.board[playerIndex - 2] = player;
					}

					// one enemy between (diagonal)
					if (tempState.board[playerIndex + 3] == opponent && tempState.board[playerIndex + 6] == player) {
						tempState.board[playerIndex + 3] = player;
					}
				}
			}

			// if player puts in the third row
			if (playerIndex / 4 == 2) {
				// if there is one enemy between (up & down)
				if (tempState.board[playerIndex - 4] == opponent && tempState.board[playerIndex - 8] == player) {
					tempState.board[playerIndex - 4] = player;
				}

				// if player puts at index 8
				if (playerIndex % 4 == 0) {
					// if there are one enemy (left & right)
					if (tempState.board[playerIndex + 1] == opponent && tempState.board[playerIndex + 2] == player) {
						tempState.board[playerIndex + 1] = player;
					}
					// if there are two enemies (left & right)
					if (tempState.board[playerIndex + 1] == opponent && tempState.board[playerIndex + 2] == opponent
							&& tempState.board[playerIndex + 3] == player) {
						tempState.board[playerIndex + 1] = player;
						tempState.board[playerIndex + 2] = player;
					}

					// one enemy between (diagonal)
					if (tempState.board[playerIndex - 3] == opponent && tempState.board[playerIndex - 6] == player) {
						tempState.board[playerIndex - 3] = player;
					}
				} else {
					// if player puts at index 11
					// if there are one enemy (left & right)
					if (tempState.board[playerIndex - 1] == opponent && tempState.board[playerIndex - 2] == player) {
						tempState.board[playerIndex - 1] = player;
					}
					// if there are two enemies (left & right)
					if (tempState.board[playerIndex - 1] == opponent && tempState.board[playerIndex - 2] == opponent
							&& tempState.board[playerIndex - 3] == player) {
						tempState.board[playerIndex - 1] = player;
						tempState.board[playerIndex - 2] = player;
					}

					// one enemy between (diagonal)
					if (tempState.board[playerIndex - 5] == opponent && tempState.board[playerIndex - 10] == player) {
						tempState.board[playerIndex - 5] = player;
					}
				}
			}
			// if player puts in the fourth row
			if (playerIndex / 4 == 3) {
				// if there is just one enemy (up & down)
				if (tempState.board[playerIndex - 4] == opponent && tempState.board[playerIndex - 8] == player) {
					tempState.board[playerIndex - 4] = player;
				}
				// if there are two enemies (up & down)
				if (tempState.board[playerIndex - 4] == opponent && tempState.board[playerIndex - 8] == opponent
						&& tempState.board[playerIndex - 12] == player) {
					tempState.board[playerIndex - 4] = player;
					tempState.board[playerIndex - 8] = player;
				}

				// if player puts at index 12
				if (playerIndex % 4 == 0) {
					// if there is just one enemy (left & right)
					if (tempState.board[playerIndex + 1] == opponent && tempState.board[playerIndex + 2] == player) {
						tempState.board[playerIndex + 1] = player;
					}
					// if there are two enemies (left & right)
					if (tempState.board[playerIndex + 1] == opponent && tempState.board[playerIndex + 2] == opponent
							&& tempState.board[playerIndex + 3] == player) {
						tempState.board[playerIndex + 1] = player;
						tempState.board[playerIndex + 2] = player;
					}

					// one enemy between (diagonal)
					if (tempState.board[playerIndex - 3] == opponent && tempState.board[playerIndex - 6] == player) {
						tempState.board[playerIndex - 3] = player;
					}
					// two enemies between (diagonal)
					if (tempState.board[playerIndex - 3] == opponent && tempState.board[playerIndex - 6] == opponent
							&& tempState.board[playerIndex - 9] == player) {
						tempState.board[playerIndex - 3] = player;
						tempState.board[playerIndex - 6] = player;
					}
				} else {
					// if player puts at index 15
					// if there is just one enemy (left & right)
					if (tempState.board[playerIndex - 1] == opponent && tempState.board[playerIndex - 2] == player) {
						tempState.board[playerIndex - 1] = player;
					}
					// if there are two enemies (left & right)
					if (tempState.board[playerIndex - 1] == opponent && tempState.board[playerIndex - 2] == opponent
							&& tempState.board[playerIndex - 3] == player) {
						tempState.board[playerIndex - 1] = player;
						tempState.board[playerIndex - 2] = player;
					}
					// one enemy between (diagonal)
					if (tempState.board[playerIndex - 5] == opponent && tempState.board[playerIndex - 10] == player) {
						tempState.board[playerIndex - 5] = player;
					}
					// two enemies between (diagonal)
					if (tempState.board[playerIndex - 5] == opponent && tempState.board[playerIndex - 10] == opponent
							&& tempState.board[playerIndex - 15] == player) {
						// System.out.println("enemy is at ");
						tempState.board[playerIndex - 5] = player;
						tempState.board[playerIndex - 10] = player;
					}

				}
			}

		} else {
			// if player puts at column 2 or column 3
			// if player puts in the first row
			if (playerIndex / 4 == 0) {
				// if there is just one enemy (up & down)
				if (tempState.board[playerIndex + 4] == opponent && tempState.board[playerIndex + 8] == player) {
					tempState.board[playerIndex + 4] = player;
				}
				// if there are two enemies (up & down)
				if (tempState.board[playerIndex + 4] == opponent && tempState.board[playerIndex + 8] == opponent
						&& tempState.board[playerIndex + 12] == player) {
					tempState.board[playerIndex + 4] = player;
					tempState.board[playerIndex + 8] = player;
				}

				// if player puts at index 1
				if (playerIndex % 4 == 1) {
					// if there is just one enemy (left & right)
					if (tempState.board[playerIndex + 1] == opponent && tempState.board[playerIndex + 2] == player) {
						tempState.board[playerIndex + 1] = player;
					}

					// one enemy between (diagonal)
					if (tempState.board[playerIndex + 5] == opponent && tempState.board[playerIndex + 10] == player) {
						tempState.board[playerIndex + 5] = player;
					}
				} else {
					// if player puts at index 2
					// if there is just one enemy (left & right)
					if (tempState.board[playerIndex - 1] == opponent && tempState.board[playerIndex - 2] == player) {
						tempState.board[playerIndex - 1] = player;
					}
					// one enemy between (diagonal)
					if (tempState.board[playerIndex + 3] == opponent && tempState.board[playerIndex + 6] == player) {
						tempState.board[playerIndex + 3] = player;
					}
				}
			}

			// if player puts in the second row
			if (playerIndex / 4 == 1) {
				// if there is one enemy between (up & down)
				if (tempState.board[playerIndex + 4] == opponent && tempState.board[playerIndex + 8] == player) {
					tempState.board[playerIndex + 4] = player;
				}

				// if player puts at index 5
				if (playerIndex % 4 == 1) {
					// if there are one enemy (left & right)
					if (tempState.board[playerIndex + 1] == opponent && tempState.board[playerIndex + 2] == player) {
						tempState.board[playerIndex + 1] = player;
					}
					// one enemy between (diagonal)
					if (tempState.board[playerIndex + 5] == opponent && tempState.board[playerIndex + 10] == player) {
						tempState.board[playerIndex + 5] = player;
					}
				} else {
					// if player puts at index 6
					// if there are one enemy (left & right)
					if (tempState.board[playerIndex - 1] == opponent && tempState.board[playerIndex - 2] == player) {
						tempState.board[playerIndex - 1] = player;
					}
					// one enemy between (diagonal)
					if (tempState.board[playerIndex + 3] == opponent && tempState.board[playerIndex + 6] == player) {
						tempState.board[playerIndex + 3] = player;
					}
				}
			}

			// if player puts in the third row
			if (playerIndex / 4 == 2) {
				// if there is one enemy between (up & down)
				if (tempState.board[playerIndex - 4] == opponent && tempState.board[playerIndex - 8] == player) {
					tempState.board[playerIndex - 4] = player;
				}

				// if player puts at index 9
				if (playerIndex % 4 == 1) {
					// if there are one enemy (left & right)
					if (tempState.board[playerIndex + 1] == opponent && tempState.board[playerIndex + 2] == player) {
						tempState.board[playerIndex + 1] = player;
					}
					// one enemy between (diagonal)
					if (tempState.board[playerIndex - 3] == opponent && tempState.board[playerIndex - 6] == player) {
						tempState.board[playerIndex - 3] = player;
					}
				} else {
					// if player puts at index 10
					// if there are one enemy (left & right)
					if (tempState.board[playerIndex - 1] == opponent && tempState.board[playerIndex - 2] == player) {
						tempState.board[playerIndex - 1] = player;
					}
					// one enemy between (diagonal)
					if (tempState.board[playerIndex - 5] == opponent && tempState.board[playerIndex - 10] == player) {
						tempState.board[playerIndex - 5] = player;
					}
				}
			}
			// if player puts in the fourth row
			if (playerIndex / 4 == 3) {
				// if there is just one enemy (up & down)
				if (tempState.board[playerIndex - 4] == opponent && tempState.board[playerIndex - 8] == player) {
					tempState.board[playerIndex - 4] = player;
				}
				// if there are two enemies (up & down)
				if (tempState.board[playerIndex - 4] == opponent && tempState.board[playerIndex - 8] == opponent
						&& tempState.board[playerIndex - 12] == player) {
					tempState.board[playerIndex - 4] = player;
					tempState.board[playerIndex - 8] = player;
				}

				// if player puts at index 13
				if (playerIndex % 4 == 1) {
					// if there is just one enemy (left & right)
					if (tempState.board[playerIndex + 1] == opponent && tempState.board[playerIndex + 2] == player) {
						tempState.board[playerIndex + 1] = player;
					}

					// one enemy between (diagonal)
					if (tempState.board[playerIndex - 3] == opponent && tempState.board[playerIndex - 6] == player) {
						tempState.board[playerIndex - 3] = player;
					}
				} else {
					// if player puts at index 14
					// if there is just one enemy (left & right)
					if (tempState.board[playerIndex - 1] == opponent && tempState.board[playerIndex - 2] == player) {
						tempState.board[playerIndex - 1] = player;
					}
					// one enemy between (diagonal)
					if (tempState.board[playerIndex - 5] == opponent && tempState.board[playerIndex - 10] == player) {
						tempState.board[playerIndex - 5] = player;
					}
				}
			}
		}
		return tempState;

	}

	public boolean contains(ArrayList<Integer> empty, int pos) {
		for (int i = 0; i < empty.size(); i++) {
			if (empty.get(i) == pos) {
				return true;
			}
		}
		return false;
	}

	
}

class Minimax {
	public static int Max_Min_Val = 0;
	public static int sumMaxSucc = 0;
	public static int sumMinSucc = 0;

	private static int max_value(State curr_state) {
		Max_Min_Val++;
		char player = '1';

		int val = Max_Min_Val;

		int alpha;

		if (curr_state.isTerminal()) {
			return curr_state.getScore();

		} else {
			alpha = Integer.MIN_VALUE;
			int aBest = Integer.MIN_VALUE; 

			State[] succ = curr_state.getSuccessors(player);

			sumMaxSucc += succ.length;

			if (succ.length > 0) {
				for (int i = 0; i < succ.length; i++) {
					alpha = Math.max(alpha, min_value(succ[i]));

					if (val == 1 && alpha > aBest) {
						aBest = alpha;
						State.firstSucc = succ[i].getBoard();
					}
				}
			} else {
				if (val == 1) {
					State.firstSucc = curr_state.getBoard();
				}
				return min_value(curr_state);
			}
		}
		return alpha;

	}

	private static int min_value(State curr_state) {
		Max_Min_Val++;

		char player = '2';

		int val = Max_Min_Val;
		int beta;

		if (curr_state.isTerminal()) {
			return curr_state.getScore();
		} else {
			beta = Integer.MAX_VALUE;
			int bBest = Integer.MAX_VALUE;// part D

			State[] succ = curr_state.getSuccessors(player);
			sumMinSucc += succ.length;
			if (succ.length > 0) {
				for (int i = 0; i < succ.length; i++) {

					beta = Math.min(beta, max_value(succ[i]));

					if (val == 1 && beta < bBest) {
						bBest = beta;
						State.firstSucc = succ[i].getBoard();
					}
				}
			} else {
				if (val == 1) {
					State.firstSucc = curr_state.getBoard();
				}
				return max_value(curr_state);
			}

		}
		return beta;

	}

	private static int max_value_with_pruning(State curr_state, int alpha, int beta) {
		Max_Min_Val++;
		char player = '1';

		int val = Max_Min_Val;

		if (curr_state.isTerminal()) {
			return curr_state.getScore();

		} else {
			int aBest = Integer.MIN_VALUE; 

			State[] succ = curr_state.getSuccessors(player);

			sumMaxSucc += succ.length;
			if (succ.length > 0) {
				for (int i = 0; i < succ.length; i++) {
					alpha = Math.max(alpha, min_value_with_pruning(succ[i], alpha, beta));

					if (val == 1 && alpha > aBest) {
						aBest = alpha;
						State.firstSucc = succ[i].getBoard();
					}
					if (alpha >= beta) {
						return beta;
					}
				}
			} else {
				if (val == 1) {
					State.firstSucc = curr_state.getBoard();
				}
				return min_value_with_pruning(curr_state, alpha, beta);
			}
		}
		return alpha;
	}

	private static int min_value_with_pruning(State curr_state, int alpha, int beta) {
		Max_Min_Val++;
		char player = '2';

		int val = Max_Min_Val;

		// if terminal node, return the current state score
		if (curr_state.isTerminal()) {
			return curr_state.getScore();
		} else {
			// if it is not terminal node
			int bBest = Integer.MAX_VALUE;

			State[] succ = curr_state.getSuccessors(player);
			sumMinSucc += succ.length;

			if (succ.length > 0) {
				for (int i = 0; i < succ.length; i++) {

					beta = Math.min(beta, max_value_with_pruning(succ[i], alpha, beta));

					if (val == 1 && beta < bBest) {
						bBest = beta;
						State.firstSucc = succ[i].getBoard();
					}

					if (alpha >= beta) {
						return alpha;
					}
				}
			} else {
				if (val == 1) {
					State.firstSucc = curr_state.getBoard();
				}
				return max_value_with_pruning(curr_state, alpha, beta);
			}
		}
		return beta;
	}

	public static int run(State curr_state, char player) {
		if (player == '1') {
			return max_value(curr_state);
		} else {
			return min_value(curr_state);
		}
	}

	public static int run_with_pruning(State curr_state, char player) {

		if (player == '1') {
			return max_value_with_pruning(curr_state, Integer.MIN_VALUE, Integer.MAX_VALUE);
		} else {
			return min_value_with_pruning(curr_state, Integer.MIN_VALUE, Integer.MAX_VALUE);
		}

	}
}

public class Reversi {
	public static void main(String args[]) {
		String tmp = "100 2 2220221021101010";
		
		String temp[] = tmp.split(" ");
		args = temp;
		
		if (args.length != 3) {
			System.out.println("Invalid Number of Input Arguments");
			return;
		}

		int flag = Integer.valueOf(args[0]);
		char[] board = new char[16];
		for (int i = 0; i < 16; i++) {
			board[i] = args[2].charAt(i);
		}
		int option = flag / 100;
		char player = args[1].charAt(0);
		if ((player != '1' && player != '2') || args[1].length() != 1) {
			System.out.println("Invalid Player Input");
			return;
		}
		State init = new State(board);
		init.printState(option, player);
	}
}