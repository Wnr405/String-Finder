public class StringFinder {
	
	public Secret getGoal() {
		return goal;
	}

	public void setGoal(Secret goal) {
		this.goal = goal;
	}

	private Secret goal;
	private static final String CHAR_SET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !@#$%^&*()-_=+[]{}|;:'\",.<>/?";
	private static final int POPULATION_SIZE = 100;
	private static final double MUTATION_RATE = 0.1;
	private static final int TOURNAMENT_SIZE = 5;
	private static final int LOG_FREQUENCY = 10; // Log only every 10th evaluation
	
	private String bestCandidate = "";
	private int bestScore = Integer.MAX_VALUE;
	private String[] population;
	private int currentGeneration = 0;
	
	// For recording evaluation results
	private int evaluationCount = 0;
	private java.util.ArrayList<String> evaluationLog = new java.util.ArrayList<>();
	
	public StringFinder(Secret goal){
		this.goal = goal;
		// Initialize the population with random strings
		initializePopulation();
		// Add header to log
		evaluationLog.add("pass_no,evaluate");
	}
	
	/**
	 * Initialize the population with random strings
	 */
	private void initializePopulation() {
		population = new String[POPULATION_SIZE];
		
		// Test the length of the target string
		String testStr = "";
		int prevScore = goal.evaluate(testStr);
		int length = 0;
		
		// Find the length of the target string by adding one character at a time
		// until the evaluation score stops decreasing
		while (true) {
			testStr += "A";
			int score = goal.evaluate(testStr);
			logEvaluation(score);
			
			if (score < prevScore) {
				length++;
				prevScore = score;
			} else {
				break;
			}
		}
		
		// Create initial population
		for (int i = 0; i < POPULATION_SIZE; i++) {
			population[i] = generateRandomString(length);
		}
	}
	
	/**
	 * Generate a random string of the specified length
	 */
	private String generateRandomString(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int randomIndex = (int) (Math.random() * CHAR_SET.length());
			sb.append(CHAR_SET.charAt(randomIndex));
		}
		return sb.toString();
	}
	
	/**
	 * Log the evaluation score
	 */
	private void logEvaluation(int score) {
		evaluationCount++;
		
		// Only log every LOG_FREQUENCY evaluations to save memory
		// Always log when score improves
		if (evaluationCount % LOG_FREQUENCY == 0 || score < bestScore) {
			evaluationLog.add(evaluationCount + "," + score);
		}
	}
	
	/**
	 * Main method to find the target string
	 * Uses a genetic algorithm approach to efficiently search for the string
	 */
	public String find(){
		// If we've already found the correct answer, return it
		if (bestScore == 0) {
			return bestCandidate;
		}
		
		// Evaluate all members of the population
		int[] scores = evaluatePopulation();
		
		// Create a new generation
		evolvePopulation(scores);
		
		currentGeneration++;
		
		return bestCandidate;
	}
	
	/**
	 * Evaluate all members of the population and keep track of the best one
	 */
	private int[] evaluatePopulation() {
		int[] scores = new int[POPULATION_SIZE];
		
		for (int i = 0; i < POPULATION_SIZE; i++) {
			int score = goal.evaluate(population[i]);
			scores[i] = score;
			
			// Update the best candidate if we found a better one
			if (score < bestScore) {
				bestScore = score;
				bestCandidate = population[i];
				// Log when we find a better score
				logEvaluation(score);
			} else if (i % 10 == 0) { // Log only some evaluations to save memory
				logEvaluation(score);
			}
		}
		
		return scores;
	}
	
	/**
	 * Create a new generation using tournament selection and mutation
	 */
	private void evolvePopulation(int[] scores) {
		String[] newPopulation = new String[POPULATION_SIZE];
		
		// Keep the best member (Elitism)
		int bestIndex = findBestIndex(scores);
		newPopulation[0] = population[bestIndex];
		
		// Create new population
		for (int i = 1; i < POPULATION_SIZE; i++) {
			// Select parents
			String parent1 = tournamentSelection(scores);
			String parent2 = tournamentSelection(scores);
			
			// Crossover
			String child = crossover(parent1, parent2);
			
			// Mutation
			child = mutate(child);
			
			newPopulation[i] = child;
		}
		
		// Replace old population with new population
		population = newPopulation;
	}
	
	/**
	 * Find the index of the member with the best score (lowest score)
	 */
	private int findBestIndex(int[] scores) {
		int bestIdx = 0;
		for (int i = 1; i < scores.length; i++) {
			if (scores[i] < scores[bestIdx]) {
				bestIdx = i;
			}
		}
		return bestIdx;
	}
	
	/**
	 * Select a member using tournament selection
	 */
	private String tournamentSelection(int[] scores) {
		int bestIdx = (int) (Math.random() * POPULATION_SIZE);
		
		for (int i = 0; i < TOURNAMENT_SIZE; i++) {
			int randomIdx = (int) (Math.random() * POPULATION_SIZE);
			if (scores[randomIdx] < scores[bestIdx]) {
				bestIdx = randomIdx;
			}
		}
		
		return population[bestIdx];
	}
	
	/**
	 * Perform crossover between two parent strings
	 */
	private String crossover(String parent1, String parent2) {
		int crossoverPoint = (int) (Math.random() * parent1.length());
		
		return parent1.substring(0, crossoverPoint) + parent2.substring(crossoverPoint);
	}
	
	/**
	 * Mutate a string by randomly changing characters
	 */
	private String mutate(String str) {
		StringBuilder result = new StringBuilder(str);
		
		for (int i = 0; i < str.length(); i++) {
			if (Math.random() < MUTATION_RATE) {
				int randomCharIdx = (int) (Math.random() * CHAR_SET.length());
				result.setCharAt(i, CHAR_SET.charAt(randomCharIdx));
			}
		}
		
		return result.toString();
	}
	
	/**
	 * Return all evaluation data for logging to file
	 */
	public String getEvaluationLog() {
		StringBuilder sb = new StringBuilder();
		for (String line : evaluationLog) {
			sb.append(line).append("\n");
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		
		// DO NOT MODIFY THIS LINE
		StringFinder finder = new StringFinder(new Secret("Parallel Programming"));
		
		///-----------------------------
		// MODIFY/ADD YOUR CODE HERE
		//
		
		String str = finder.find();
		while(finder.goal.evaluate(str) != 0){
			str = finder.find();
		}
		
		// Record the time taken
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		
		// Save evaluation results to file
		try {
			// Create result directory if it doesn't exist
			java.io.File resultDir = new java.io.File("./result");
			if (!resultDir.exists()) {
				resultDir.mkdir();
			}
			
			// Find the current run number
			int runNumber = 1;
			java.io.File timeFile = new java.io.File("./result/time.txt");
			if (timeFile.exists()) {
				java.util.Scanner scanner = new java.util.Scanner(timeFile);
				int lineCount = 0;
				while (scanner.hasNextLine()) {
					scanner.nextLine();
					lineCount++;
				}
				scanner.close();
				runNumber = lineCount + 1;
			}
			
			// Save evaluation results
			String runNumberStr = String.format("%02d", runNumber);
			java.io.FileWriter evalWriter = new java.io.FileWriter("./result/evaluate_" + runNumberStr + ".csv");
			evalWriter.write(finder.getEvaluationLog());
			evalWriter.close();
			
			// Save time information
			java.io.FileWriter timeWriter = new java.io.FileWriter("./result/time.txt", true);
			timeWriter.write("Run " + runNumberStr + ": " + duration + " ns\n");
			timeWriter.close();
			
			System.out.println("Results saved to evaluate_" + runNumberStr + ".csv");
			System.out.println("Time information saved to time.txt");
			
		} catch (java.io.IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
		}
		
		//
		// End of your modification
		//-----------------------------

		// print out the message find so far
		System.out.println("The secret message is: "+str);
		
	}

}

// DO NOT MODIFY Secret CLASS
class Secret{
	private String data;
	
	public Secret(String data){
		this.data = data;
	}
	
	/**
	 *  compare an input string to the goal.
	 * @param input
	 * @return the number of incorrect characters. It returns 0 when the input string exactly matches the data string.
	 */
	public int evaluate(String input){
		int nchargoal = data.length();
		int nchar = input.length();
		int incorrect = 0;
		
	    for (int i = 0; i < input.length(); i++) {
	    	if(i < nchargoal){
		        if (input.charAt(i) != data.charAt(i)) 
		        	incorrect++;
	    	}else
	    		incorrect++;
	    }
	    
	    if(nchargoal > nchar)
	    	incorrect += nchargoal-nchar;
		
		return incorrect;
	}
}
