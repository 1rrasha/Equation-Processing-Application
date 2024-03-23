package application;

//rasha mansour-1210773
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main extends Application {
	// attributes
	private int currentSectionIndex = 0;
	TextArea eq;
	TextField pa;

	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane root = new GridPane();
			root.setHgap(10);
			root.setVgap(10);
			root.setAlignment(Pos.CENTER);

			Label fileNameLabel = new Label("File Name:");
			fileNameLabel.setFont(Font.font("Verdana", 15));

			Button load = new Button("Load");
			load.setFont(Font.font("Verdana", 15));

			pa = new TextField();
			pa.setPromptText("Path");
			pa.setFont(Font.font("Verdana", 15));

			eq = new TextArea();
			eq.setPrefSize(450, 400);
			eq.setMaxWidth(500);
			eq.setFont(Font.font("Verdana", 15));

			Button previous = new Button("Prev");
			previous.setFont(Font.font("Verdana", 15));

			Button next = new Button("Next");
			next.setFont(Font.font("Verdana", 15));

			load.setStyle("-fx-background-color: green; -fx-text-fill: white;");
			previous.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
			next.setStyle("-fx-background-color: blue; -fx-text-fill: white;");

			HBox buttonsBox = new HBox(10, previous, next);
			buttonsBox.setAlignment(Pos.CENTER);

			root.addColumn(0, fileNameLabel, load, pa, eq, buttonsBox);
			root.setAlignment(Pos.CENTER);
			// load button
			load.setOnAction(e -> {
				try {

					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Choose File");
					File selectedFile = fileChooser.showOpenDialog(primaryStage);

					if (selectedFile != null) {
						pa.setText(selectedFile.getPath());
						fileNameLabel.setText("File Name: " + selectedFile.getName());

						String result = Equation.FileReader(pa.getText().trim());

						Platform.runLater(() -> {
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setTitle("File Loaded");
							alert.setHeaderText(null);
							alert.setContentText(result);
							alert.show();
						});

						if (result.equals("Success")) {
							if (Equation.equationList.isEmpty()) {
								displayErrorAlert("Empty File", "The loaded file doesn't contain any equations.");
							} else {
								currentSectionIndex = 0;
								processEquations();

								// Add debug print
								System.out.println("Content of Equation.equationList: " + Equation.equationList);

							}
						}
					}
				} catch (Exception t) {
					displayErrorAlert("Error loading file", "An error occurred while loading the file.");
					t.printStackTrace();
				}
			});
			// next button
			next.setOnAction(e -> {
				try {
					if (currentSectionIndex < Equation.equationList.size() - 1) {
						currentSectionIndex++;
						processEquations();
					} else {
						displayErrorAlert("End of Sections", "No more sections available.");
					}
				} catch (Exception o) {
					displayErrorAlert("Error navigating to the next section",
							"An error occurred while navigating to the next section.");
					o.printStackTrace();
				}
			});

			// previous button
			previous.setOnAction(e -> {
				try {
					if (currentSectionIndex > 1) {
						currentSectionIndex--;
						processEquations();
					} else {
						displayErrorAlert("Start of Sections", "Already at the beginning of sections.");
					}
				} catch (Exception o) {
					displayErrorAlert("Error navigating to the previous section",
							"An error occurred while navigating to the previous section.");
					o.printStackTrace();
				}
			});

			Image icon = new Image(
					new File("C:\\Users\\user\\Desktop\\java\\Rasha_Proj2\\download.png").toURI().toString());

			// Set the icon for the stage
			primaryStage.getIcons().add(icon);
			Scene sc = new Scene(root, 800, 600);
			primaryStage.setTitle("Rasha project II");
			primaryStage.setScene(sc);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Display an error alert to the user
	private void displayErrorAlert(String title, String content) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(title);
			alert.setHeaderText(null);
			alert.setContentText(content);
			alert.show();
		});
	}

	// method to process sections from the file
	private void processEquations() {
		try {
			// Load file
			String fileContent = new String(Files.readAllBytes(Paths.get(pa.getText())));
			StringBuilder output = new StringBuilder();

			// Split sections
			String[] sections = fileContent.split("<section>");

			// Check if there are sections
			if (sections.length <= 1 || currentSectionIndex >= sections.length) {
				displayErrorAlert("No More Sections", "No more sections found in the file.");
				return;
			}

			// Find the closing tag of the current section
			int endIndex = sections[currentSectionIndex].indexOf("</section>");
			if (endIndex != -1) {
				String section = sections[currentSectionIndex].substring(0, endIndex);

				// Get infix equations
				String infixContent = extractEquation(section, "<infix>", "</infix>");
				String[] infixEquations = infixContent.split("<equation>|</equation>");

				// Get postfix equations
				String postfixContent = extractEquation(section, "<postfix>", "</postfix>");
				String[] postfixEquations = postfixContent.split("<equation>|</equation>");

				// Process infix equations
				for (String infixEquation : infixEquations) {
					if (!infixEquation.trim().isEmpty()) {
						processInfixEquation(infixEquation, output);
					}
				}

				// Process postfix equations
				for (String postfixEquation : postfixEquations) {
					if (!postfixEquation.trim().isEmpty()) {
						processPostfixEquation(postfixEquation, output);
					}
				}

				output.append("\n----------------------------\n");
			}

			eq.setText(output.toString());

		} catch (IOException e) {
			e.printStackTrace();
			displayErrorAlert("Error Loading File", "An error occurred while loading the file.");
		}
	}

	// method to extract tags
	private String extractEquation(String section, String startTag, String endTag) {
		int startIndex = section.indexOf(startTag);
		int endIndex = section.indexOf(endTag);
		if (startIndex != -1 && endIndex != -1) {
			return section.substring(startIndex + startTag.length(), endIndex).trim();
		}
		return "";
	}

	// method to process Infix Equation
	private void processInfixEquation(String infixEquation, StringBuilder output) {
		output.append("Infix:\n* ").append(infixEquation).append(" ==> ");

		if (!Equation.checkParenthesis(infixEquation)) {
			output.append("Unbalanced parenthesis");

		} else {
			String postfix = Equation.infixToPostfix(infixEquation);
			output.append("Postfix: ").append(postfix).append(" ==> ");
			String result = Equation.evaluatePostfix(postfix);
			output.append(result).append("\n");
		}

		output.append("\n\n");
	}

	// method to process postfix Equation
	private void processPostfixEquation(String PostfixEquation, StringBuilder output) {
		output.append("Postfix:\n* ").append(PostfixEquation).append(" ==> ");

		if (!Equation.checkParenthesis(PostfixEquation)) {
			output.append("Unbalanced parenthesis");

		} else {
			String prefix = Equation.postfixToPrefix(PostfixEquation);
			output.append("Prefix: ").append(prefix).append(" ==> ");
			String result = Equation.evaluatePrefix(prefix);
			output.append(result).append("\n");
		}

		output.append("\n\n");
	}

	public static void main(String[] args) {
		launch(args);
	}
}
