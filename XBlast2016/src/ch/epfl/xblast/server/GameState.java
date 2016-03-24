/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 * @date 14 mars 2016
 */
package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.Lists;
import ch.epfl.xblast.PlayerID;

public final class GameState {

    private final int ticks;
    private final Board board;
    private final List<Player> players;
    private final List<Bomb> bombs;
    private final List<Sq<Sq<Cell>>> explosions;
    private final List<Sq<Cell>> blasts;
    private final static List<List <PlayerID>> permut = Lists.permutations(Arrays.asList(PlayerID.values()));
    private static final Random RANDOM = new Random(3);

    /**
     * Construit l'état du jeu pour le coup d'horloge, le plateau de jeu, les joueurs, les bombes, les explosions et les particules
     * d'explosion (blasts) donnés.
     * @param ticks
     *          Coup d'horloge actuel
     * @param board
     *          Plateau de jeu actuel
     * @param players
     *          Joueurs (vivant et morts --> toujours une liste de quatre éléments)
     * @param bombs
     *          Bombes posées sur le plateau
     * @param explosions
     *          Explosions "en cours"
     * @param blasts
     *          Particules d'explosion sur le plateau
     * @throws IllegalArgumentException si le coup d'horloge est (strictement) négatif ou si la liste des joueurs
     *          ne contient pas exactement 4 éléments.
     * @throws NullPointerException si l'un des cinq derniers arguments est nul 
     */
    public GameState(int ticks, Board board, List<Player> players,
            List<Bomb> bombs, List<Sq<Sq<Cell>>> explosions,
            List<Sq<Cell>> blasts){

        this.ticks = ArgumentChecker.requireNonNegative(ticks);

        if(players.size() != 4){
            throw new IllegalArgumentException("Le nombre de joueurs doit être de 4");
        }
        this.players = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(players)));
        this.board = Objects.requireNonNull(board);
        this.bombs = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(bombs)));
        this.explosions = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(explosions)));
        this.blasts = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(blasts)));
    }

    /**
     * Construit l'état du jeu pour le plateau et les joueurs donnés,
     * pour le coup d'horloge 0 et aucune bombe, explosion ou particule d'explosion.
     * @param board
     *          Plateau de jeu donné
     * @param players
     *          Liste de joueurs donnée (toujours 4)
     * @throws les mêmes exceptions que le constructeur principal
     */
    public GameState(Board board, List<Player> players) {
        this(0, board,players,null, null, null);
    }

    /**
     * Retourne le coup d'horloge correspondant à l'état
     * @return ticks (int)
     *          Coup d'horloge pour l'état correspondant
     */
    public int ticks() {
        return ticks;
    }

    /**
     * Retourne vrai sii l'état correspond à une partie terminée, c-à-d si le nombre de coups d'horloge d'une partie
     * (Ticks.TOTAL_TICKS) est écoulé, ou s'il n'y a pas plus d'un joueur vivant
     * @return true 
     *          sii le jeu es fini (il ne reste pas plus d'un joueur ou Ticks.TOTAL_TICKS est écoulé)
     */
    public boolean isGameOver() {
        if (Ticks.TOTAL_TICKS == ticks || alivePlayers().size() <= 1) {
            return true;
            }
            return false;
    }

    /**
     * Retourne le temps restant dans la partie, en secondes
     * @return remainingTime (double)
     *          Temps restant avant la fin du jeu
     */
    public double remainingTime() {
        return (Ticks.TOTAL_TICKS - ticks)/ Ticks.TICKS_PER_SECOND;
    }

    /**
     * Retourne l'identité du vainqueur de cette partie s'il y en a un, sinon la valeur optionnelle vide (voir plus bas)
     * @return winner (Optional<PlayerID>)
     *          Identité du dernier joueur vivant ou rien s'il n'y a pas de vainqueur
     */
    public Optional<PlayerID> winner() {
        if (alivePlayers().size() == 1) {
            return  Optional.of(players.get(0).id());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Retourne le plateau de jeu
     * @return board (Board)
     *          Plateau du jeu courant
     */
    public Board board() {
        return board;
    }

    /**
     * Retourne les joueurs, sous la forme d'une liste contenant toujours 4 éléments, car même les joueurs morts en font partie,
     * @return players (List<Player>)
     *          Liste des joueurs
     */
    public List<Player> players() {
        return players;
    }

    /**
     * Retourne les joueurs vivants, c-à-d ceux ayant au moins une vie
     * @return alivePlayers (List<Player>)
     *          List des joueurs vivant (qui ont au moins une vie) au temps t
     */
    public List<Player> alivePlayers() {
        List<Player> alivePlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).isAlive()) {
                alivePlayers.add(players.get(i));
            }
        }
        return alivePlayers;
    }
    
    /**
     * Calcule les particules d'explosion pour l'état suivant étant données celles de l'état courant, le plateau de jeu courant et les explosions courantes.
     * @param blasts0
     *          Particules d'explosion pour l'état actuel
     * @param board0
     *          Plateau de jeu pour l'état actuel
     * @param explosions0
     *          Explosions pour l'état actuel
     * @return nextBlasts (List<Sq<Cell>>)
     *          Liste de particules d'explosion courantes
     */
    private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0, Board board0, List<Sq<Sq<Cell>>> explosions0){
        List<Sq<Cell>> blasts1 = new LinkedList<>();
        for (Sq<Cell> bla: blasts0) {
            if(board0.blockAt(bla.head()).isFree() && !bla.isEmpty()){
                blasts1.add(bla);
            }
        }
        for(Sq<Sq<Cell>> exp: explosions0){
            blasts1.add(exp.head());
        }
        return blasts1;
    }

    /**
     * Retourne une table associant les bombes aux cases qu'elles occupent
     * @return bombedCells (Map<Cell, Bomb>)
     *          Table associant les bombes aux cases qu'elles occupent
     */
    public Map<Cell, Bomb> bombedCells() {
        Map<Cell, Bomb> bombedCells = new HashMap<>();
        for (Bomb b : bombs) {
            bombedCells.put(b.position(), b);
        }
        return bombedCells;
    }

    /**
     * Retourne l'ensemble des cases sur lesquelles se trouve au moins une particule d'explosion
     * @return blastedCells (Set<Cell>)
     *          Ensemble de cases contenant au moins une particule d'explosion
     */
    public Set<Cell> blastedCells() {
        Set<Cell> blastedCells = new HashSet<>();
        for (Sq<Cell> cell : blasts) {
            blastedCells.add(cell.head());
        }
        return blastedCells;
    }

    /**
     * Retourne l'état du jeu pour le coup d'horloge suivant, en fonction de l'actuel et des événements donnés (speedChangeEvents et bombDropEvents)
     * @param speedChangeEvents
     *          Changement(s) de direction effectué(s) par les joueurs
     * @param bombDropEvents
     *          Liste des joueur ayant posé une/des bombe/s
     * @return next (GameState)
     *          Prochain état du jeu
     */
    public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents, Set<PlayerID> bombDropEvents) {
        for (Bomb b : bombs) {
            if (b.fuseLength() == Ticks.BOMB_FUSE_TICKS) {
                bombs.remove(b);
            }
        }
        
        
        GameState nextState = new GameState(ticks + 1, board, alivePlayers(), bombs, explosions, nextBlasts(blasts, board, explosions));
        return nextState;
    }

    /**
     * Calcule le prochain état du plateau en fonction du plateau actuel
     * des bonus consommés par les joueurs et les nouvelles particules d'explosion donnés.
     * @param board0
     *          Plateau actuel
     * @param consumedBonuses
     *          Bonus consommés par les joueurs
     * @param blastedCells1
     *          Nouvelles particules d'explosions
     * @return board1 (Board)
     *          Prochain état du plateau de jeu en fonction des paramètres donnés
     */
    private static Board nextBoard(Board board0, Set<Cell> consumedBonuses, Set<Cell> blastedCells1) {
        List<Sq<Block>> plat = new ArrayList<>();
        
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 15; j++) {
               if (consumedBonuses.contains(board0.blockAt(new Cell(i, j))) && board0.blockAt(new Cell(i, j)).isBonus()) {
                    plat.add(Sq.constant(Block.FREE));
                } else if (blastedCells1.contains(board0.blockAt(new Cell(i, j))) && !board0.blockAt(new Cell(i, j)).isFree() && !board0.blockAt(new Cell(i, j)).isBonus()) {
                    plat.add(Sq.repeat(Ticks.WALL_CRUMBLING_TICKS, Block.CRUMBLING_WALL).concat(Sq.constant(Block.FREE)));
                } else if (blastedCells1.contains(board0.blockAt(new Cell(i, j))) && board0.blockAt(new Cell(i, j)).isBonus()) {
                    if (board0.blocksAt(new Cell(i, j)).findFirst(p -> p == Block.FREE).isFree()) {
                        plat.add(board0.blocksAt(new Cell(i, j)).tail());
                    } else {
                        plat.add(Sq.repeat(Ticks.BONUS_DISAPPEARING_TICKS, board0.blockAt(new Cell(i, j))).concat(Sq.constant(Block.FREE)));
                    }
                } else {
                    plat.add(board0.blocksAt(new Cell(i, j)));
                }
            }
        }
        return new Board(plat);
    }

    /**
     * 
     * @param players0
     * @param playerBonuses
     * @param bombedCells1
     * @param board1
     * @param blastedCells1
     * @param speedChangeEvents
     * @return
     */
    private static List<Player> nextPlayers(List<Player> players0, Map<PlayerID, Bonus> playerBonuses, Set<Cell> bombedCells1,
            Board board1, Set<Cell> blastedCells1, Map<PlayerID, Optional<Direction>> speedChangeEvents) {
        
        return players0;
    }

    /**
     * Calcule les explosions pour le prochain état en fonction des actuelles
     * @param explosions0
     * @return 
     */
    private static List<Sq<Sq<Cell>>> nextExplosions(List<Sq<Sq<Cell>>> explosions0) {
        for (Sq<Sq<Cell>> e : explosions0) {
            e = e.tail();
        }
        return explosions0;
    }

    /**
     * Retourne la liste des bombes nouvellement posées par les joueurs, étant donnés les joueurs actuels,
     * les événements de dépôt de bombes et les bombes actuelles donnés
     * @param players0
     * @param bombDropEvents
     * @param bombs0
     * @return newlyDroppedBombs (List<Bomb>)
     */
    private static List<Bomb> newlyDroppedBombs(List<Player> players0, Set<PlayerID> bombDropEvents, List<Bomb> bombs0) {
        return bombs0;
    }
}
