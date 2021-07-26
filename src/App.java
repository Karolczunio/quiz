import java.util.Scanner;

public class App {
    /*
    The user enters certain number of lines of text.
    All entered lines should be collected in an array.
    Each line should look like this: The capital city of Poland?;Warsaw-YES;Szczecin-NO;Cracow-NO
    After taking all answers and questions in a form presented above
    the program asks user questions he or she previously entered
    In the end program displays the score of the user.
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
     * @return an array of String objects containing lines with questions and answer in a correct format
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
        System.out.println("a) " + tokens[1].replaceFirst("(-YES|-NO)", ""));
        System.out.println("b) " + tokens[2].replaceFirst("(-YES|-NO)", ""));
        System.out.println("c) " + tokens[3].replaceFirst("(-YES|-NO)", ""));
        int index;
        String option;
        do {
            option = getExpression("Write a, b or c and press enter:");
            index = switch (option.toLowerCase()) {
                case "a":
                    yield 0;
                case "b":
                    yield 1;
                case "c":
                    yield 2;
                default:
                    yield -1;
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
     * The main method of the program
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        final int numberOfQuestions = 5;
        System.out.println("Enter " + numberOfQuestions + " lines containg questions and 3 answers to each of them.");
        System.out.println();
        String[] items = getQuestionsAndAnswers(numberOfQuestions);
        makeAQuiz(items);
    }
}
