package edu.cmu.cs.cs214.hw4.core;

import java.util.Collections;

/**
 * Reverse tile is a special tile that reverses the order of the players' turn
 * 
 * @author Hizal
 *
 */
public class ReverseTile implements SpecialTile {

	private int price;
	private String description;
	private String icon;
	private Coord loc;
	private Player owner;

	/**
	 * initialize the file, price, description and location
	 * 
	 * @param i
	 *            filepath of the icon to display
	 */
	public ReverseTile(String i) {
		price = 15;
		description = "Reverse the order of players' turns!";
		loc = new Coord(-1, -1);
		icon = i;
	}

	@Override
	public void setLoc(Coord c) {
		loc = c;
	}

	@Override
	public Coord getLoc() {
		return loc;
	}

	@Override
	public String getIcon() {
		return icon;
	}

	@Override
	public String activate(Game game, Move move) {
		Player p = game.getTurn();
		Collections.reverse(game.getPlayerList());
		game.setTurn(game.getPlayerList().indexOf(p));
		return "The player order has been reversed, thanks to "
				+ owner.getName() + "!";
	}

	@Override
	public void setOwner(Player p) {
		owner = p;
	}

	@Override
	public Player getOwner() {
		return owner;
	}

	@Override
	public int getPrice() {
		return price;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return "Reverse";
	}

	@Override
	public String toString() {
		return getName() + " (" + price + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ReverseTile))
			return false;
		if (obj == this)
			return true;
		ReverseTile c = (ReverseTile) obj;
		return (this.toString().equals(c.toString()));
	}

	@Override
	public int hashCode() {
		return 0;
	}
}
