// Assignment 5 ------------------------------------
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File; // loop all files in a folder, used when user chooses new avatar image

// card images: 73 x 97  Pixels
// avatar images: 128 x 128 Pixels

/**
 * The BigTwoTable class implements the CardGameTable interface. It is used to
 * build a GUI for the Big Two card game and handle all user actions. Below is a
 * detailed description for the BigTwoTable class.
 * 
 * @author Thomas Ho
 * 
 */
public class BigTwoTable implements CardGameTable {
	/**
	 * a constructor for creating a BigTwoTable.
	 * 
	 * @param game a reference to a card game associates with this table.
	 */
	BigTwoTable(CardGame game) {
		this.game = game;
		playerLabel = new JLabel[5];
		cardBackImage = new ImageIcon("images/_back.gif").getImage();
		avatars = new Image[4];
		avatars[0] = new ImageIcon("images/icon/Monsters-Character-Mikes-icon.png").getImage();
		avatars[1] = new ImageIcon("images/icon/Monsters-James-P-Sullivan-icon.png").getImage();
		avatars[2] = new ImageIcon("images/icon/Monsters-University-Character-Randy-Boggs-Icon.png").getImage();
		avatars[3] = new ImageIcon("images/icon/monsters-university-icons-by-designbolts.png").getImage();
		selected = new boolean[13];
		xPos = (ArrayList<Integer>[]) new ArrayList[4];
		yPos = (ArrayList<Integer>[]) new ArrayList[4];
		for (int i = 0; i < 4; i++) {
			xPos[i] = new ArrayList<Integer>();
			yPos[i] = new ArrayList<Integer>();
		}
		cardImages = new Image[13][4];
		for (int i = 0; i < 13; i++)
			for (int j = 0; j < 4; j++)
				cardImages[i][j] = new ImageIcon("images/" + Integer.toString(i) + "." + Integer.toString(j) + ".gif")
						.getImage();
		init();
	}

	/**
	 * a card game associates with this table
	 */
	private CardGame game;
	/**
	 * a boolean array indicating which cards are being selected
	 */
	private boolean[] selected;
	/**
	 * an integer specifying the index of the active player
	 */
	private int activePlayer;
	/**
	 * the main window of the application
	 */
	private JFrame frame;
	/**
	 * a panel for showing the cards of each player and the cards played on the
	 * table
	 */
	private JPanel bigTwoPanel;
	/**
	 * store MouseListener reference for bigTwoPanel. Pass this to
	 * PanelMouseListener#mouseClicked(), addMouseListener() and
	 * removeMouseListener().
	 */
	private boolean ifMouseListener;
	/**
	 * a menu bar holding the game menu
	 */
	private JMenuBar menuBar;
	/**
	 * a panel holding the Play and Pass buttons
	 */
	private JPanel bottomPanel;

	/**
	 * a ¡§Play¡¨ button for the active player to play the selected cards
	 */
	private JButton playButton;
	/**
	 * a ¡§Pass¡¨ button for the active player to pass his/her turn to the next player
	 */
	private JButton passButton;
	/**
	 * a text area for showing the current game status as well as end of game
	 * messages
	 */
	private JTextArea msgArea;
	/**
	 * a scroll pane for the text area
	 */
	private JScrollPane scrollPane;
	/**
	 * an JLabel array holding players' names
	 */
	private JLabel[] playerLabel;
	/**
	 * a 2D array storing the images for the faces of the cards. Ignore joker card.
	 */
	private Image[][] cardImages;
	/**
	 * an image for the backs of the cards
	 */
	private Image cardBackImage;
	/**
	 * an array storing the images for the avatars
	 */
	private Image[] avatars;
	
	private JTextArea chatBox;
	private JScrollPane scrollPane_chat;
	private JTextField textField;
	private int thisPlayerID= -1;
	
	public void setThisPlayerID(int i) {
		thisPlayerID= i;
	}

	/**
	 * Array of ArrayList. Stores x,y coordinates of each player's cards.
	 */
	private ArrayList<Integer>[] xPos, yPos;

	/**
	 * Prints the cards in this list to the JTextArea. Copied from
	 * CardList#print(boolean, boolean).
	 * 
	 * @param printFront a boolean value specifying whether to print the face (true)
	 *                   or the black (false) of the cards
	 * @param printIndex a boolean value specifying whether to print the index in
	 *                   front of each card
	 */
	public void printCards(boolean printFront, boolean printIndex, CardList temp) {
		ArrayList<Card> cards = new ArrayList<Card>();
		for (int i = 0; i < temp.size(); i++) {
			cards.add(temp.getCard(i));
		}
		if (cards.size() > 0) {
			for (int i = 0; i < cards.size(); i++) {
				String string = "";
				if (printIndex) {
					string = i + " ";
				}
				if (printFront) {
					string = string + "[" + cards.get(i) + "]";
				} else {
					string = string + "[  ]";
				}
				if (i % 13 != 0) {
					string = " " + string;
				}
				printMsg(string);
			}
		} else {
			printMsg("[Empty]");
		}
	}

	@Override
	/**
	 * a method for setting the index of the active player (i.e., the current
	 * player)
	 * 
	 * @param activePlayer an int value representing the index of the active player
	 */
	public void setActivePlayer(int activePlayer) {
		if (activePlayer < 0 || activePlayer >= 4) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
		}
	}

	/**
	 * a method for getting an array of indices of the cards selected
	 * 
	 * @return an array of indices of the cards selected
	 */
	@Override
	public int[] getSelected() {
		int count = 0;
		for (int i = 0; i < selected.length; i++) {
			if (selected[i]) {
				count++;
			}
		}
		if (count == 0)
			return null;
		else {
			int[] temp = new int[count];
			count = 0;
			for (int i = 0; i < selected.length; i++)
				if (selected[i]) {
					temp[count] = i;
					count++;
				}
			return temp;
		}
	}

	/**
	 * a method for resetting the list of selected cards
	 */
	@Override
	public void resetSelected() {
		for (int i = 0; i < selected.length; i++)
			selected[i] = false;
	}

	/**
	 * a method for repainting the GUI
	 */
	@Override
	public void repaint() {
		bigTwoPanel.repaint();
	}

	/**
	 * a method for printing the specified string to the message area of the GUI.
	 * Also makes the scrollbar scroll to bottom. <br>
	 * No automatic next line.
	 * 
	 * @param msg the string to be printed to the message area of the card game
	 *            table
	 */
	public void printMsg(String msg) {
		msgArea.append(msg);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
				scrollBar.setValue(scrollBar.getMaximum());
			}
		});

	}
	
	/**
	 * a method for printing the specified string to the chat box area of the GUI.
	 * Also makes the scrollbar scroll to bottom. <br>
	 * No automatic next line.
	 * 
	 * @param msg the string to be printed to the chat box area of the card game
	 *            table
	 */
	public void printMsg_chat(String msg) {
		chatBox.append(msg);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JScrollBar scrollBar = scrollPane_chat.getVerticalScrollBar();
				scrollBar.setValue(scrollBar.getMaximum());
			}
		});

	}

	/**
	 * a method for clearing the message area of the GUI
	 */
	@Override
	public void clearMsgArea() {
		msgArea.setText("");
	}

	/**
	 * a method for resetting the GUI. You should (i) reset the list of selected
	 * cards using resetSelected() method from the CardGameTable interface; (ii)
	 * clear the message area using the clearMsgArea() method from the CardGameTable
	 * interface; and (iii) enable user interactions using the enable() method from
	 * the CardGameTable interface.
	 */
	@Override
	public void reset() {
		resetSelected();
		clearMsgArea();
		selected = new boolean[13];
		xPos = (ArrayList<Integer>[]) new ArrayList[4];
		yPos = (ArrayList<Integer>[]) new ArrayList[4];
		for (int i = 0; i < 4; i++) {
			xPos[i] = new ArrayList<Integer>();
			yPos[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				xPos[i].add(128 + 25 * j);
				yPos[i].add(25 + 18 * (i + 1) + 128 * i);
			}
		}
		if (playerLabel[4] != null) {
			bigTwoPanel.remove(playerLabel[4]);
			playerLabel[4] = null;
		}
		for (int i=0; i< 4; i++) {
			if (playerLabel[i] == null)
				continue;
			playerLabel[i].setForeground(new Color(51, 51, 51));
		}
			
		enable();
	}

	/**
	 * a method for enabling user interactions with the GUI. You should (i) enable
	 * the ¡§Play¡¨ button and ¡§Pass¡¨ button (i.e., making them clickable); and (ii)
	 * enable the BigTwoPanel for selection of cards through mouse clicks.
	 */
	@Override
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		ifMouseListener = true;
	}

	/**
	 * a method for disabling user interactions with the GUI. You should (i) disable
	 * the ¡§Play¡¨ button and ¡§Pass¡¨ button (i.e., making them not clickable); and
	 * (ii) disable the BigTwoPanel for selection of cards through mouse clicks.
	 */
	@Override
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		ifMouseListener = false;
	}

	/**
	 * Additional method.<br>
	 * Initialize the GUI.
	 */
	public void init() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		JMenu menu = new JMenu("Game");
		menuBar.add(menu);
		JMenuItem connectMenuItem = new JMenuItem("Connect");
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		connectMenuItem.addActionListener(new ConnectMenuItemListener());
		quitMenuItem.addActionListener(new QuitMenuItemListener());
		menu.add(connectMenuItem);
		menu.add(quitMenuItem);

		bottomPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		frame.add(bottomPanel, BorderLayout.SOUTH);
		playButton = new JButton("Play");
		passButton = new JButton("Pass");
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());
		c.gridx= 0; c.weightx=0.1;
		bottomPanel.add(new JLabel(), c); 
		c.fill= GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 10, 10);
		c.ipadx = 10;
		c.ipady = 5;
		c.weightx=0;
		c.gridx=1;
		bottomPanel.add(playButton, c);
		c.gridx= 2;
		bottomPanel.add(passButton, c);

		msgArea = new JTextArea(10, 34);
		msgArea.setLineWrap(false);
		Font newFont = new Font(
				msgArea.getFont().getName(), Font.BOLD, msgArea.getFont().getSize() );
		msgArea.setFont(newFont);
		msgArea.setBackground(new Color(20, 146, 155)); // blue
		msgArea.setForeground(new Color(50, 50, 225)); // blue
		scrollPane = new JScrollPane(msgArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel rightPanel= new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(scrollPane, BorderLayout.NORTH);
		frame.add(rightPanel, BorderLayout.EAST);
//		frame.add(scrollPane, BorderLayout.EAST);
		
		chatBox= new JTextArea(10, 34);
		newFont = new Font(
				chatBox.getFont().getName(), Font.BOLD, chatBox.getFont().getSize() );
		chatBox.setFont(newFont);
		chatBox.setBackground(new Color(183, 151, 93)); // light brown
		chatBox.setForeground(new Color(0, 0, 0)); // black
		scrollPane_chat = new JScrollPane(chatBox);
		scrollPane_chat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_chat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		rightPanel.add(scrollPane_chat, BorderLayout.SOUTH);
		
		c.gridx= 3; c.weightx=0.1;
		bottomPanel.add(new JLabel(), c);
		JLabel inputLabel= new JLabel("Message: ");
		c.insets= new Insets(10, 0, 10, 10);
		c.ipadx = 0;
		c.weightx=0;
		c.gridx= 4;
		bottomPanel.add(inputLabel, c);
		textField= new JTextField(34);
		c.gridx= 5;
		bottomPanel.add(textField, c);
		
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str= textField.getText();
				((BigTwoClient)game).sendMessage(new CardGameMessage(
						CardGameMessage.MSG, -1, str));
				textField.setText("");
			}
		});
		
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setBackground(new Color(20, 155, 30)); // green
		bigTwoPanel.setLayout(null);
		for (int i = 0; i < 4; i++) {
			playerLabel[i] = new JLabel(game.getPlayerList().get(i).getName());
			bigTwoPanel.add(playerLabel[i]);
			Dimension size = playerLabel[i].getPreferredSize();
			playerLabel[i].setBounds(4, i * (128 + size.height), size.width, size.height);
		}
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				xPos[i].add(128 + 25 * j);
				yPos[i].add(25 + 18 * (i + 1) + 128 * i);
			}
		}
		bigTwoPanel.addMouseListener(new PanelMouseListener());
		ifMouseListener = true;
		frame.add(bigTwoPanel);

		bigTwoPanel.setPreferredSize(new Dimension(700, 750));
		frame.setGlassPane(new MyGlassPane());
		frame.getGlassPane().setVisible(true);
		setVisible();
	}
	
	/**
	 * make the JFrame visible
	 */
	public void setVisible() {
		frame.setVisible(true);
		frame.pack();
	}
	
	/**
	 * Update player names after the game has enough players.
	 */
	public void updateLabel() {
		for (int i = 0; i < 4; i++) {
			if (i == thisPlayerID)
				playerLabel[i].setText( game.getPlayerList().get(i).getName() + " <--- This is you" );
			else
				playerLabel[i].setText( game.getPlayerList().get(i).getName() );
			Dimension size = playerLabel[i].getPreferredSize();
			playerLabel[i].setBounds(4, i * (128 + size.height), size.width, size.height);
		}
	}
	
	/**
	 * Additonal method. <br>
	 * This method is called from {@link BigTwo#checkMove(int, int[])} and it will
	 * update the last hand played and display the cards on the bottom of the table.
	 * 
	 * @param player the player who played the last hand
	 */
	public void updateLastHandOnTable(CardGamePlayer player) {
		if (playerLabel[4] != null)
			bigTwoPanel.remove(playerLabel[4]);
		playerLabel[4] = new JLabel("Played by " + player.getName());
		bigTwoPanel.add(playerLabel[4]);
		Dimension size = playerLabel[4].getPreferredSize();
		playerLabel[4].setBounds(4, 4 * (128 + size.height) + 10, size.width, size.height);
	}

	/**
	 * Additonal method. <br>
	 * Resets the y-coordinates(shift downwards) of the cards selected by the player
	 * 
	 * @param playerID the player who have selected the cards
	 * @param a        an int array indicating which cards have been selected
	 */
	public void resetYPos(int playerID, int[] a) {
		if (playerID != thisPlayerID)
			return;
		if (a == null)
			return;
		for (int i = 0; i < a.length; i++) {
			yPos[playerID].set(a[i], yPos[playerID].get(a[i]) + 25);
		}
	}

	/**
	 * Additonal method. <br>
	 * Remove contents from ArrayList xPos and yPos, since the player has played the
	 * cards. This method is called from BigTwo#checkMove().
	 * 
	 * @param playerID the player who have played the cards
	 * @param a        an int array indicating which cards have been played
	 */
	public void removePos(int playerID, int[] a) {
		if (a == null)
			return;
		for (int i = a.length - 1; i >= 0; i--) {
			xPos[playerID].remove(i);
			yPos[playerID].remove(i);
		}
	}

	// Inner Classes --------------------------------

	/**
	 * an inner class that extends the JPanel class and implements the MouseListener
	 * interface. Overrides the paintComponent() method inherited from the JPanel
	 * class to draw the card game table.
	 * 
	 * @author Thomas Ho
	 *
	 */
	class BigTwoPanel extends JPanel {

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			for (int i = 0; i < game.getNumOfPlayers(); i++) {
				g.drawImage(avatars[i], 0, 128 * i + 18 * (i + 1), this);
				CardGamePlayer player = game.getPlayerList().get(i);
				if (i == thisPlayerID) {
					// show cards face
					CardList cards = player.getCardsInHand();
					for (int j = 0; j < player.getNumOfCards(); j++) {
						Card c = cards.getCard(j);
						g.drawImage(cardImages[c.getRank()][c.getSuit()], xPos[i].get(j), yPos[i].get(j), this);
					}

				} else {
					// show cards' back image
					for (int j = 0; j < player.getNumOfCards(); j++) {
						g.drawImage(cardBackImage, xPos[i].get(j), yPos[i].get(j), this);
					}
				}
			}
			// draw last hand on table
			if (playerLabel[4] != null) {
				Dimension d = this.getSize();
				g.drawLine(0, (18 + 128) * 4, d.width, (18 + 128) * 4);
				CardList playerCards = game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
				for (int i = 0; i < playerCards.size(); i++) {
					Card c = playerCards.getCard(i);
					g.drawImage(cardImages[c.getRank()][c.getSuit()], 4 + 25 * i, 25 + 18 * (4 + 1) + 128 * 4, this);
				}
			}
		}
	}

	/**
	 * an inner class that implements the MouseListener interface. Implements the
	 * mouseClicked() method from the MouseListener interface to handle mouse click
	 * events.
	 * 
	 * @author Thomas Ho
	 *
	 */
	class PanelMouseListener implements MouseListener {
		/**
		 * Process user's mouse click and select the cards. Invoked when the mouse
		 * button has been clicked (pressed and released) on a component.
		 * 
		 * @param e the event to be processed
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if (!ifMouseListener)
				return;
			CardGamePlayer player = game.getPlayerList().get(thisPlayerID);
			for (int i = player.getNumOfCards() - 1; i >= 0; --i) {
				if (e.getX() >= xPos[thisPlayerID].get(i) && e.getX() <= xPos[thisPlayerID].get(i) + 73)
					if (e.getY() >= yPos[thisPlayerID].get(i) && e.getY() <= yPos[thisPlayerID].get(i) + 97) {
						// System.out.println("CLicked");
						if (selected[i]) {
							selected[i] = false;
							yPos[thisPlayerID].set(i, yPos[thisPlayerID].get(i) + 25);
						} else {
							selected[i] = true;
							yPos[thisPlayerID].set(i, yPos[thisPlayerID].get(i) - 25);
						}
						break;
					}
			}
			// Check if player clicks his/her icon
			int i= thisPlayerID;
			if (e.getX() <= 128)
				if (e.getY() >= 128 * i + 18 * (i + 1) && e.getY() <= 128 * i + 18 * (i + 1) + 128) {
					//System.out.println("Icon CLicked");
					changeIcon();
					
				}
				
			bigTwoPanel.repaint();
		}

		// unused methods
		public void mousePressed(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}

	/**
	 * an inner class that implements the ActionListener interface. Implements the
	 * actionPerformed() method from the ActionListener interface to handle
	 * button-click events for the ¡§Play¡¨ button. When the ¡§Play¡¨ button is clicked,
	 * you should call the makeMove() method of your CardGame object to make a move.
	 * 
	 * @author Thomas Ho
	 *
	 */
	class PlayButtonListener implements ActionListener {
		/**
		 * Invoked when the button is clicked or activated (by keyboard). It calls
		 * {@link BigTwo#makeMove(int, int[])} and then call
		 * {@link BigTwoTable#resetSelected()}.
		 * 
		 * @param e the event to be processed
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (activePlayer != thisPlayerID) {
				printMsg("Wait for Your turn.\n");
				return;
			}
			// System.out.println("PLayed");
			int[] a = getSelected();
			// Ignore player if he/she didn't choose any card. They should use Pass button instead.
			if (a == null)
				return;
			// System.out.println(Arrays.toString(a));
			game.makeMove(thisPlayerID, a);
			resetSelected();
			repaint();
		}
	}

	/**
	 * an inner class that implements the ActionListener interface. Implements the
	 * actionPerformed() method from the ActionListener interface to handle
	 * button-click events for the ¡§Pass¡¨ button. When the ¡§Pass¡¨ button is clicked,
	 * you should call the makeMove() method of your CardGame object to make a move.
	 * 
	 * @author Thomas Ho
	 *
	 */
	class PassButtonListener implements ActionListener {
		/**
		 * Invoked when the button is clicked or activated (by keyboard).
		 * 
		 * @param e the event to be processed
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (activePlayer != thisPlayerID) {
				printMsg("Wait for Your turn.\n");
				return;
			}
			int[] a= getSelected();
			if (a != null)
				resetYPos(thisPlayerID, a);
			resetSelected();
			game.makeMove(thisPlayerID, null);
			repaint();
		}
	}

	/**
	 * an inner class that implements the ActionListener interface. Implements the
	 * actionPerformed() to connect the user to the server.
	 * 
	 * @author Thomas Ho
	 *
	 */
	class ConnectMenuItemListener implements ActionListener {
		/**
		 * Invoked when the menu item is clicked or activated (by keyboard).
		 * 
		 * @param e the event to be processed
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (((BigTwoClient)game).ifConnected()) {
				printMsg("Already connected to the server.\n;");
				return;
			}
			((BigTwoClient)game).makeConnection();
		}
	}

	/**
	 * an inner class that implements the ActionListener interface. Implements the
	 * actionPerformed() method from the ActionListener interface to handle
	 * menu-item-click events for the ¡§Quit¡¨ menu item. When the ¡§Quit¡¨ menu item is
	 * selected, you should terminate your application. (You may use System.exit()
	 * to terminate your application.)
	 * 
	 * @author Thomas Ho
	 *
	 */
	class QuitMenuItemListener implements ActionListener {
		/**
		 * Invoked when the menu item is clicked or activated (by keyboard).
		 * 
		 * @param e the event to be processed
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	/**
	 * Send chat message to the server.
	 * 
	 * @author Thomas Ho
	 */
	class textFieldListener implements ActionListener{
		/**
		 * Invoked when ENTER key is pressed.
		 * 
		 * @param e the event to be processed
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String str= textField.getName();
			textField.setText("");
			((BigTwoClient)game).sendMessage( new CardGameMessage(CardGameMessage.MSG, -1, str) );
		}
	}
	
	/**
	 * Display game result in a dialog window.
	 */
	public void gameEndMessage() {
		String str= "";
		str+=("Game ends\n");
		for (CardGamePlayer i : game.getPlayerList()) {
			if (i.getNumOfCards() > 0)
				str+=(i.getName() + " has " + i.getNumOfCards() + " cards in hand.\n");
			else
				str+=(i.getName() + " wins the game.\n");
		}
		int a= JOptionPane.showOptionDialog(frame,
				str,
				"Game results",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, null, null);
		if (a == JOptionPane.OK_OPTION) {
			((BigTwoClient)game).sendMessage( new CardGameMessage(CardGameMessage.READY, -1, null) );
		}
	}
	
	// Add ability to change player label's color, depending on situations where
	// player's action is legal/illegal
	
	/**
	 * If the move is legal, change next player's label to green, and change
	 * previous player's label to black.<br>
	 * If illegal, change current player's label to red, wait 0.75 second, and
	 * change it back to black color.
	 * 
	 * @param legal    indicates whether the last move in the BigTwo game is
	 *                 legal.<br>
	 *                 True if legal, false if illegal.
	 * @param playerID If legal is true, playerID is the id of next player after the
	 *                 previous player has made the move.<br>
	 *                 If legal is false, playerID is the id of current player.
	 */
	public void changePlayerLabelColorMethod( boolean legal, int playerID ) {
		if (legal == true) {
			// change this playerID's label color first, then change the previous player's color
			playerLabel[playerID].setForeground(Color.GREEN);
			
			int prev;
			if (playerID == 0)
				prev= 3;
			else
				prev= playerID-1;
			playerLabel[prev].setForeground(new Color(51, 51, 51));
		} else {
			// change this playerID's label color to red, wait a while, then to green
			playerLabel[playerID].setForeground(Color.RED);
		}
	}
	
	// Add ability to change player's icon (avatar)
	private JFrame changeIconFrame;
	
	/**
	 * Only works in Assignment 4. Here, the BigTwoTable of other players cannot update the icon.
	 * <br>
	 * Allow current player to choose new avatar image by creating a new JFrame and
	 * adding JButtons that each contains an image stored in images/icon<br>
	 * This method also disables the GUI while the player is choosing new avatar image.
	 */
	public void changeIcon() {
		// disable interaction with the BigTwo GUI first
		// enable only when user confirms icon or cancel it
		disable();
		changeIconFrame= new JFrame("Choose your new avatar image");
		changeIconFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		changeIconFrame.setLayout(new FlowLayout());
		File folder = new File("images/icon");
		File[] files = folder.listFiles();
		for (File i: files) {
			if (i.isFile()) {
				String relativePath= "images/icon/" + i.getName();
				ImageIcon im= new ImageIcon(relativePath);
				JButton button= new JButton(im);
				button.setName(relativePath);
				button.addActionListener( new changeIconButtonListener() );
				changeIconFrame.add(button);
		    }
		}
		// need to enable GUI in case user doesn't choose new icon and close the window
		// normally, choosing new icon will enable the GUI
		changeIconFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                enable();
            }
        });
		
		changeIconFrame.pack();
		changeIconFrame.setVisible(true);
	}
	
	/**
	 * Create an actionlistener that set a new avatar image for the current activeplayer.
	 * 
	 * @author Thomas Ho
	 */
	class changeIconButtonListener implements ActionListener {
		/**
		 * Invoked when the button (with avatar image) is clicked or activated (by keyboard).
		 * 
		 * @param e the event to be processed
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String newIcon= ((JButton)e.getSource()).getName();
			avatars[thisPlayerID] = new ImageIcon(newIcon).getImage();
			repaint();
			enable();
			changeIconFrame.dispatchEvent(new WindowEvent(changeIconFrame, WindowEvent.WINDOW_CLOSING));
		}
	}
	
	// Add translucent GlassPane at the beginning of the game
	
	/**
	 * Create a translucent GlassPane and ask player to enter their name, server IP and port number.
	 * The information will be updated in BigTwoClient accordingly.
	 * 
	 * @author Thomas Ho
	 */
	class MyGlassPane extends JPanel {
		/**
		 * Create a translucent GlassPane and ask player to enter their name, server IP and port number.
		 * Then set the values in BigTwoClient.
		 */
	    public MyGlassPane() {
	    	BigTwoTable.this.disable();
	    	setLayout(new GridBagLayout());
	        setOpaque(false);
	        JLabel enterName= new JLabel("Enter your player name: ");
	        JLabel enterIP= new JLabel("Server IP: ");
	        JLabel enterPort= new JLabel("Server port: ");
	        JTextField name= new JTextField(20);
	        name.setText("CPU0");
	        JTextField ip= new JTextField(20);
	        ip.setText("127.0.0.1");
	        JTextField port= new JTextField(20);
	        port.setText("2396");
	        JLabel text= new JLabel("BigTwo Game");
	        Font newFont = new Font(
	        		text.getFont().getName(), Font.BOLD, msgArea.getFont().getSize()+10 );
	        text.setFont(newFont);
	        text.setForeground(new Color(50, 50, 225)); 
	        name.selectAll();
	        name.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) {
					ip.requestFocus();
					ip.selectAll();
				}
			});
	        ip.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) {
					port.requestFocus();
					port.selectAll();
				}
			});
	        JButton confirm= new JButton("Confirm");
	        confirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					BigTwoClient client= (BigTwoClient) game;
					client.setPlayerName(name.getText());
					client.setServerIP(ip.getText());
					client.setServerPort( Integer.parseInt(port.getText()) );
					client.makeConnection();
					BigTwoTable.this.enable();
					MyGlassPane.this.setVisible(false);
				}
			});
	        // JButton's actionlistener only registers Left click and Space bar
	        // now press Enter on port number's textfield will also click Confirm
	        port.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) {
					confirm.doClick();
				}
			});
	        GridBagConstraints c= new GridBagConstraints();
	        c.insets = new Insets(10, 10, 10, 10);
			c.ipadx = 10;
			c.ipady = 5;
			c.anchor= GridBagConstraints.EAST;
	        add(text, c);
	        c.gridy=1; add(enterName, c); c.gridx=1; add(name, c);
	        c.gridx=0;
	        c.gridy=2; add(enterIP, c); c.gridx=1; add(ip, c);
	        c.gridx=0;
	        c.gridy=3; add(enterPort, c); c.gridx=1; add(port, c);
	        c.gridy=4;
	        add(confirm, c);
	    }
	    @Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2 = (Graphics2D) g;
	        g2.setColor(new Color(0, 150, 0, 240));
	        g2.fillRect(0, 0, getWidth(), getHeight());
	    }
	}

}