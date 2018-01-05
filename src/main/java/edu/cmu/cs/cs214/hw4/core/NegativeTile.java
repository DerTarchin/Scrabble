package edu.cmu.cs.cs214.hw4.core;

/**
 * Negative tile is a special tile that subtracts the points earned from the
 * current turn's move, from the score of the player that made the move and
 * activated the tile
 * 
 * @author Hizal
 *
 */
public class NegativeTile implements SpecialTile {

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
	public NegativeTile(String i) {
		price = 30;
		description = "Subtract the points earned during the turn!";
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
		if (move.getMulFactor() > 0)
			move.setMulFactor(move.getMulFactor() * -1);
		return "Your move score is negative, thanks to " + owner.getName()
				+ "!";
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
		return "Negative";
	}

	@Override
	public String toString() {
		return getName() + " (" + price + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof NegativeTile))
			return false;
		if (obj == this)
			return true;
		NegativeTile c = (NegativeTile) obj;
		return (this.toString().equals(c.toString()));
	}

	@Override
	public int hashCode() {
		return 0;
	}
}
