import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameState.Player;

public class TestDivers {
	public static void main(String[] args) {
		PlayerID id = PlayerID.PLAYER_3;
		SubCell c1 = SubCell.centralSubCellOf(new Cell(1,6));
		SubCell c2 = SubCell.centralSubCellOf(new Cell(2,2));
		SubCell c3 = SubCell.centralSubCellOf(new Cell(4,2));
		SubCell c4 = SubCell.centralSubCellOf(new Cell(1,1));
		List<Player> l = Arrays.asList(new Player(PlayerID.PLAYER_1, 3,c1 , null),
				new Player(PlayerID.PLAYER_2, 3, c2, null),
				new Player(PlayerID.PLAYER_3, 3, c3, null),
				new Player(PlayerID.PLAYER_4, 3, c4, null));
		Comparator<Player> com1 = (p1, p2) -> Integer.compare(p1.getPosition().y(), p2.getPosition().y());
		Comparator<Player> com2 = (p1, p2) -> {
			int i = id.ordinal();
			int a = Math.floorMod(i+1 + p1.getId().ordinal()+1, 4);
			int b = Math.floorMod(i+1 + p2.getId().ordinal()+1, 4);

			return Integer.compare(a, b);

		};
		
		l.sort(com1.thenComparing(com2));
		l.forEach(p->System.out.print(p.getId()+" "));
	}
}
