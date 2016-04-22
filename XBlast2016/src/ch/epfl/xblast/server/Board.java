/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 * @date 29 févr. 2016
 */

package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;

public class Board {

	private final List<Sq<Block>> plateau;

	public Board(List<Sq<Block>> blocks){
		if(blocks.size() != Cell.COUNT){
			throw new IllegalArgumentException("Argument invalide: nombre de blocs différent de 195 nombre de blocks obtenus :" + blocks.size());
		}
		plateau = Collections.unmodifiableList(new ArrayList<>(blocks));
	}

	/**
	 * Méthode public qui construit un plateau constant avec la matrice de blocs donnée
	 * @param rows 
	 *          Liste de Blocks utilisées pour construire le plateau
	 * @return Board
	 *          Plateau construit avec la liste de blocks données
	 * @throws IllegalArgumentException si la liste reçue n'est pas constituée de 13 listes de 15 éléments chacune
	 */
	public static Board ofRows(List<List<Block>> rows){
		checkBlockMatrix(rows, Cell.ROWS, Cell.COLUMNS);

		List<Sq<Block>> platEnCstr = new ArrayList<>();
		for (int i = 0; i < rows.size(); i++) {
			for (int j = 0; j < rows.get(i).size(); j++) {
				platEnCstr.add(Sq.constant(rows.get(i).get(j)));
			}
		}

		return new Board(platEnCstr);
	}

	/**
	 * Construit un plateau murÃ© avec les blocs intÃ©rieurs donnÃ©s.
	 * Lève l'exception 
	 * @param innerBlocks
	 *          Liste des blocks interieurs du plateau de jeu
	 * @return Board
	 *          Plateau de jeu construit à partir de la liste de blocks données
	 * @throws IllegalArgumentException si la liste reçue n'est pas constituée de 11 listes de 13 éléments chacune
	 */
	public static Board ofInnerBlocksWalled(List<List<Block>> innerBlocks) {
		checkBlockMatrix(innerBlocks, 11, 13);

		List<List<Block>> platEnCstr = new ArrayList<>();

		for (int i = 0; i < innerBlocks.size(); i++) {
			platEnCstr.add(new ArrayList<>());
			platEnCstr.get(i).add(Block.INDESTRUCTIBLE_WALL);
			for (int j = 0; j < innerBlocks.get(i).size(); j++) {
				platEnCstr.get(i).add(innerBlocks.get(i).get(j));
			} 
			platEnCstr.get(i).add(Block.INDESTRUCTIBLE_WALL);
		}

		List<Block> walls = Collections.nCopies(platEnCstr.get(0).size(), Block.INDESTRUCTIBLE_WALL);
		platEnCstr.add(0, new ArrayList<>(walls));
		platEnCstr.add(new ArrayList<>(walls));

		return ofRows(platEnCstr);
	}

	/**
	 * Construit un plateau muré symétrique avec les blocs du quadrant nord-ouest donnés. Le plateau est séparé en quatre parties symétriques
	 * @param quadrantNWBlocks
	 *          Liste des blocks dans le quadrant nord-ouest du plateau de jeu, utilisée pour construire tout le plateau
	 * @return Board
	 *          Plateau de jeu construit à partir de la liste donnée
	 * @throws IllegalArgumentException si la liste reÃ§ue n'est pas constituÃ©e de 6 listes de 7 Ã©lÃ©ments chacune.
	 * @author Eleonore Pochon(262969)
	 */

	public static Board ofQuadrantNWBlocksWalled(
			List<List<Block>> quadrantNWBlocks) {

		List<List<Block>> walledSymetricBoard = new ArrayList<List<Block>>();

		checkBlockMatrix(quadrantNWBlocks,
				(int) Math.ceil((Cell.ROWS - 2) / 2.0),
				(int) Math.ceil((Cell.COLUMNS - 2) / 2.0));

		for (List<Block> list : quadrantNWBlocks) {
			walledSymetricBoard.add(Lists.mirrored(list));
		}
		walledSymetricBoard = Lists.mirrored(walledSymetricBoard);

		return ofInnerBlocksWalled(walledSymetricBoard);

	}

	/**
	 * Test si la matrice donnée contient rows lignes et columns colonnes
	 * @param matrix
	 *          Matrice donnée (à tester)
	 * @param rows
	 *          Nombre de ligne attendues
	 * @param columns
	 *          Nombre de colonnes attendues
	 * @throws IllegalArgumentException si la matrice (liste de liste) donnée ne contient pas rows éléments, contenant chacun columns blocs
	 * @author Eleonore Pochon(262969)
	 */
	
	public static void checkBlockMatrix(List<List<Block>> matrix, int rows, int columns) {
		if (matrix == null || matrix.isEmpty() || matrix.size() != rows) {
			throw new IllegalArgumentException("matrix is incorrect");
		}
		for (List<Block> row : matrix) {
			if (row.size() != columns) {
				throw new IllegalArgumentException("matrix is incorrect");
			}
		}
	}


	/**
	 * Retourne la séquence des blocs pour la case donnée
	 * @param c
	 *          Case donnée contenant une séquance de blocks
	 * @return Sq<Block>
	 *          Séquance de blocks contenus dans la case données
	 */
	public Sq<Block> blocksAt(Cell c){
		int index = c.rowMajorIndex();
		return plateau.get(index);
	}

	/**
	 * Retourne le bloc pour la case donnée, c-à-d la tête de la séquence retournée par la méthode précédente
	 * @param c
	 *          Case donnée contenant
	 * @return Block
	 */
	public Block blockAt(Cell c){
		int index = c.rowMajorIndex();
		return plateau.get(index).head();
	}
}
