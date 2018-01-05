package edu.cmu.cs.cs214.hw4.core;

/**
 * boom tile is a special tile that wipes off any letter within a 3x3 square
 * radius of the triggering square,
 * 
 * @author Hizal
 *
 */
public class BoomTile implements SpecialTile {

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
	public BoomTile(String i) {
		price = 25;
		description = "Blow up anything within a 3x3 radius!";
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
		// remove letters in a 3x3 radius, but keep other special tiles intact
		int radius = 3;
		for (int r = loc.getRow() - radius; r <= loc.getRow() + radius; r++)
			for (int c = loc.getCol() - radius; c <= loc.getCol() + radius; c++) {
				if (game.getSquare(r, c) != null)
					game.getSquare(r, c).removeLetter();
				for (int i = 0; i < move.size(); i++)
					if (move.get(i).getLoc().equals(new Coord(r, c)))
						move.remove(i);
			}
		return "You have triggered a bomb, set by " + owner.getName() + "!";
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
		return "Boom!";
	}

	@Override
	public String toString() {
		return getName() + " (" + price + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BoomTile))
			return false;
		if (obj == this)
			return true;
		BoomTile c = (BoomTile) obj;
		return (this.toString().equals(c.toString()));
	}

	@Override
	public int hashCode() {
		return 0;
	}
}
