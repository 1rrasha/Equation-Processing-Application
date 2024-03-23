package application;

//rasha mansour-1210773
import java.io.FileNotFoundException;

//class to test equation class methods
public class EquationTest {

	public static void main(String[] args) {
		// Test FileReader method
		testFileReader("C:\\Users\\user\\Downloads\\DS-Proj2.242");

		// Test other methods
		testInfixToPostfix();
		testPostfixToPrefix();
		testEvaluatePostfix();
		testInfixValidation();

		// Evaluate Prefix Test
		String prefixExpression = "+ * 3 5 2";
		String resultEvaluatePrefix = Equation.evaluatePrefix(prefixExpression);
		System.out.println("Evaluate Prefix: " + resultEvaluatePrefix);

	}

	private static void testFileReader(String filePath) {
		try {
			String result = Equation.FileReader(filePath);
			System.out.println("FileReader Result: " + result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void testInfixToPostfix() {
		String infix = "2 + 3 * 4";
		String postfix = Equation.infixToPostfix(infix);
		System.out.println("Infix to Postfix: " + postfix);
	}

	private static void testPostfixToPrefix() {
		String postfix = "2 3 4 * +";
		String prefix = Equation.postfixToPrefix(postfix);
		System.out.println("Postfix to Prefix: " + prefix);
	}

	private static void testEvaluatePostfix() {
		String postfix = "2 3 4 * +";
		String result = Equation.evaluatePostfix(postfix);
		System.out.println("Evaluate Postfix: " + result);
	}

	private static void testInfixValidation() {
		String validInfix = "(2 + 3) * 4";
		String invalidInfix = "2 + * 3 4";
		boolean isValid = Equation.infixValidation(validInfix);
		boolean isInvalid = Equation.infixValidation(invalidInfix);
		System.out.println("Infix Validation - Valid: " + isValid);
		System.out.println("Infix Validation - Invalid: " + isInvalid);
	}
}
