package edu.cmu.cs.cs214.hw4.core;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestGame {

	private Game g;
	private Player p1;
	private LetterTile lh = new LetterTile('H', 0, null);
	private LetterTile li = new LetterTile('I', 0, null);
	private SpecialTile b = new BoomTile(null);
	private SpecialTile n = new NegativeTile(null);

	@Before
	public void setUp() throws Exception {
		g = new Game(15, 7, "src/main/resources/");
		p1 = new Player("Bob Joji");
	}

	@Test
	public void testPlayer() {
		assertTrue(p1.getScore() == 0);
		assertTrue(p1.getName().equals("Bob Joji"));
		assertFalse(p1.hasLetter('X'));
		assertTrue(p1.getRack().size() == 0);
		assertTrue(p1.getSpecialsList().size() == 0);
		assertTrue(p1.toString().equals("Bob Joji (0)"));
		p1.addToRack(lh);
		p1.addToRack(li);
		assertTrue(p1.getRack().contains(lh));
		assertTrue(p1.hasLetter('H'));
		assertTrue(p1.rackToString().contains("H(0)")
				&& p1.rackToString().contains("I(0)"));
		p1.removeFromRack(lh);
		assertFalse(p1.hasLetter('H'));
		p1.setScore(100);
		assertTrue(p1.getScore() == 100);
		p1.setScore(-100);
		assertTrue(p1.getScore() == -100);
		p1.addSpecial(b);
		p1.addSpecial(n);
		assertTrue(p1.getSpecialsList().size() == 2);
		p1.removeSpecial(b);
		assertTrue(p1.getSpecialsList().size() == 1);
		System.out.println("Passed player test");
	}

	@Test
	public void testGame() {
		assertFalse(g.isTileBagEmpty());
//		assertTrue(g.toString().equals(g.getBoard().toString()));
		assertTrue(g.getPlayerList().size() == 0);
		assertTrue(g.getTurn() == null);
		g.addPlayer("Jon Bovi");
		g.addPlayer("Bon Jovi");
		assertTrue(g.getPlayerList().size() == 2);
//		assertTrue(g.getTurn().getName().equals("Jon Bovi"));
		g.setTurn(1);
		assertTrue(g.getTurn().getName().equals("Bon Jovi"));
		String r = g.getTurn().rackToString();
		assertTrue(r.equals(g.getTurn().rackToString()));
		g.shufflePlayerRack("abcdefghijklmnopqrstuvwxyz"
				+ "1234567890!@#$%^&*()");
		assertFalse(r.equals(g.getTurn().rackToString()));
		g.nextTurn();
		assertTrue(g.getTurn().getName().equals("Jon Bovi"));
		assertTrue(g.getSquare(-1, -1) == null);
		assertTrue(g.getSquare(7, 7) != null);
		assertTrue(g.getSpecialsList().size() > 0);
		for (int i = 0; i < g.getSpecialsList().size(); i++) {
			g.getTurn().setScore(g.getSpecialsList().get(i).getPrice());
			g.buySpecial(g.getSpecialsList().get(i));
		}
		assertTrue(g.getPlayerList().get(0).getScore() == 0);
		g.getPlayerList().get(0).setScore(0);
		g.getPlayerList().get(1).setScore(0);
		assertTrue(g.getSpecialsList().size() > 0);

		Player p = g.getTurn();
		for (int i = 0; i < p.getRack().size(); i++)
			p.removeFromRack(p.getRack().get(i));
		p.addToRack(new LetterTile('H', 1, null));
		p.addToRack(new LetterTile('E', 1, null));
		p.addToRack(new LetterTile('L', 1, null));
		p.addToRack(new LetterTile('L', 1, null));
		p.addToRack(new LetterTile('O', 1, null));
		p.addToRack(new LetterTile('M', 1, null));
		p.addToRack(new BlankTile(null));
		assertTrue(g.getMoves(null).size() ==0);
		assertTrue(g.getMoves("WORLD").size() ==0);
		assertTrue(g.getMoves("E").size() ==0);
		assertTrue(g.getMoves("ME").size() == 4);
		assertTrue(g.getMoves("TO").size() == 4);
		g.insertWord(g.getMoves("TO").get(0));
		assertTrue(g.getSquare(7, 7).getLetter() != null);
		assertTrue(g.getTurn().getScore() == 2);
		g.insertWord(g.getMoves("HELLO").get(0));

		Game g2 = new Game(15, 150, "src/main/resources/");
		g2.addPlayer("test");
		assertTrue(g2.isTileBagEmpty());

		// should print error(s)
		Game g3 = new Game(15, 7, "src/main/nonexistant/");
		System.out.println("Passed game test");
	}

	@Test
	public void testSpecials() {
		// boom!
		Game g4 = new Game(15, 150, "src/main/resources/");
		g4.addPlayer("Test Player");
		assertTrue(g4.isTileBagEmpty());
		for (int i = 0; i < g4.getSpecialsList().size(); i++) {
			g4.getTurn().setScore(g4.getSpecialsList().get(i).getPrice());
			g4.buySpecial(g4.getSpecialsList().get(i));
		}
		SpecialTile s = g4.getTurn().getSpecialsList().get(0);
		g4.addSpecial(s, 7, 7);
		assertTrue(g4.getSquare(7, 7).getSpecials().get(0).equals(s));
		assertTrue(g4.insertWord(g4.getMoves("EXPLODE").get(0)).size()>0);
		assertTrue(g4.getSquare(7, 7).getSpecials().size() == 0);
		assertTrue(g4.getSquare(7, 7).getLetter() == null);

		// negative
		Game g5 = new Game(15, 150, "src/main/resources/");
		g5.addPlayer("Test Player");
		g5.addPlayer("Player 2");
		g5.addPlayer("Player 3");
		for (int i = 0; i < g5.getSpecialsList().size(); i++) {
			g5.getTurn().setScore(g5.getSpecialsList().get(i).getPrice());
			g5.buySpecial(g5.getSpecialsList().get(i));
		}
		s = g5.getTurn().getSpecialsList().get(3);
		g5.addSpecial(s, 7, 7);
		assertTrue(g5.getSquare(7, 7).getSpecials().get(0).equals(s));
//		g5.insertWord(g5.getMoves("AT").get(0));
//		assertTrue(g5.getSquare(7, 7).getSpecials().size() == 0);
//		assertTrue(g5.getTurn().getScore() < 0);
		
		// reverse
		s = g5.getTurn().getSpecialsList().get(1);
		g5.addSpecial(s, 7, 8);
//		g5.insertWord(g5.getMoves("ARK").get(0));
		g5.nextTurn();
//		assertTrue(g5.getTurn().getName().equals("Player 3"));
		
		// shuffle (personal)
		Game g6 = new Game(15, 150, "src/main/resources/");
		g6.addPlayer("Test Player");
		for (int i = 0; i < g6.getSpecialsList().size(); i++) {
			g6.getTurn().setScore(g6.getSpecialsList().get(i).getPrice());
			g6.buySpecial(g6.getSpecialsList().get(i));
		}
		s = g6.getTurn().getSpecialsList().get(2);
		g6.addSpecial(s, 7, 7);
		String r = g6.getTurn().rackToString();
		g6.insertWord(g6.getMoves("ARKS").get(0));
		assertFalse(r.contains(g6.getTurn().rackToString()));
		System.out.println("Passed specials test");
	}
}
