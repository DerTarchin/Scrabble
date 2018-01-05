package edu.cmu.cs.cs214.hw4.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestTiles {
	private Coord c;
	private Tile l1;
	private Tile b1;
	private SpecialTile r1;
	private SpecialTile s1;
	private SpecialTile b2;
	private SpecialTile n1;
	private String iconPath;

	@Before
	public void setUp() throws Exception {
		iconPath = "path/to/file.png";

		c = new Coord(10, 12);

		l1 = new LetterTile('x', 10, null);
		b1 = new BlankTile(null);
		r1 = new ReverseTile(iconPath);
		s1 = new ShuffleTile(iconPath);
		b2 = new BoomTile(iconPath);
		n1 = new NegativeTile(iconPath);
	}

	@Test
	public void testCoord() {
		assertTrue(c.getCol() == 12);
		assertTrue(c.getRow() == 10);
		assertFalse(c.equals(l1));
		assertTrue(c.equals(c));
		assertTrue(c.equals(new Coord(10, 12)));
		assertTrue(c.toString().equals("(10,12)"));
		Coord c2 = new Coord(-2, 2);
		assertTrue(c2.toString().equals("(-,-)"));
		System.out.println("Passed coord test");
	}

	@Test
	public void testTile() {
		// letterTile
		assertTrue(l1.getIcon() == null);
		l1.setIcon(iconPath);
		assertTrue(l1.getIcon().equals(iconPath));
		assertTrue(l1.getChar() == 'x');
		assertTrue(l1.getValue() == 10);
		assertTrue(l1.getLoc().toString().equals("(-,-)"));
		l1.setLoc(c);
		assertTrue(l1.getLoc() == c && l1.getLoc().getRow() == c.getRow());
		assertTrue(l1.toString().equals("x[10](10,12)"));
		LetterTile l2 = new LetterTile('x', 10, null);
		l2.setLoc(c);
		assertFalse(l1.equals(n1));
		assertTrue(l1.equals(l1));
		assertTrue(l1.equals(l2));

		// blankTile
		assertTrue(b1.getIcon() == null);
		b1.setIcon(iconPath);
		assertTrue(b1.getIcon().equals(iconPath));
		assertTrue(b1.getChar() == '_');
		assertTrue(b1.getValue() == 0);
		assertTrue(b1.getLoc().toString().equals("(-,-)"));
		b1.setLoc(c);
		assertTrue(b1.getLoc() == c && l1.getLoc().getRow() == c.getRow());
		assertTrue(b1.toString().equals("_[0](10,12)"));
		BlankTile b2 = new BlankTile(null);
		b2.setLoc(c);
		assertFalse(b1.equals(l1));
		assertTrue(b1.equals(b1));
		assertTrue(b1.equals(b2));

		System.out.println("Passed tile test");
	}

	@Test
	public void testSpecialTile() {
		Coord neg = new Coord(-1, -1);
		Player p = new Player("bob");

		// negativeTile
		assertTrue(n1.getLoc().equals(neg));
		assertTrue(n1.getPrice() == 30);
		n1.setLoc(c);
		assertTrue(n1.getLoc().equals(c));
		assertTrue(n1.getIcon().equals(iconPath));
		assertTrue(n1.getOwner() == null);
		n1.setOwner(p);
		assertTrue(n1.getOwner() == p);
		assertTrue(n1.getDescription() != null
				&& n1.getDescription().length() > 0);
		assertTrue(n1.getName().equals("Negative"));
		assertTrue(n1.toString().equals("Negative (30)"));
		assertTrue(n1.equals(n1));
		assertFalse(n1.equals(b1));
		assertTrue(n1.equals(new NegativeTile(null)));
		// activate() will be tested with Game tests

		// shuffleTile
		assertTrue(s1.getLoc().equals(neg));
		assertTrue(s1.getPrice() == 10);
		s1.setLoc(c);
		assertTrue(s1.getLoc().equals(c));
		assertTrue(s1.getIcon().equals(iconPath));
		assertTrue(s1.getOwner() == null);
		s1.setOwner(p);
		assertTrue(s1.getOwner() == p);
		assertTrue(s1.getDescription() != null
				&& s1.getDescription().length() > 0);
		assertTrue(s1.getName().equals("Shuffle"));
		assertTrue(s1.toString().equals("Shuffle (10)"));
		assertTrue(s1.equals(s1));
		assertFalse(s1.equals(b1));
		assertTrue(s1.equals(new ShuffleTile(null)));
		// activate() will be tested with Game tests

		// reverseTile
		assertTrue(r1.getLoc().equals(neg));
		assertTrue(r1.getPrice() == 15);
		r1.setLoc(c);
		assertTrue(r1.getLoc().equals(c));
		assertTrue(r1.getIcon().equals(iconPath));
		assertTrue(r1.getOwner() == null);
		r1.setOwner(p);
		assertTrue(r1.getOwner() == p);
		assertTrue(r1.getDescription() != null
				&& r1.getDescription().length() > 0);
		assertTrue(r1.getName().equals("Reverse"));
		assertTrue(r1.toString().equals("Reverse (15)"));
		assertTrue(r1.equals(r1));
		assertFalse(r1.equals(b1));
		assertTrue(r1.equals(new ReverseTile(null)));
		// activate() will be tested with Game tests

		// boomTile
		assertTrue(b2.getLoc().equals(neg));
		assertTrue(b2.getPrice() == 25);
		b2.setLoc(c);
		assertTrue(b2.getLoc().equals(c));
		assertTrue(b2.getIcon().equals(iconPath));
		assertTrue(b2.getOwner() == null);
		b2.setOwner(p);
		assertTrue(b2.getOwner() == p);
		assertTrue(b2.getDescription() != null
				&& b2.getDescription().length() > 0);
		assertTrue(b2.getName().equals("Boom!"));
		assertTrue(b2.toString().equals("Boom! (25)"));
		assertTrue(b2.equals(b2));
		assertFalse(b2.equals(b1));
		assertTrue(b2.equals(new BoomTile(null)));
		// activate() will be tested with Game tests

		System.out.println("Passed special tile test");

	}
}
