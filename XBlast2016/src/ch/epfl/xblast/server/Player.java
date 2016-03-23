package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.LifeState.State;

public final class Player {
    
    private final PlayerID id;
    private final Sq<LifeState> lifeStates;
    private final Sq<DirectedPosition> directedPos;
    private final int maxBombs;
    private final int bombRange;
    
    /**
     * 1er constructeur
     * @param id
     * @param lifeStates
     * @param directedPos
     * @param maxBombs
     * @param bombRange
     * @throws NullPointerException si l'un des trois premiers arguments est nul
     * @throws IllegalArgumentException si l'un des 2 derniers éléments est négatif
     */
    public Player(PlayerID id, Sq<LifeState> lifeStates, Sq<DirectedPosition> directedPos, int maxBombs, int bombRange){
        this.id = Objects.requireNonNull(id);
        this.lifeStates = Objects.requireNonNull(lifeStates);
        this.directedPos = Objects.requireNonNull(directedPos);
        if(maxBombs < 0 || bombRange <0){
            throw new IllegalArgumentException("Le nombre maximum de bombes et la portée doivent être positif");
        }
        this.maxBombs = maxBombs;
        this.bombRange = bombRange;
    }
    
    /**
     * 2ème constructeur
     * @param id
     * @param lives
     * @param position
     * @param maxBombs
     * @param bombRange
     */
    public Player(PlayerID id, int lives, Cell position, int maxBombs, int bombRange){
        this(id, SqCreator(lives),
             DirectedPosition.stopped(new DirectedPosition(SubCell.centralSubCellOf(position), Direction.N)),
             maxBombs, 
             bombRange);
        
        if(lives < 0){
            throw new IllegalArgumentException("Le nombre de vie est negatif");
        }
    }
    
    /**
     * @return id
     */
    public PlayerID id(){
        return id;
    }
    /**
     * Retourne la séquence des couples (nombre de vies, état) du joueur
     * @return Sq<LifeState>
     */
    public Sq<LifeState> lifeStates(){
        return lifeStates;
    }
    
    /**
     * Retourne le couple (nombre de vies, état) actuel du joueur
     * @return LifeState
     */
    public LifeState lifeState(){
        return lifeStates.head();
    }
    
    /**
     * retourne la séquence d'états pour la prochaine vie du joueur, qui commence par une période de longueur Ticks.PLAYER_DYING_TICKS
     * durant laquelle le joueur est mourant, suivie soit d'un état de mort permanent si le joueur n'a plus de vie,
     * soit d'une période d'invulnérabilité de longueur Ticks.PLAYER_INVULNERABLE_TICKS suivie d'une période de vulnérabilité permanente, 
     * avec une vie en moins qu'actuellement
     * @return Sq<LifeStates>
     */
    public Sq<LifeState> statesForNextLife(){
        Sq<LifeState> nextState = Sq.repeat(Ticks.PLAYER_DYING_TICKS, new LifeState(lives(), State.DYING));
        if(!isAlive()){
            return nextState.concat(SqCreator(lives()));
        }
        else{
            return nextState.concat(SqCreator(lives()-1));
        }
    }
    
    /**
     * 
     * @return lives
     */
    public int lives(){
        return lifeStates.head().lives();
    }
    
    /**
     * Retourne vrai si le joueur est vivant et faux s'il est mort
     * @return boolean
     */
    public boolean isAlive(){
        if(lives() > 0){
            return true;
        }
        return false;
    }
    
    /**
     * 
     * @return Sq<DirectedPosition>
     */
    public Sq<DirectedPosition> directedPositions(){
        return directedPos;
    }
    
    /**
     * Retourne la position actuelle du joueur
     * @return SubCell
     */
    public SubCell position(){
        return directedPos.head().position();
    }
    
    /**
     * Retourne la direction vers laquelle le joueur regarde actuellement
     * @return
     */
    public Direction direction(){
        return directedPos.head().direction;
    }
    
    /**
     * 
     * @return int
     */
    public int maxBombs(){
        return maxBombs;
    }
    
    /**
     * Retourne un joueur identique à celui auquel on l'applique, si ce n'est que son nombre maximum de bombes est celui donné
     * @param newMaxBombs
     * @return Player
     */
    public Player withMaxBombs(int newMaxBombs){
        return new Player(id, lifeStates, directedPos, newMaxBombs,bombRange);
    }
    
    /**
     * 
     * @return int
     */
    public int bombRange(){
        return bombRange;
    }
    /**
     * Retourne un joueur identique à celui auquel on l'applique, si ce n'est que la portée de ses bombes est celle donnée
     * @param newBombRange
     * @return Player 
     */
    public Player withBombRange(int newBombRange){
        return new Player(id, lifeStates, directedPos, maxBombs, newBombRange);
    }
    
    /**
     * Retourne une nouvelle bombe correspondant au personnage
     * @return
     */
    public Bomb newBomb(){
        return new Bomb(id, position().containingCell(), Ticks.BOMB_FUSE_TICKS, bombRange);
    }
    
    private static Sq<LifeState> SqCreator(int lives){
        if(lives == 0){
            return Sq.constant(new LifeState(lives, State.DEAD));
        }
        else{
            Sq<LifeState> invuln = Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, new LifeState(lives, State.INVULNERABLE));
            LifeState vulState = new LifeState(lives, State.VULNERABLE);
            Sq<LifeState> vuln = Sq.constant(vulState); 
            return invuln.concat(vuln);
        }
        
    }

    public static final class LifeState{
        private final int lives;
        private final State state;
        /**
         * Construit le couple (nombre de vies, état) avec les valeurs données
         * @param lives
         * @param state
         * @throws IllegalArgumentException si le nombre de vies est (strictement) négatif
         * @throws NullPointerException si l'état est nul
         */
        public LifeState(int lives, State state){
            
            if(lives >= 0){
                this.lives = lives;
            }
            else{
                throw new IllegalArgumentException("Le nombre de vie doit être positif!");
            }
            this.state = Objects.requireNonNull(state);
        }
        /**
         * Retourne le nombre de vies du couple
         * @return lives 
         */
        public int lives(){
            return lives;
        }
        /**
         * Retourne l'état
         * @return state
         */
        public State state(){
            return state;
        }
        /**
         * Retourne vrai si et seulement si l'état permet au joueur de se déplacer, ce qui est le cas uniquement s'il est invulnérable ou vulnérable.
         * @return boolean
         */
        public boolean canMove(){
            if(state.equals(State.INVULNERABLE)|| state.equals(State.VULNERABLE)){
                return true;
            }
            return false;
        }
        
        public enum State{
            INVULNERABLE, VULNERABLE, DYING, DEAD;
        }
    }
    
    public static final class DirectedPosition{
        private final SubCell position;
        private final Direction direction;
        
        public DirectedPosition(SubCell position, Direction direction){
            this.position = Objects.requireNonNull(position);
            this.direction = Objects.requireNonNull(direction);
        }
        
        /**
         * retourne une séquence infinie composée uniquement de la position dirigée donnée et représentant un joueur arrêté dans cette position
         * @param p
         * @return 
         */
        public static Sq<DirectedPosition> stopped(DirectedPosition p){
            return Sq.constant(p);        
        }
        
        /**
         * retourne une séquence infinie de positions dirigées représentant un joueur se déplaçant dans la direction dans laquelle il regarde;
         * le premier élément de cette séquence est la position dirigée donnée, 
         * le second a pour position la sous-case voisine de celle du premier élément dans la direction de regard, et ainsi de suite.
         * @return
         */
        public static Sq<DirectedPosition> moving(DirectedPosition p){
            return Sq.iterate(p, x->x.withPosition(x.position().neighbor(x.direction())));
            
        }
        
        /*méthodes de consultation et de dérivation*/
        
        public SubCell position(){
            return position;
        }
        
        public DirectedPosition withPosition(SubCell newPosition){
            return new DirectedPosition(newPosition, direction);
        }
        
        public Direction direction(){
            return direction;
        }
        
        public DirectedPosition withDirection(Direction newDirection){
            return new DirectedPosition(position, newDirection);
        }
        
        
    }
 
}
