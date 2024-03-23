package application;

//rasha mansour-1210773
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Equation {

	// attributes
	static ArrayList<String> equationList = new ArrayList<>();

	// FileReader
	public static String FileReader(String path) throws FileNotFoundException {
		if (path.isEmpty()) {
			return "Error: Path is empty!";
		}

		String[] extension = path.split("\\.");
		if (!(extension[extension.length - 1].equals("242"))) {
			return "Error: Wrong file extension";
		}

		equationList = new ArrayList<>();
		File myfile = new File(path);

		try (Scanner scanner = new Scanner(myfile)) {
			StringBuilder content = new StringBuilder();

			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();

				if (line.isEmpty()) {
					continue; // Skip empty lines
				}

				content.append(line).append("\n");
			}

			// Print the file content for debugging
			System.out.println("File Content:\n" + content);

			String result = processContent(content.toString().trim());

			if (!result.equals("Success")) {
				return "invalid File: " + result;
			}

			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	// method to check the validation of the file
	private static String processContent(String content) {
		boolean found242 = false;
		boolean insideSection = false;
		boolean insideInfix = false;
		boolean insidePostfix = false;
		boolean insideEquation = false;
		StringBuilder currentSectionContent = new StringBuilder();

		// Split content by < and >
		String[] tokens = content.split("[<>]");
		for (String token : tokens) {
			String trimmedToken = token.trim();

			if (trimmedToken.isEmpty()) {
				continue; // Skip empty tokens
			}

			switch (trimmedToken) {
			case "242":
				found242 = true;
				break;
			case "/242":
				if (!found242) {
					return "Error: Delimiter </242> doesn't have an opening delimiter";
				}
				found242 = false;
				break;
			case "section":
				if (insideSection || insideInfix || insidePostfix || insideEquation) {
					return "Error: Incorrect starting tag within another tag";
				}
				insideSection = true;
				break;
			case "/section":
				if (!insideSection) {
					return "Error: Mismatched section tags";
				}
				insideSection = false;
				break;
			case "infix":
				if (insideInfix || insidePostfix || insideEquation) {
					return "Error: Incorrect starting tag within another tag";
				}
				insideInfix = true;
				break;
			case "/infix":
				if (!insideInfix) {
					return "Error: Mismatched infix tags";
				}
				insideInfix = false;
				break;
			case "postfix":
				if (insidePostfix || insideEquation) {
					return "Error: Incorrect starting tag within another tag";
				}
				insidePostfix = true;
				break;
			case "/postfix":
				if (!insidePostfix) {
					return "Error: Mismatched postfix tags";
				}
				insidePostfix = false;
				break;
			case "equation":
				if (insideEquation) {
					return "Error: Incorrect starting tag within another tag";
				}
				insideEquation = true;
				currentSectionContent = new StringBuilder(); // Start a new equation
				break;
			case "/equation":
				if (!insideEquation) {
					return "Error: Mismatched equation tags";
				}
				insideEquation = false;
				if (!currentSectionContent.toString().trim().isEmpty()) {
					String equation = currentSectionContent.toString().trim();
					if (!Equation.equationList.contains(equation)) {
						Equation.equationList.add(equation);
					}
				}
				break;
			default:
				// Accumulate content for the current section
				currentSectionContent.append(trimmedToken).append("\n");
				break;
			}
		}

		if (insideSection || insideInfix || insidePostfix || insideEquation) {
			return "Error: Tags are not correctly matched";
		}

		return "Success";
	}

	// method to get The Precedence
	public static int getThePrecedence(String c) {
		if (c.equals("^"))
			return 3;
		if (c.equals("*") || c.equals("/"))
			return 2;
		if (c.equals("-") || c.equals("+"))
			return 1;
		return 0;
	}

	// method to check operators
	private static boolean isOperator(String c) {
		switch (c) {
		case "^":
		case "*":
		case "/":
		case "-":
		case "+":
		case "%":
			return true;
		default:
			return false;
		}
	}

	// method to check Parenthesis
	public static boolean checkParenthesis(String eq) {
		CursorStack temp = new CursorStack(20);
		for (int i = 0; i < eq.length(); i++) {
			String c = String.valueOf(eq.charAt(i));
			switch (c) {
			case "(":
			case ")":
			case "{":
			case "}":
			case "[":
			case "]":
				if (temp.isEmpty()) {
					temp.push(c);
				} else if ((temp.peek().equals("(") && c.equals(")")) || (temp.peek().equals("{") && c.equals("}"))
						|| (temp.peek().equals("[") && c.equals("]"))) {
					temp.pop();
				} else
					temp.push(c);
				break;
			default:
				break;
			}
		}
		return temp.isEmpty();
	}

	// method to check infix validation
	public static boolean infixValidation(String eq) {
		int openCount = 0;
		int closeCount = 0;
		char prevChar = ' ';
		for (int i = 0; i < eq.length(); i++) {
			char c = eq.charAt(i);
			if (Character.isDigit(c)) {
				// If the previous character was also a digit, it's an invalid equation
				if (Character.isDigit(prevChar)) {
					return false;
				}
			} else if (c == '(') {
				openCount++;
			} else if (c == ')') {
				closeCount++;
				// If there is a digit before a closing parenthesis, it's an invalid equation
				if (Character.isDigit(prevChar)) {
					return false;
				}
			} else if (isOperator(String.valueOf(c))) {
				// If there is an operator before or after another operator, it's an invalid
				// equation
				if (prevChar == ' ' || isOperator(prevChar + "") || i == eq.length() - 1) {
					return false;
				}
			}
			prevChar = c;
		}
		return openCount == closeCount;
	}

	// method to convert from infix to postfix
	public static String infixToPostfix(String eq) {
		StringBuilder output = new StringBuilder();
		CursorStack temp = new CursorStack(20);

		for (int i = 0; i < eq.length(); ++i) {
			char c = eq.charAt(i);
			if (c != ' ') {
				if (Character.isDigit(c) || c == '.') {
					output.append(c);
				} else if (c == '(') {
					temp.push(String.valueOf(c));
				} else if (c == ')') {
					while (!temp.isEmpty() && !temp.peek().equals("(")) {
						output.append(" ").append(temp.pop().getElement());
					}

					if (!temp.isEmpty() && !temp.peek().equals("(")) {
						return "Error: invalid equation";
					} else
						temp.pop();
				} else {
					output.append(" ");
					while (!temp.isEmpty() && getThePrecedence(String.valueOf(c)) <= getThePrecedence(temp.peek())) {
						output.append(temp.pop().getElement()).append(" ");
					}
					temp.push(String.valueOf(c));
				}
			}
		}
		while (!temp.isEmpty()) {
			output.append(" ").append(temp.pop().getElement());
		}
		return output.toString().trim();
	}

	// method to convert from postfix to prefix
	public static String postfixToPrefix(String eq) {
		CursorStack operands = new CursorStack(20);
		String[] tokens = eq.split(" ");
		for (int i = 0; i < tokens.length; i++) {
			try {
				if (!isOperator(tokens[i])) {
					operands.push(tokens[i]);
				} else {
					if (operands.getStackSize() < 2) {
						throw new IllegalStateException("Not enough operands for operator: " + tokens[i]);
					}
					String val1 = operands.pop().getElement();
					String val2 = operands.pop().getElement();
					operands.push(tokens[i] + " " + val2 + " " + val1);
				}
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
				return "Error: " + e.getMessage();
			}
		}

		if (operands.getStackSize() == 1) {
			return operands.pop().getElement();
		} else {
			return "Error: Invalid expression";
		}
	}

	// method to evaluate postfix
	public static String evaluatePostfix(String eq) {
		CursorStack operands = new CursorStack(20);
		String[] tokens = eq.split(" ");
		for (int i = 0; i < tokens.length; i++) {
			try {
				if (!isOperator(tokens[i])) {
					// Check if the token is a valid number
					if (isValidNumber(tokens[i])) {
						double parsedValue = Double.parseDouble(tokens[i]);
						operands.push(tokens[i]);
					} else {
						throw new NumberFormatException("Invalid numeric value: " + tokens[i]);
					}
				} else {
					if (operands.getStackSize() < 2) {
						throw new IllegalStateException("Not enough operands for operator: " + tokens[i]);
					}
					double val1 = Double.parseDouble(operands.pop().getElement());
					double val2 = Double.parseDouble(operands.pop().getElement());
					switch (tokens[i]) {
					case "+":
						operands.push(String.valueOf(val2 + val1));
						break;
					case "^": // Power operator
						operands.push(String.valueOf(Math.pow(val2, val1)));
						break;
					case "-":
						operands.push(String.valueOf(val2 - val1));
						break;
					case "*":
						operands.push(String.valueOf(val2 * val1));
						break;
					case "/":
						operands.push(String.valueOf(val2 / val1));
						break;

					default:
						throw new IllegalArgumentException("Invalid operator: " + tokens[i]);
					}
				}
			} catch (NumberFormatException e) {
				System.err.println("Error parsing Double for token: " + tokens[i]);
				e.printStackTrace();
				return "Error: Invalid expression";
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
				return "Error: " + e.getMessage();
			}
		}

		if (operands.getStackSize() == 1) {
			return operands.pop().getElement();
		} else {
			return "Error: Invalid expression";
		}
	}

	// method to check the validation of the numbers
	private static boolean isValidNumber(String token) {
		try {
			Double.parseDouble(token);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// method to evaluate prefix
	public static String evaluatePrefix(String eq) {
		CursorStack operands = new CursorStack(20);
		String[] tokens = eq.split(" ");
		for (int i = tokens.length - 1; i >= 0; i--) {
			try {
				if (!isOperator(tokens[i])) {
					operands.push(tokens[i]);
				} else {
					if (operands.getStackSize() < 2) {
						throw new IllegalStateException("Not enough operands for operator: " + tokens[i]);
					}
					String val1 = operands.pop().getElement();
					String val2 = operands.pop().getElement();
					operands.push(evaluatePrefixOperation(tokens[i], val1, val2));
				}
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
				return "Error: " + e.getMessage();
			}
		}

		if (operands.getStackSize() == 1) {
			return operands.pop().getElement();
		} else {
			return "Error: Invalid expression";
		}
	}

	// method to evaluate prefix operation
	private static String evaluatePrefixOperation(String operator, String operand1, String operand2) {
		switch (operator) {
		case "+":
			return String.valueOf(Double.parseDouble(operand1) + Double.parseDouble(operand2));
		case "-":
			return String.valueOf(Double.parseDouble(operand1) - Double.parseDouble(operand2));
		case "*":
			return String.valueOf(Double.parseDouble(operand1) * Double.parseDouble(operand2));
		case "/":
			return String.valueOf(Double.parseDouble(operand1) / Double.parseDouble(operand2));
		case "^": // Power operator
			return String.valueOf(Math.pow(Double.parseDouble(operand1), Double.parseDouble(operand2)));
		default:
			throw new IllegalArgumentException("Invalid operator: " + operator);
		}
	}

}