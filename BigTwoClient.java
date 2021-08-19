import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The BigTwoClient class implements the CardGame interface and NetworkGame
 * interface. It is used to model a Big Two card game that supports 4 players
 * playing over the internet.
 * 
 * @author Thomas Ho
 */
public class BigTwoClient implements CardGame, NetworkGame{

	/**
	 * a constructor for creating a Big Two client. You should (i) create 4 players
	 * and add them to the list of players; (ii) create a Big Two table which builds
	 * the GUI for the game and handles user actions; and (iii) make a connection to
	 * the game server by calling the makeConnection() method from the NetworkGame
	 * interface.
	 */
	public BigTwoClient() {
		playerList = new ArrayList<CardGamePlayer>();
		playerList.add(new CardGamePlayer());
		playerList.add(new CardGamePlayer());
		playerList.add(new CardGamePlayer());
		playerList.add(new CardGamePlayer());
		handsOnTable = new ArrayList<Hand>();
		table = new BigTwoTable(this);
		// makeConnection();
		// make connection after user types in their player name and server address
	}

	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID;
	private String playerName;
	private String serverIP;
	private int serverPort;
	private Socket sock;
	private ObjectOutputStream oos;
	private int currentIdx;
	private BigTwoTable table;
	
	private ObjectInputStream ois;
	private boolean gameInProgress;
	private boolean connectionMade;
	
// CardGame interface ---------------
	
	/**
	 * Returns the number of players in this card game.
	 * 
	 * @return the number of players in this card game
	 */
	public int getNumOfPlayers() {
		return playerList.size();
	}

	/**
	 * Returns the deck of cards being used in this card game.
	 * 
	 * @return the deck of cards being used in this card game
	 */
	public Deck getDeck() {
		return deck;
	}

	/**
	 * Returns the list of players in this card game.
	 * 
	 * @return the list of players in this card game
	 */
	public ArrayList<CardGamePlayer> getPlayerList(){
		return playerList;
	}

	/**
	 * Returns the list of hands played on the table.
	 * 
	 * @return the list of hands played on the table
	 */
	public ArrayList<Hand> getHandsOnTable(){
		return handsOnTable;
	}

	/**
	 * Returns the index of the current player.
	 * 
	 * @return the index of the current player
	 */
	public int getCurrentIdx() {
		return currentIdx;
	}

	/**
	 * a method for starting/restarting the game with a given shuffled deck of
	 * cards. You should <br>(i) remove all the cards from the players as well as from
	 * the table; <br>(ii) distribute the cards to the players; <br>(iii) identify the
	 * player who holds the 3 of Diamonds; <br>(iv) set the currentIdx of the
	 * BigTwoClient instance to the playerID (i.e., index) of the player who holds
	 * the 3 of Diamonds; and <br>(v) set the activePlayer of the BigTwoTable instance
	 * to the playerID (i.e., index) of the local player (i.e., only shows the cards
	 * of the local player and the local player can only select cards from his/her
	 * own hand).
	 * 
	 * @param deck the deck of (shuffled) cards to be used in this game
	 */
	public void start(Deck deck) {
		// Clear all cards in hands/table
		for (CardGamePlayer i : playerList)
			i.removeAllCards();
		handsOnTable.clear();
		// Assigning blocks of cards to players
		for (int i = 0; i < 4; ++i) {
			for (int j = 13 * i; j < 13 * (i + 1); ++j)
				playerList.get(i).addCard(deck.getCard(j));
			playerList.get(i).sortCardsInHand();
		}
		// Beginning of game, check for 3 diamond
		for (int i = 0; i < 4; ++i)
			if (playerList.get(i).getCardsInHand().contains(new BigTwoCard(0, 2))) {
				table.changePlayerLabelColorMethod(true, i);
				currentIdx = i;
				table.setActivePlayer(i);
				break;
			}
		table.printMsg(playerList.get(currentIdx).getName() + "'s turn:\n");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {e.printStackTrace();}
		table.setVisible();
	}
	
	/**
	 * a method for making a move by a player with the specified playerID using the
	 * cards specified by the list of indices. This method should be called from the
	 * BigTwoTable when the local player presses either the ¡§Play¡¨ or ¡§Pass¡¨ button.
	 * You should create a CardGameMessage object of the type MOVE, with the
	 * playerID and data in this message being -1 and cardIdx, respectively, and
	 * send it to the game server using the sendMessage() method from the
	 * NetworkGame interface.
	 * 
	 * @param playerID the playerID of the player who makes the move
	 * @param cardIdx  the list of the indices of the cards selected by the player
	 */
	public void makeMove(int playerID, int[] cardIdx) {
		sendMessage( new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx) );
	}

	/**
	 * a method for checking a move made by a player. This method should be called
	 * from the parseMessage() method from the NetworkGame interface when a message
	 * of the type MOVE is received from the game server. The playerID and data in
	 * this message give the playerID of the player who makes the move and a
	 * reference to a regular array of integers specifying the indices of the
	 * selected cards, respectively. These are used as the arguments in calling the
	 * checkMove() method.
	 * 
	 * @param playerID the playerID of the player who makes the move
	 * @param cardIdx  the list of the indices of the cards selected by the player
	 */
	public void checkMove(int playerID, int[] cardIdx) {
		CardGamePlayer player = playerList.get(playerID);
		CardList playerCards = player.play(cardIdx);
		if (!ifLegal(player, cardIdx)) {
			if (cardIdx == null) {
				table.resetYPos(playerID, cardIdx );
				table.printMsg("{Pass}  <== Not a legal move!!!\n");
			}
			else {
				table.resetYPos(playerID, cardIdx );
				table.resetSelected();
				table.printCards(true, false, playerCards);
				table.printMsg( "  <== Not a legal move!!!\n" );
			}
			table.changePlayerLabelColorMethod(false, playerID);
			table.repaint();
			table.printMsg(player.getName()+ "'s turn:\n");
		} else {	// Legal hand
			if (cardIdx == null) {
				table.printMsg("{Pass}\n");
			}
			else {
				Hand playerHand = BigTwoClient.composeHand(player, playerCards);
				table.printMsg("{" + playerHand.getType() + "} ");
				table.printCards(true, false, playerCards);
				table.printMsg("\n");
				table.resetYPos(playerID, cardIdx);
				table.removePos( playerID, cardIdx );
				player.removeCards(playerCards);
				handsOnTable.add(playerHand);
				table.updateLastHandOnTable(player);
			}
			table.repaint();
			// check if current player has no more card
			if (!endOfGame()) {
				// Game continues
				currentIdx = (currentIdx+1)%4;
				table.changePlayerLabelColorMethod(true, currentIdx);
				table.setActivePlayer( currentIdx );
				player = playerList.get(currentIdx);
				table.printMsg(player.getName()+ "'s turn:\n");				
			} else {
				// Game ends
				gameInProgress= false;
				table.printMsg("\nGame ends\n");
				for (CardGamePlayer i : playerList) {
					if (i.getNumOfCards() > 0)
						table.printMsg(i.getName() + " has " + i.getNumOfCards() + " cards in hand.\n");
					else
						table.printMsg(i.getName() + " wins the game.\n");
				}
				table.disable();
				table.gameEndMessage();
			}
		}
	}
	
	/**
	 * Additional method.<br>
	 * This method checks if the card(s) chosen is/are allowed to be played and if
	 * it's legal to pass.
	 * 
	 * @param player the player who play the cards
	 * @param a      an int array whose elements represents selected cards' position
	 *               in the player's hand
	 * @return true if the card(s) chosen is/are allowed to be played or if it's
	 *         legal to pass, false otherwise
	 */
	public boolean ifLegal(CardGamePlayer player, int a[]) {
		if (handsOnTable.size() == 0) { // Player's hand must include 3 diamond or pass the turn
			if (a == null || a.length == 0)
				if ( player.getCardsInHand().contains( new BigTwoCard(0, 2) ) )
					return false;
				else
					return true;
			CardList playerCards = player.play(a);
			if (!playerCards.contains(new BigTwoCard(0, 2)))
				return false;
			if (BigTwoClient.composeHand(player, playerCards) == null)
				return false;
			else
				return true;
		}
		Hand tableHand = handsOnTable.get(handsOnTable.size() - 1);
		// Check if player wants to pass first
		if (a == null || a.length == 0)
			if (player.equals(tableHand.getPlayer()))
				return false;
			else
				return true;
		// check if player's hand is valid
		if (!player.equals(tableHand.getPlayer()) && a.length != tableHand.size())
			return false;
		CardList playerCards = player.play(a);
		Hand playerHand = BigTwoClient.composeHand(player, playerCards);
		if (playerHand == null)
			return false;
		// now player's hand is valid
		// if last hand on table is played by the current player, any valid hand can be
		// played
		if (player.equals(tableHand.getPlayer()))
			return true;
		// check if player's hand beats last hand on table
		return playerHand.beats(tableHand);
	}

	/**
	 * Checks for end of game.
	 * 
	 * @return true if the game ends; false otherwise
	 */
	public boolean endOfGame() {
		if (playerList.get(currentIdx).getCardsInHand().size() == 0)
			return true;
		else
			return false;
	}
	
// NetworkGame interface ---------------	
	
	/**
	 * Returns the playerID (index) of the local player.
	 * 
	 * @return the playerID (index) of the local player
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Sets the playerID (index) of the local player.
	 * 
	 * @param playerID
	 *            the playerID (index) of the local player.
	 */
	public void setPlayerID(int playerID) {
		this.playerID= playerID; 
	}

	/**
	 * Returns the name of the local player.
	 * 
	 * @return the name of the local player
	 */
	public String getPlayerName() {
		return this.playerName;
	}

	/**
	 * Sets the name of the local player.
	 * 
	 * @param playerName
	 *            the name of the local player
	 */
	public void setPlayerName(String playerName) {
		this.playerName= playerName;
	}

	/**
	 * Returns the IP address of the server.
	 * 
	 * @return the IP address of the server
	 */
	public String getServerIP() {
		return serverIP;
	}

	/**
	 * Sets the IP address of the server.
	 * 
	 * @param serverIP
	 *            the IP address of the server
	 */
	public void setServerIP(String serverIP) {
		this.serverIP= serverIP;
	}

	/**
	 * Returns the TCP port of the server.
	 * 
	 * @return the TCP port of the server
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * Sets the TCP port of the server
	 * 
	 * @param serverPort
	 *            the TCP port of the server
	 */
	public void setServerPort(int serverPort) {
		this.serverPort= serverPort;
	}
	
	/**
	 * Lets BigTwoTable confirms if the client has connected to the server.
	 * 
	 * @return true if connected to the server
	 */
	public boolean ifConnected() {
		if (sock == null)
			return false;
		else
			return true;
	}
	
	/**
	 * a method for making a socket connection with the game server. Upon successful
	 * connection, you should <br>(i) create an ObjectOutputStream for sending messages
	 * to the game server; <br>(ii) create a thread for receiving messages from the game
	 * server; <br>(iii) send a message of the type JOIN to the game server, with
	 * playerID being -1 and data being a reference to a string representing the
	 * name of the local player; <br>(iv) send a message of the type READY to the game
	 * server, with playerID and data being -1 and null, respectively.
	 */
	public void makeConnection() {
		if (!connectionMade) {
			connectionMade= true;
			try {
				sock = new Socket(serverIP, serverPort);
				oos= new ObjectOutputStream( new BufferedOutputStream( sock.getOutputStream() ) );
				ois= new ObjectInputStream( new BufferedInputStream( sock.getInputStream() ) );
				Thread readerThread = new Thread(new ServerHandler());
				readerThread.start();
			} catch (Exception ex) {ex.printStackTrace();}
		}
		sendMessage( new CardGameMessage(CardGameMessage.JOIN, -1, playerName) );
		sendMessage( new CardGameMessage(CardGameMessage.READY, -1, null) );
	}

	/**
	 * a method for parsing the messages received from the game server. This method
	 * should be called from the thread responsible for receiving messages from the
	 * game server. Based on the message type, different actions will be carried out
	 * (please refer to the general behavior of the client described in the previous
	 * section).
	 * 
	 * @param message the specified message received from the server
	 */
	public synchronized void parseMessage(GameMessage message) {
		switch (message.getType()) {
			case CardGameMessage.PLAYER_LIST:
				String[] names= (String []) message.getData();
				this.playerID= message.getPlayerID();
				table.setThisPlayerID(playerID);
				for (int i= 0; i< names.length; i++) {
					if (i > 3) break;
					if (names[i] == null) continue;
					playerList.get(i).setName(names[i]);
				}
				break;
			case CardGameMessage.JOIN:
				int i= message.getPlayerID();
				playerList.get(i).setName( (String) message.getData() );
				break;
			case CardGameMessage.FULL:
				table.printMsg("Server is full. Failed to join game.\n");
				sock= null;
				break;
			case CardGameMessage.QUIT:
				int ii= message.getPlayerID();
				table.printMsg(playerList.get(ii).getName() + 
						(String) message.getData() + " has left the game\n");
				playerList.get(message.getPlayerID()).setName("");
				table.disable();
				if (gameInProgress) {
					sendMessage( new CardGameMessage(CardGameMessage.READY, -1, null) );
					gameInProgress= false;
				}
				break;
			case CardGameMessage.READY:
				table.printMsg( playerList.get(message.getPlayerID()).getName() + " is ready.\n" );
				break;
			case CardGameMessage.START:
				table.reset();
				BigTwoDeck deck= (BigTwoDeck) message.getData();
				start(deck);
				table.updateLabel();
				gameInProgress= true;
				table.repaint();
				break;
			case CardGameMessage.MOVE:
				checkMove(message.getPlayerID(), (int []) message.getData());
				break;
			case CardGameMessage.MSG:
				table.printMsg_chat((String) message.getData());
				table.printMsg_chat("\n");
				break;
			default:
				table.printMsg("Wrong message type: " + message.getType()+"\n");
				break;
		}
	}

	/**
	 * a method for sending the specified message to the game server. This method
	 * should be called whenever the client wants to communicate with the game
	 * server or other clients.
	 * 
	 * @param message the specified message to be sent the server
	 */
	public synchronized void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * an inner class that implements the Runnable interface. You should implement
	 * the run() method from the Runnable interface and create a thread with an
	 * instance of this class as its job in the makeConnection() method from the
	 * NetworkGame interface for receiving messages from the game server. Upon
	 * receiving a message, the parseMessage() method from the NetworkGame interface
	 * should be called to parse the messages accordingly.
	 * 
	 * @author Thomas Ho
	 */
	class ServerHandler implements Runnable{
		@Override
		public void run() {
			CardGameMessage message;
			try {
				while ((message = (CardGameMessage) ois.readObject()) != null) {
					parseMessage(message);
				} 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * a method for creating an instance of BigTwoClient
	 * 
	 * @param args not used here.
	 */
	public static void main(String[] args) {
		BigTwoClient game = new BigTwoClient();
	}
	
	/**
	 * a method for returning a valid hand from the specified list of cards of the
	 * player. Returns null is no valid hand can be composed from the specified list
	 * of cards.
	 * 
	 * @param player the player who play the cards
	 * @param cards  A CardList object that contains the cards the player chooses
	 * @return respective Hand subclass object if the cards represent a valid hand;
	 *         null otherwise
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		Hand temp;
		if (cards == null)
			return null;
		if (cards.size() == 1) {
			temp = new Single(player, cards);
			if (temp.isValid())
				return temp;
			else
				return null;
		}
		if (cards.size() == 2) {
			temp = new Pair(player, cards);
			if (temp.isValid())
				return temp;
			else
				return null;
		}
		if (cards.size() == 3) {
			temp = new Triple(player, cards);
			if (temp.isValid())
				return temp;
			else
				return null;
		}
		if (cards.size() == 5) {
			temp = new Straight(player, cards);
			if (!temp.isValid()) {
				temp = new Flush(player, cards);
				if (!temp.isValid()) {
					temp = new FullHouse(player, cards);
					if (!temp.isValid()) {
						temp = new Quad(player, cards);
						if (!temp.isValid()) {
							temp = new StraightFlush(player, cards);
							if (!temp.isValid())
								return null;
							else
								return temp;
						}
						return temp;
					}
					return temp;
				}
				return temp;
			}
			return temp;
		}
		return null;
	}

}
