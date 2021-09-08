import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class App {
    /*
    The program displays main menu with options to start a quiz or quit
    After choosing to start a quiz the user is presented with categories
    to choose from. After choosing a category the program loads
    questions with answers from a text file and randomly chooses 5 of those
    and then makes a quiz out of them allowing user to choose one of 3
    possible answers. Then it tells the user if the answer
    he or she gave was correct or not. After going through all the questions in a quiz
    the program returns to the main menu. To close the program the user must choose the option
    to quit from the main menu.
    */


    /**
     * The method checks whether text containg question and answers has a correct format
     *
     * @param expression String object containg expression to check
     * @return true if expression has correct format otherwise false
     */
    static boolean isQuestionWithAnswersCorrect(String expression) {
        return expression != null && expression.matches(".+(;.*-(YES|NO)){3}");
    }

    /**
     * The method asks a user for input and gets one line of input from keyboard
     *
     * @param message contains a message that will be showed to ask a user for input
     * @return String object containg a line of input from a user
     */
    static String getExpression(String message) {
        System.out.println(message);
        return new Scanner(System.in).nextLine();
    }

    /**
     * The method prompts you to enter a line containing question and answers
     * As long as the format of the line is incorrect it prompts you to enter it again
     *
     * @param numberOfQuestion number of question to use in prompt message
     * @return String object containing line with question and answers in a correct format
     */
    static String getQuestionWithAnswers(int numberOfQuestion) {
        var expression = "";
        do {
            expression = getExpression("Enter question no. " + numberOfQuestion);
        } while (!isQuestionWithAnswersCorrect(expression));
        return expression;
    }

    /**
     * The method repeatedly gets certain number of lines given by method's parameter from a user
     * each time checking for a validity of a given line and asking again for the same line if it was incorrect
     *
     * @param numberOfItems number of lines to ask a user for
     * @return an array of String objects containing lines with questions and answers in a correct format
     */
    static String[] getQuestionsAndAnswers(int numberOfItems) {
        if (numberOfItems <= 0) {
            throw new IllegalArgumentException("Number of questions is not valid");
        }
        String[] questionsAndAnswers = new String[numberOfItems];
        for (int i = 0; i < numberOfItems; i++) {
            questionsAndAnswers[i] = getQuestionWithAnswers(i + 1);
        }
        return questionsAndAnswers;
    }

    /**
     * The method counts number of lines in a file specified by filename parameter
     *
     * @param filename name of the file
     * @return number of lines in a file
     */
    static int getLineCount(String filename) {
        if (filename == null)
            throw new IllegalArgumentException("Filename is null");
        int count = 0;
        try(Scanner input = new Scanner(new File(filename))) {
            while (input.hasNextLine()) {
                input.nextLine();
                count++;
            }
        }
        catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
        return count;
    }

    /**
     * The method loads questions from a text file specified as parameter
     *
     * @param filename name of the file to read questions from
     * @return an array of String objects containing lines with questions and answers in a correct format
     */
    static String[] getQuestionsAndAnswers(String filename) {
        if (filename == null)
            throw new IllegalArgumentException("Filename is null");

        String[] questionAndAnswers = new String[getLineCount(filename)];
        try(Scanner input = new Scanner(new File(filename))) {
            String line;
            for (int i = 0; input.hasNextLine(); i++) {
                line = input.nextLine();
                if (!isQuestionWithAnswersCorrect(line)) {
                    throw new IllegalStateException("Incorrect line in the file: " + filename + " Line number: " + (i + 1));
                }
                questionAndAnswers[i] = line;
        }
        }
        catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
        return questionAndAnswers;
    }

    /**
     * The method displays a question, all possible answers and asks a user to enter a specified letter
     * a, b or c. Until it won't get one of those letters it will ask again for 1 of those 3 letters.
     * After choosing 1 of 3 possible letters the information about correctness of a given answer is
     * displayed to the user.
     *
     * @param questionsAndAnswers An array containing String objects of specified format
     * @param numberOfQuestion    number of question the method uses to choose a question from an array
     * @return true if the question is answered correctly otherwise false
     */
    static boolean askAQuestion(String[] questionsAndAnswers, int numberOfQuestion) {
        boolean[] correctnessOfAnswers = new boolean[3];
        String[] tokens = questionsAndAnswers[numberOfQuestion].split(";");
        for (int i = 1; i < tokens.length; i++) {
            correctnessOfAnswers[i - 1] = tokens[i].matches(".*-YES");
        }
        System.out.println((numberOfQuestion + 1) + ". " + tokens[0]);
        for (int i = 0; i < 3; i++) {
            System.out.println((char)('a' + i) + ") " + tokens[i + 1].replaceFirst("(-YES|-NO)", ""));
        }
        int index;
        String option;
        do {
            option = getExpression("Write a, b or c and press enter:");
            index = switch (option.toLowerCase()) {
                case "a" -> 0;
                case "b" -> 1;
                case "c" -> 2;
                default -> -1;
            };
        } while (index < 0);
        System.out.println((correctnessOfAnswers[index]) ? "Correct Answer" : "Incorrect Answer");
        return correctnessOfAnswers[index];
    }

    /**
     * The method asks all questions specified in the array.
     * Displays the score at the end.
     *
     * @param array array with String objects of specified format
     */
    static void makeAQuiz(String[] array) {
        int score = 0;
        for (int i = 0; i < array.length; i++) {
            score += askAQuestion(array, i) ? 1 : 0;
        }
        System.out.println("Achieved score: " + score);
    }

    /**
     * The method displays main menu to a user.
     * Then it prompts the user to enter a character
     * representing one of the options if it's none
     * of the options it prompts a user again
     *
     * @return false if user chooses to quit true otherwise
     */
    static boolean makeInteractiveMainMenu() {
        System.out.println("s) start");
        System.out.println("q) quit");
        boolean passed;
        boolean running = true;
        String[] questions;
        do {
            String option = getExpression("Write s or q and press enter:");
            switch (option) {
                case "s" -> {
                    passed = true;
                    questions = makeInteractiveCategoryMenu();
                    makeAQuiz(questions);
                }
                case "q" -> {
                    passed = true;
                    running = false;
                }
                default -> passed = false;
            }
        } while (!passed);
        return running;
    }

    /**
     * The method displays category menu to a user.
     * Then it prompts the user to enter a character
     * representing one of the options if it's none
     * of the options it prompts a user again
     *
     * @return an array of Strings with question and answers
     */
    static String[] makeInteractiveCategoryMenu() {
        System.out.println("Categories:");
        System.out.println("a) film");
        System.out.println("b) geography");
        System.out.println("c) history");
        System.out.println("d) science");
        boolean passed = true;
        String[] items;
        do {
            String option = getExpression("Write a, b, c or d and press enter:");
            final int numberOfQuestions = 5;
            switch (option) {
                case "a" -> items = getRandomSubArray(getQuestionsAndAnswers("files/film"), numberOfQuestions);
                case "b" -> items = getRandomSubArray(getQuestionsAndAnswers("files/geography"), numberOfQuestions);
                case "c" -> items = getRandomSubArray(getQuestionsAndAnswers("files/history"), numberOfQuestions);
                case "d" -> items = getRandomSubArray(getQuestionsAndAnswers("files/science"), numberOfQuestions);
                default -> {
                    items = null;
                    passed = false;
                }
            }
        } while (!passed);
        return items;
    }

    /**
     * The method runs a program allowing you to take quizes,
     * choose categories and quit the program
     *
     */
    public static void runQuizWithMenuAndCategories() {
        boolean going = true;
        while (going) {
            going = makeInteractiveMainMenu();
        }
    }

    /**
     * The method checks if array numbers contains element target at or before given index
     *
     * @param numbers integer array to be checked
     * @param target  integer looked for in an array
     * @param index   index in an array at which and before which array is checked to contain target element
     * @return true if array contains target at or before index
     */
    private static boolean isContainedInAnArray(int[] numbers, int target, int index) {
        if (numbers == null || numbers.length == 0)
            throw new IllegalArgumentException("Wrong array argument");
        if (index < 0 || index >= numbers.length)
            throw new IllegalArgumentException("Wrong index");
        for (int i = 0; i <= index; i++) {
            if (numbers[i] == target) {
                return true;
            }
        }
        return false;
    }

    /**
     * The method creates an array of size given by newArraySize with indexes choosen randomly
     * from those that would be present in array of size given by oldArraySize
     *
     * @param oldArraySize size of an array which indexes should be present in created array
     * @param newArraySize size of a newly created array
     * @return an array of indexes
     */
    private static int[] getArrayOfRandomIndexes(int oldArraySize, int newArraySize) {
        if (oldArraySize <= 0)
            throw new IllegalArgumentException("oldArraySize must be a positive Integer");

        if (newArraySize <= 0)
            throw new IllegalArgumentException("newArraySize must be a positive Integer");

        if (newArraySize > oldArraySize)
            throw new IllegalArgumentException("newArraySize must be less or equal to oldArraySize");

        Random random = new Random();
        int[] indexes = new int[newArraySize];
        indexes[0] = random.nextInt(oldArraySize);
        int temp;
        for (int i = 1; i < newArraySize; i++) {
            do {
                temp = random.nextInt(oldArraySize);
            } while (isContainedInAnArray(indexes, temp, i - 1));
            indexes[i] = temp;
        }
        return indexes;
    }

    /**
     * The method creates String array of a given size from a given larger String array
     * with elements choosen randomly from a given array
     *
     * @param array   source array
     * @param newSize size of the returned array
     * @return array of Strings chosed randomly from a larger array
     */
    static String[] getRandomSubArray(String[] array, int newSize) {
        if (array == null || array.length == 0)
            throw new IllegalArgumentException("Wrong array argument");
        if (newSize > array.length)
            throw new IllegalArgumentException("newSize must be less or equal to the length of an array");
        int[] indexes = getArrayOfRandomIndexes(array.length, newSize);
        String[] result = new String[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            result[i] = array[indexes[i]];
        }
        return result;
    }

    /**
     * The main method of the program
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        runQuizWithMenuAndCategories();
    }
}
