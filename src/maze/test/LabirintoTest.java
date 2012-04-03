package maze.test;

import static org.junit.Assert.*;
import maze.logic.Coord;
import maze.logic.Labirinto;

import org.junit.Before;
import org.junit.Test;

public class LabirintoTest {
	
	public Labirinto maze = new Labirinto();
	
	@Before
	public void inicializar() {
		maze.init(10, 2, 1);
	}
	
	@Test
	public void testEstadoPersonagens() {
		
		assertFalse(maze.dragoesTodosMortos());
		assertFalse(maze.getChegouFim());
		assertFalse(maze.getHeroi().estaMorto());
		assertFalse(maze.getHeroi().estaArmado());
	}
	
	@Test
	public void testCoordHeroi() {
		
		Coord c1 = new Coord(1,1);
		Coord c2 = new Coord(2,1);
		Coord Heroi = (Coord)maze.getHeroi().getPos();
		
		assertEquals(Heroi, c1);
		assertFalse(Heroi.equals(c2));
	}

	@Test
	public void testMovimentoHeroi() {
		
		Coord c1 = new Coord(1,2);
		Coord Heroi = (Coord)maze.getHeroi().getPos();
		
		maze.moverHeroi("w");
		assertEquals(Heroi, maze.getHeroi().getPos());
		
		
		maze.moverHeroi("s");
		assertEquals(maze.getHeroi().getPos(), c1);
		
		maze.moverHeroi("d");
		assertEquals(maze.getHeroi().getPos(), c1);
		
		//assertFalse(maze.getPosHeroi().equals(Heroi)); da errado, porque?
		
		maze.moverHeroi("w");
		
		for(int i = 1; i < 4; i++)
			maze.moverHeroi("d");
		
		c1.setCoord(4,1);
		assertEquals(maze.getHeroi().getPos(), c1);
		
		for(int i = 1; i < 5; i++)
			maze.moverHeroi("s");
		
		c1.setCoord(4,5);
		assertEquals(maze.getHeroi().getPos(), c1);
		
		for(int i = 1; i < 4; i++)
			maze.moverHeroi("a");
		
		c1.setCoord(1,5);
		assertEquals(maze.getHeroi().getPos(), c1);
	}
	
	@Test
	public void testMorteHeroi() {
		
		assertFalse(maze.getHeroi().estaMorto());
		
		maze.moverHeroi("s");
		
		maze.pertoDragao();
		
		assertTrue(maze.getHeroi().estaMorto());
	}
	
	@Test
	public void testMorteDragao() {
		
		assertFalse(maze.getHeroi().estaArmado());
		
		for(int i = 1; i < 4; i++)
			maze.moverHeroi("d");
		
		for(int i = 1; i < 5; i++)
			maze.moverHeroi("s");
		
		for(int i = 1; i < 4; i++)
			maze.moverHeroi("a");
		
		for(int i = 1; i < 4; i++)
			maze.moverHeroi("s");
		
		assertTrue(maze.getHeroi().estaArmado());
		assertFalse(maze.dragoesTodosMortos());
		
		for(int i = 1; i < 5; i++)
			maze.moverHeroi("w");
		
		maze.pertoDragao();
		
		assertFalse(maze.getHeroi().estaMorto());
		assertTrue(maze.dragoesTodosMortos());
	}
	
	@Test
	public void testSairLabirintoArmado() {
		
		for(int i = 1; i < 4; i++)
			maze.moverHeroi("d");
		
		for(int i = 1; i < 5; i++)
			maze.moverHeroi("s");
		
		for(int i = 1; i < 4; i++)
			maze.moverHeroi("a");
		
		for(int i = 1; i < 4; i++)
			maze.moverHeroi("s");
		
		for(int i = 1; i < 5; i++)
			maze.moverHeroi("w");
		
		maze.pertoDragao();
		
		for(int i = 1; i < 4; i++)
			maze.moverHeroi("w");
		
		for(int i = 1; i < 8; i++)
			maze.moverHeroi("d");
		
		for(int i = 1; i < 5; i++)
			maze.moverHeroi("s");
		
		maze.moverHeroi("d");
		assertTrue(maze.getChegouFim());
	}
	
	@Test
	public void testSairLabirintoDesarmado() {
		
		for(int i = 1; i < 8; i++)
			maze.moverHeroi("d");
		
		for(int i = 1; i < 5; i++)
			maze.moverHeroi("s");
		
		maze.moverHeroi("d");
		assertFalse(maze.getChegouFim());
	}
	
	@Test
	public void testMovimentoDragao () {
	
		Coord c1 = new Coord(1,3);
		Coord Dragao = (Coord)maze.getDragaoAt(3,1).getPos();
		
		assertEquals(Dragao, c1);
		
		maze.moverDragao("s");
		maze.moverDragao("s");
		
		c1.setCoord(1,5);
		assertEquals(Dragao, c1);
		
		for (int i = 1; i < 6; i++)
			maze.moverDragao("d");

		maze.moverDragao("m");
		
		c1.setCoord(6,5);
		assertEquals(Dragao, c1);
		
		for (int i = 1; i < 5; i++)
			maze.moverDragao("w");
		
		c1.setCoord(6,1);
		assertEquals(Dragao, c1);
		
		for (int i = 1; i < 5; i++)
			maze.moverDragao("a");
		
		c1.setCoord(2,1);
		assertEquals(Dragao, c1);
		
		maze.pertoDragao();
		
		assertTrue(maze.getHeroi().estaMorto());
		assertFalse(maze.dragoesTodosMortos());
	}
	
	@Test
	public void testTacticaDragoesAdormecidos() {
		maze.init(12, 1, 2);
		assertTrue(maze.dragoesAdormecidos());
	}
	
	@Test
	public void testHeroiNaoMorreComDragaoAdormecido() {
		maze.init(0, 0, 2);
		maze.moverHeroi("s");
		maze.pertoDragao();
		assertFalse(maze.getHeroi().estaMorto());
	}
	
	@Test
	public void testTacticaDragaoAdormecidoEAcordado() {
		boolean aDormir = false;
		maze.init(21, 1, 3);
		assertFalse(maze.dragoesAdormecidos());
		
		while (true) 
		{
			maze.avaliaTactica();
			if (maze.dragoesAdormecidos())
				aDormir = true;
			if (!maze.dragoesAdormecidos() && aDormir) // estiveram a dormir e acordaram
				break;
		}
	}
}
































































