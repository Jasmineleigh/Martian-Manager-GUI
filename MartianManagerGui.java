package application;
	
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application2.GreenMartian;
import application2.Martian;
import application2.MartianManager;
import application2.RedMartian;
import application2.Teleporter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MartianManagerGui extends Application {
	
	// Create Martians
	protected ToggleGroup tGrpCreateMartian;
	protected TextField txfId, txfVolume, txfTenacity;
	protected Button btnCreateMartian;
	protected MartianManager martianManager = new MartianManager();
	protected ArrayList<Martian> invaders = new ArrayList<>();
	
	// Display Martians
	protected ToggleGroup tGrpDisplayMartians;
	protected Button btnDisplayMartians, btnTeleport;
	protected TextField txfDestination;
	
	// ListView events
	protected Button btnDisplaySelected, btnRemoveSelected, btnClosestToSelected;
	protected ListView<String> lvwMartians = new ListView<>();

	// Battle events
	protected Button btnPopulateManager, btnCreateInvaders, btnBattleInvaders, btnReset;
	protected TextField txfNumToCreate;
	
	// Results
	protected TextArea txaMessage;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Pane root = buildGui();
			Scene scene = new Scene(root,750,590);
			primaryStage.setTitle("Martian Manager");
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private Pane buildGui() {
		// Build top row
		GridPane topRow = new GridPane();
		Pane p = this.buildCreateMartian();
		topRow.add(p, 0, 0);
		p = this.buildDisplayMartians();
		topRow.add(p, 1, 0);
		p = buildSelectMartians();
		topRow.add(p, 0, 1);
		p = buildBattleChoices();
		topRow.add(p, 1, 1);

		// Build bottom row
		p = buildResults();
		
		// Build root container
		VBox root = new VBox();
		root.getStyleClass().add("h_or_v_box");		
		root.getStyleClass().add("boxBorder");		
		root.getChildren().addAll(topRow, p);
		
		return root;
	}

	private Pane buildCreateMartian() {
		// Build radio buttons and event button
		btnCreateMartian = new Button("Create");
		btnCreateMartian.setOnAction(new CreateMartianEventHandler());
		RadioButton rbGreen = new RadioButton("Green");
		tGrpCreateMartian = new ToggleGroup();
		rbGreen.setToggleGroup(tGrpCreateMartian);
		RadioButton rbRed = new RadioButton("Red");
		rbRed.setToggleGroup(tGrpCreateMartian);

		HBox hBoxRadios = new HBox();
		hBoxRadios.getStyleClass().add("h_or_v_box");			
		hBoxRadios.getChildren().addAll(btnCreateMartian, rbGreen, rbRed);

		// Build text fields
		Label lblId = new Label("Id");
		txfId = new TextField();
		txfId.getStyleClass().add("textFieldCreate");	
		Label lblVol = new Label("Volume");
		txfVolume = new TextField();
		txfVolume.getStyleClass().add("textFieldCreate");	
		Label lblTen = new Label("Tenacity");
		txfTenacity = new TextField();
		txfTenacity.getStyleClass().add("textFieldCreate");	

		HBox hBoxFields = new HBox();
		hBoxFields.getStyleClass().add("h_or_v_box");
		hBoxFields.getChildren().addAll(lblId, txfId, lblVol, txfVolume, lblTen, txfTenacity);

		// Build root container
		VBox vBox = new VBox();
		vBox.getStyleClass().add("h_or_v_box");		
		vBox.getStyleClass().add("boxBorder");		
		vBox.getChildren().addAll(hBoxRadios, hBoxFields);

		return vBox;
	}

	private Pane buildDisplayMartians() {
		// Build radio buttons and event button
		btnDisplayMartians = new Button("Display");
		btnDisplayMartians.setOnAction(new DisplayMartiansEventHandler());
		RadioButton rbGreen = new RadioButton("Green");
		tGrpDisplayMartians = new ToggleGroup();
		rbGreen.setToggleGroup(tGrpDisplayMartians);
		RadioButton rbRed = new RadioButton("Red");
		rbRed.setToggleGroup(tGrpDisplayMartians);

		RadioButton rbAll = new RadioButton("All");
		rbAll.setToggleGroup(tGrpDisplayMartians);
		RadioButton rbSorted = new RadioButton("Sorted");
		rbSorted.setToggleGroup(tGrpDisplayMartians);

		HBox hBoxRadio1 = new HBox();
		hBoxRadio1.getStyleClass().add("h_or_v_box");
		hBoxRadio1.getChildren().addAll(btnDisplayMartians, rbGreen, rbRed, rbAll, rbSorted);

		// Build teleport
		btnTeleport = new Button("Teleport");
		btnTeleport.setOnAction(new TeleportEventHandler());
		Label lbl = new Label("detination");
		txfDestination = new TextField();

		HBox hBoxTeleport = new HBox();
		hBoxTeleport.getStyleClass().add("h_or_v_box");
		hBoxTeleport.getChildren().addAll(btnTeleport, lbl, txfDestination);

		// Build root pane
		VBox vBox = new VBox();
		vBox.getStyleClass().add("h_or_v_box");
		vBox.getStyleClass().add("boxBorder");		
		vBox.getChildren().addAll(hBoxRadio1, hBoxTeleport);
		
		return vBox;
	}

	private Pane buildSelectMartians() {
		// Build ListView
		lvwMartians.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		lvwMartians.setPrefHeight(150);
		lvwMartians.setPrefWidth(120);

		// Build button box.
		Label lbl = new Label("Select 1 or more Martians");
		btnDisplaySelected = new Button("Display");
		btnDisplaySelected.setOnAction(new DisplaySelectedEventHandler());
		btnRemoveSelected = new Button("Remove");
		btnRemoveSelected.setOnAction(new RemoveMartiansEventHandler());
		btnClosestToSelected =new Button("Closest to");
		btnClosestToSelected.setOnAction(new ClosestToEventHandler());
		VBox vBox = new VBox();
		vBox.getStyleClass().add("h_or_v_box");		
		vBox.getChildren().addAll(lbl, btnDisplaySelected, btnRemoveSelected, btnClosestToSelected);
		
		// Build root
		HBox hBox = new HBox();
		hBox.getStyleClass().add("h_or_v_box");			
		hBox.getStyleClass().add("boxBorder");
		hBox.getChildren().addAll(lvwMartians, vBox);
		return hBox;
	}

	private Pane buildBattleChoices() {
		// Create buttons
		btnPopulateManager = new Button("Populate Manager");
		btnPopulateManager.setOnAction(new PopulateManagerEventHandler());
		btnCreateInvaders = new Button("Create Invaders");
		btnCreateInvaders.setOnAction(new CreateInvadersEventHandler());
		btnBattleInvaders = new Button("Battle Invaders");
		btnBattleInvaders.setOnAction(new BattleEventHandler());
		btnReset = new Button("Reset All");
		btnReset.setOnAction(new ResetEventHandler());
		// Build num to create.
		HBox hBox = new HBox();
		hBox.getStyleClass().add("h_or_v_box");	
		Label lbl = new Label("num");
		txfNumToCreate = new TextField();
		txfNumToCreate.getStyleClass().add("textFieldNum");	
		hBox.getChildren().addAll(btnPopulateManager, btnCreateInvaders, lbl, txfNumToCreate);
		
		String helpMsg = "Use 'Populate...' to create 'num' random Martians\n";
		helpMsg += "Use 'Create...' to create 'num' random invader Martians\n";
		helpMsg += "Create no more than 90 in total.";
		Label lblHelp = new Label(helpMsg);
		
		HBox hBox2 = new HBox();
		hBox2.getStyleClass().add("h_or_v_box");	
		hBox2.getChildren().addAll(btnBattleInvaders, btnReset);
		
		// Build root
		GridPane root = new GridPane();
		root.getStyleClass().add("grid");			
		root.getStyleClass().add("boxBorder");

		root.add(hBox, 0, 0);
		root.add(lblHelp, 0, 1);
		root.add(hBox2, 0, 2);

		return root;
	}

	private Pane buildResults() {
		// Create controls
		Label lbl = new Label("Results");
		txaMessage = new TextArea();
		
		VBox vBox = new VBox();
		vBox.getStyleClass().add("h_or_v_box");	
		vBox.getChildren().addAll(lbl, txaMessage);
		
		return vBox;
	}
	
	/*----------------------------------------------------------------------------
	 * Event handlers
	 ----------------------------------------------------------------------------*/
	/**
	 * Creates a Martian with the values specified and (a) saves in the the martian manager,
	 * (b) add the martian to the list view, (c) and displays the martian in the text area.
	 * Note the format for the items in the list view: firstCharColor-id. For example for a
	 * Green Martian with id=34, the list view would display: G-34.
	 * 
	 * Button text: "Create", name: "btnCreateMartians", created: buildCreateMartian()
	 */
	private class CreateMartianEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// get radio button user selected
						RadioButton rdCreateMartian = (RadioButton)tGrpCreateMartian.getSelectedToggle();
						String textOfCreateMartian = rdCreateMartian.getText();
						Martian martian = null;
						lvwMartians.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
						
						switch(textOfCreateMartian) {
						case "Green":
							String idText = txfId.getText();
							String volumeText = txfVolume.getText();
							
							int id = 0;
							int volume = 0;
							
							if(isInteger(idText)) {
								 id = Integer.parseInt(idText);
								 if(isInteger(volumeText)) {
										volume = Integer.parseInt(volumeText);
										martian = new GreenMartian(id, volume);
									}
							}
							
							martianManager.addMartian(martian);
							
							System.out.println("Creating a green martian...");
							break;
							
						case "Red":
							String tenacityText = txfTenacity.getText();
							idText = txfId.getText();
							volumeText = txfVolume.getText();
							
							id = 0;
							volume = 0;
							int tenacity = 0;
							
							if(isInteger(idText)) {
								 id = Integer.parseInt(idText);
								 if(isInteger(volumeText)) {
										volume = Integer.parseInt(volumeText);
										if(isInteger(tenacityText)) {
											tenacity = Integer.parseInt(tenacityText);
											martian = new RedMartian(id, volume, tenacity);
										}
									}
							}
							
							martianManager.addMartian(martian);
							System.out.println("Creating a red martian...");
							break;
							
						}
						
						System.out.println("Adding Martian to the list...");
						lvwMartians.getItems().add(martian.toString());
						
						StringBuilder msg = new StringBuilder();
						msg.append("Martian: [" + martian + "] was added.");
						txaMessage.setText(msg.toString());
			
		}
	}

	/**
	 * Displays all the green martians, all the red martians, all martians, or sorted martians,
	 * depending on the radio button that is selected. The results are displayed in the text area.
	 * The "Red" choice will take a little bit of thought. Hint: use MartianManager.getMartianAt(i) 
	 * and loop over all and pick out the reds.
	 * 
	 * Button text: "Display", name: "btnDisplayMartians", created: buildDisplayMartians()
	 */
	private class DisplayMartiansEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			RadioButton displayMartians = (RadioButton)tGrpDisplayMartians.getSelectedToggle();
			String displayText = displayMartians.getText();
			String message = "";
			
			switch(displayText) {
				case "Green":
					message = "Green Martians:\n";
					System.out.println("Displaying Green Martians...");
					
					for(int i = 0; i < martianManager.getNumMartians(); i++) {
						if(martianManager.getMartianAt(i) instanceof GreenMartian) {
							message += martianManager.getMartianAt(i) + "\n";
						}
					}
					break;
					
				case "Red":
					message = "Red Martians:\n";
					System.out.println("Displaying Red Martians...");
					
					for(int i = 0; i < martianManager.getNumMartians(); i++) {
						if(martianManager.getMartianAt(i) instanceof RedMartian) {
							message += martianManager.getMartianAt(i) + "\n";
						}
					}
					break;
					
				case "All":
					message = "All Martians:\n";
					System.out.println("Displaying all Martians...");
					
					for(int i = 0; i < martianManager.getNumMartians(); i++) {
							message += martianManager.getMartianAt(i) + "\n";
					}
					
					break;
					
				case "Sorted":
					message = "Sorted Martians:\n";
					System.out.println("Displaying sorted Martians...");
					
					for(int i = 0; i < martianManager.getNumMartians(); i++) {
						ArrayList<Martian> sortedMartians = martianManager.getSortedMartians();
						message += sortedMartians.get(i).toString() + "\n";
					}
				
			}
			
			txaMessage.setText(message);
			txfVolume.setText(null);
			txfId.setText(null);
			txfTenacity.setText(null);
			
		}
	}

	/**
	 * Has all the teleporters teleport to the destination that is supplied by the user and the 
	 * results are displayed in the text area.
	 * 
	 * Button text: "Teleport", name: "btnTeleport", created: buildDisplayMartians()
	 */
	private class TeleportEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			String destination = txfDestination.getText();
			String message = "Teleporting Martians...\n";
			
			for(int i = 0; i < martianManager.getNumTeleporters(); i++) {
				Teleporter teleporter = martianManager.getTeleporterAt(i);
				message += teleporter.teleport(destination) + "\n";
				System.out.println("Teleporting Martians...");
			}
			txaMessage.setText(message);
		}
	}

	/**
	 * Displays the selected martians in the text area. 
	 * 
	 * Button text: "Display", name: "btnDisplaySelected", created: buildSelectMartians()
	 */
	private class DisplaySelectedEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			String selectedMartians = "";
			List<String> allMartians = lvwMartians.getSelectionModel().getSelectedItems();
			for(String martian: allMartians) {
				selectedMartians += martian + "\n";
				System.out.println("Displaying Martians...");
			}
			txaMessage.setText(selectedMartians);

		}
	}

	/**
	 * Removes the selected martians from the martian manager and from the listview, and
	 * displays the removed martians in the text area. Hints:
	 * (1) You obtain a list of string from the list view, each string (e.g. G-34),
	 * corresponds to a Martian. Thus, you can split off the id, and then use it to 
	 * remove the martian manager
	 * (2) the manager's remove returns the removed martian, which you can use for the
	 * display in the text area.
	 * (3) The list view can return the selected items as shown in labs and class:
	 * 
	 * 		List<String> selMartiansObs = lvwMartians.getSelectionModel().getSelectedItems();
	 * 
	 * However, this is an "observable" list which mean that when we use this to loop 
	 * over to do the removes from the list view itself, it will change this list which
	 * is problematic. Thus, immediately after the line above, create a new list
	 * which will be "safe" to iterate over.
	 * 
	 *      List<String> selMartians = new ArrayList<>(selMartiansObs);
	 * 
	 * (4) To remove from list view (for example): lvw.getItems().remove("G-34")
	 *   
	 * Button text: "Remove", name: "btnRemoveSelected", created: buildSelectMartians()
	 */
	private class RemoveMartiansEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			List<String> allMartians = lvwMartians.getSelectionModel().getSelectedItems();
			Martian martian = null;
			String message = "Removing martians...\n";
			for(int i = 0; i < allMartians.size(); i++) {	
				int id = getId(allMartians.get(i));
				martian = martianManager.removeMartian(id);
				lvwMartians.getItems().remove(allMartians.get(i));
				
				message += martian + " was removed.\n";
				
				System.out.println("Removing Martians...");
			}
			
			txaMessage.setText(message);

		}
	}

	/**
	 * Finds the martians closest to the selected martians and displays both in the text area. 
	 * 
	 * Button text: "Closest to", name: "btnClosestToSelected", created: buildSelectMartians()
	 */
	private class ClosestToEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			List<String> allMartians = lvwMartians.getSelectionModel().getSelectedItems();
			Martian martianSelected = null;
			Martian martianClosestTo = null;
			String message = "Martians closest:\n";
			
			for(String martian: allMartians) {
				int id = getId(martian);
				martianSelected = martianManager.getMartianWithId(id);
				martianClosestTo = martianManager.getMartianClosestTo(martianSelected);
				message += "Martian selected: " + martianSelected + "\nMartian closest: " + martianClosestTo + "\n";
			}
			
			System.out.println("Getting closest martian...");
			txaMessage.setText(message);

		}
	}

	/**
	 * Creates "num" random martians, adds them to the manager, adds them to the list view, and displays 
	 * them. A martian is green with probability 0.5. It has an id that is uniformly random between 1 and 100.
	 * It has a volume that is uniformly random between 1 and 100. If it is red, it has a tenacity that 
	 * is uniformly random between 1 and 5. Notes on how to create random values is shown on the 
	 * problem statement. Suggestion, you should write a helper method: createRandomMartian():Martian.
	 * 
	 * Button text: "Populate Manager", name: "btnPopulateManager", created: buildBattleChoices()
	 */
	private class PopulateManagerEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			int numMartians = Integer.parseInt(txfNumToCreate.getText());
			
			String message = "Populating Martian Manager...";
			System.out.println(message);
			
			for(int i = 0; i < numMartians; i++) {
				Martian martian = createRandomMartian();
				lvwMartians.getItems().add(martian.toString());
				martianManager.addMartian(martian);
			}
			
			message = "Martian Manager populated with " + numMartians + " Martians.";
			txaMessage.setText(message);
		}
	}

	/**
	 * Creates "num" random martians that are considered "invaders" and displays them to the text area.
	 * The "invaders" can then be use later when "Battle" is chosen. Hint: you will need a list of 
	 * Martians to hold these, so that they can be used later for a battle. Thus, make the list an instance
	 * variable. Hint: use: createRandomMartian() from above. Invaders have the same qualities as above:
	 *  
	 * A martian is green with probability 0.5. It has an id that is uniformly random between 1 and 100.
	 * It has a volume that is uniformly random between 1 and 100. If it is red, it has a tenacity that 
	 * is uniformly random between 1 and 5. Notes on how to create random values is shown on the 
	 * problem statement.
	 * 
	 * Button text: "Create Invaders", name: "btnCreateInvaders", created: buildBattleChoices()
	 */
	private class CreateInvadersEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			int numInvaders = Integer.parseInt(txfNumToCreate.getText());
			
			String message = "Creating Invaders...";
			System.out.println(message);
			
			for(int i = 0; i < numInvaders; i++) {
				Martian martian = createRandomMartian();
				invaders.add(martian);
			}
			
			message = "Created " + numInvaders + " Invaders.";
			txaMessage.setText(message);
		}
	}

	/**
	 * A "battle" takes place between the martians in the manager and the "invaders" and the killed
	 * martians are displayed in the text area and removed from the list view.
	 * 
	 * Button text: "Battle", name: "btnBattleInvaders", created: buildBattleChoices()
	 */
	private class BattleEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			ArrayList<Martian> fallenMartians = martianManager.battle(invaders);
			String message = "We honor those who have fallen...\n";
			List<String> allMartians = lvwMartians.getItems();
			
			for(int i = 0; i < fallenMartians.size();i++) {
				message += fallenMartians.get(i) + "\n";
				for(int j = 0; j < allMartians.size(); j++) {
					if(fallenMartians.get(i).toString().equals(allMartians.get(j))) {
						lvwMartians.getItems().remove(allMartians.get(j));
					}
				}
			}
			
			
			txaMessage.setText(message);
			
		}
	}

	/**
	 * Removes all the martians from the manager and invader, and removes from list view.
	 * Hints:
	 * (1) the simplest way to remove from manager and invaders is to simply re-instantiate them.
	 * martians are displayed in the text area and removed from the list view.
	 * (2) the simplest way to remove from the list view is: lvw.getItems().clear()
	 * 
	 * Button text: "Reset All", name: "btnReset", created: buildBattleChoices()
	 */
	private class ResetEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			
			String message = "Martians and Invaders have been reset.\nMartians Removed: \n";
		
			invaders.clear();
			
			for(int i = 0; i < martianManager.getNumMartians();i++) {
				message += martianManager.getMartianAt(i) + "\n";
				lvwMartians.getItems().remove(martianManager.getMartianAt(i).toString());
			}
			martianManager = new MartianManager();
			txaMessage.setText(message);
		}
	}
	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		}
		catch(NumberFormatException nfe) {
			return false;
		}
	}
	
	private Martian createRandomMartian() {
		Random rage = new Random();
		String[] colors = {"red", "green"};
		String color = colors[(int)(Math.random() * colors.length)];
		int id = rage.nextInt(100) + 1;
		int volume = rage.nextInt(100) + 1;
		int tenacity = rage.nextInt(5) + 1;
		Martian martian = null;
		
		if(color.equals("red")) {
			martian = new RedMartian(id, volume, tenacity);
		}
		else {
			martian = new GreenMartian(id, volume);
		}
		
		return martian;
	}

	private int getId(String s) {
		int id =  Integer.parseInt(s.substring((s.indexOf('=') + 1), (s.indexOf('v') - 1)));
		return id;
	}
	public static void main(String[] args) {
		launch(args);
	}
	
}
