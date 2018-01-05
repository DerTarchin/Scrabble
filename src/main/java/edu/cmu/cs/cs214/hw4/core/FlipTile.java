package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;

/**
 * The Shuffle Tile is a special tile that shuffles all the tiles in the rack of
 * the player that activated the tile. Custom tile by Hizal
 * 
 * @author Hizal
 *
 */
public class FlipTile implements SpecialTile {

	private int price;
	private String description;
	private String icon;
	private Coord loc;
	private Player owner;

	/**
	 * initizalizes the shuffle tile, its price, description and location
	 * 
	 * @param i
	 *            filepath for the icon to display the tile
	 */
	public FlipTile(String i) {
		price = 15;
		description = "Flip the letters placed by the player!";
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
		ArrayList<Coord> temp = new ArrayList<Coord>();
		for (int i = move.size() - 1; i >= 0; i--)
			temp.add(move.get(i).getLoc());
		for (int i = 0; i < move.size(); i++)
			move.get(i).setLoc(temp.get(i));
		return "Your move has been flipped, thanks to " + owner.getName() + "!";
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
		return "Flip";
	}

	@Override
	public String toString() {
		return getName() + " (" + price + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FlipTile))
			return false;
		if (obj == this)
			return true;
		FlipTile c = (FlipTile) obj;
		return (this.toString().equals(c.toString()));
	}

	@Override
	public int hashCode() {
		return 0;
	}
}
