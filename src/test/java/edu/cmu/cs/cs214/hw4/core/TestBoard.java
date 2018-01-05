package edu.cmu.cs.cs214.hw4.core;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestBoard {

	private Dictionary d;
	private Coord c = new Coord(7, 7);
	private LetterTile lc = new LetterTile('C', 3, null);
	private LetterTile la = new LetterTile('A', 1, null);
	private LetterTile lt = new LetterTile('T', 1, null);
	private LetterTile lr = new LetterTile('R', 1, null);
	private LetterTile lg = new LetterTile('G', 4, null);
	private Move letters = new Move();
	private ArrayList<Character> blanks = new ArrayList<Character>();
	private SpecialTile s1 = new BoomTile(null);
	private SpecialTile s2 = new NegativeTile(null);
	private Square s;
	private Board b;

	@Before
	public void setUp() throws Exception {
		s = new Square();
		b = new Board(15, "src/main/resources/");
		d = new Dictionary("src/main/resources/words.txt");
		letters.add(lc);
		letters.add(la);
		letters.add(lt);
	}

	@Test
	public void testDictionary() {
		assertTrue(d.isWord("yellow"));
		assertFalse(d.isWord("DerTarchin"));
		Dictionary d2 = new Dictionary("src/main/nonexistant/words.txt");
		// should print error
		assertFalse(d2.isWord("yellow"));
		System.out.println("Passed dictionary test");
	}

	@Test
	public void testSquare() {
		assertTrue(s.getScoreModifier() == null);
		assertTrue(s.getLetter() == null);
		assertTrue(s.toString().equals("- "));
		assertTrue(s.updateScore(12, "letter") == 12);
		s.setLetter(la);
		assertTrue(s.getLetter().equals(la));
		assertTrue(s.toString().equals("A "));
		s.removeLetter();
		assertTrue(s.getLetter() == null);
		s.setScoreModifier("2xl");
		assertTrue(s.toString().equals("1 "));
		assertTrue(s.updateScore(12, "word") == 12);
		assertTrue(s.updateScore(12, "letter") == 24);
		s.setScoreModifier("3xl");
		assertTrue(s.updateScore(12, "letter") == 36);
		assertTrue(s.toString().equals("2 "));
		s.setScoreModifier("2xw");
		assertTrue(s.updateScore(1, "word") == 2);
		assertTrue(s.toString().equals("3 "));
		s.setScoreModifier("3xw");
		assertTrue(s.updateScore(1, "word") == 3);
		assertTrue(s.toString().equals("4 "));
		assertTrue(s.getSpecials().size() == 0);
		s.addSpecial(s1);
		assertTrue(s.getSpecials().size() == 1);
		assertTrue(s.toString().equals("# "));
		// will test activate with Game

		System.out.println("Passed square test");
	}

	@Test
	public void testBoard() {
		assertTrue(b.toString().contains("- ") && b.toString().contains("1 "));
		assertFalse(b.contains(la));
		assertTrue(b.isEmpty());
		assertTrue(b.getWordScore(new Coord(1, 1),"horizontal", "horizontal") == 0);
		assertTrue(b.getWordScore(new Coord(-1, 1),"horizontal", "vertical") == 0);
		assertTrue(b.getWordScore(new Coord(1, 1),"horizontal", "hoax") == 0);
		assertFalse(b.hasNeighbor(c)[0]);
		assertFalse(b.hasNeighbor(c)[1]);
		assertTrue(b.getSquare(-1, 10) == null);
		assertTrue(b.getSquare(10, 16) == null);
		assertFalse(b.getSquare(7, 7) == null);
		assertTrue(b.getPossibilities(null, null).size() == 0);
		assertTrue(b.getPossibilities(new Move(), null).size() == 0);
		Move single = new Move();
		single.add(la);
		assertTrue(b.getPossibilities(single, null).size() == 0);
//		assertTrue(b.getPossibilities(letters, blanks, 0).size() == 6);
		for (int i = 0; i < letters.size(); i++)
			letters.get(i).setLoc(new Coord(7 + i, 7));
		b.insertWord(letters, 1);
		assertFalse(b.isEmpty());
		assertTrue(b.contains(lc));
		assertTrue(b.contains(la));
		assertTrue(b.contains(lt));
		assertTrue(b.hasNeighbor(c)[0]);
		assertTrue(b.getWordScore(c,"vertical", "vertical") == 10);
		letters.clear();
		letters.add(lr);
		letters.add(la);
		letters.add(lg);
		blanks.add('A');
//		assertTrue(b.getPossibilities(letters, blanks, 1).size() == 4);
//		assertTrue(b.getPossibilities(letters, blanks, 0).size() == 1);
		for (int i = 0; i < letters.size(); i++)
			letters.get(i).setLoc(new Coord(8, 6 + i));
		b.insertWord(letters, 2);
		assertTrue(b.hasNeighbor(new Coord(8, 7))[1]);
		letters.clear();
		letters.add(lg);
		letters.add(lt);
//		assertTrue(b.getPossibilities(letters, blanks, 0).size() == 0);
		letters.clear();
		letters.add(lc);
		letters.add(la);
		letters.add(lt);
		letters.add(new LetterTile('S', 1, null));
		System.out.println(b);
//		System.out.println(b.getPossibilities(letters, blanks, 0));
//		assertTrue(b.getPossibilities(letters, blanks, 0).size() == 1);
		letters.remove(lc);
		letters.remove(la);
		letters.remove(lt);
		blanks.clear();
//		assertTrue(b.getPossibilities(letters, blanks, 0).size() >= 1);
		blanks.add('S');
//		assertTrue(b.getPossibilities(letters,blanks,1).size() >= 1);
		// should print error
		Board badBoard = new Board(15, "src/main/nonexistant/");

		System.out.println("Passed board test");
	}
}
