/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Eleonore Pochon (262959)
 */
package ch.xblast.client;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.peer.KeyboardFocusManagerPeer;
import java.util.Map;
import java.util.function.Consumer;
import ch.epfl.xblast.PlayerAction;

public class KeyboardEventHandler implements KeyListener, KeyboardFocusManagerPeer {

	private Map<Integer, PlayerAction> kb;
	private Consumer<PlayerAction> c;

	/**
	 * Construct the key board handler in function of an associative table with
	 * the player's actions associated to the key of the keyboard and the
	 * action's players consumer
	 * 
	 * @param kb
	 *            the given associative table
	 * @param c
	 *            the given action's players consumer
	 */
	public KeyboardEventHandler(Map<Integer, PlayerAction> kb, Consumer<PlayerAction> c) {
		this.kb = kb;
		this.c = c;
	}

	@Override
	public void setCurrentFocusedWindow(Window win) {

	}

	@Override
	public Window getCurrentFocusedWindow() {
		return null;
	}

	@Override
	public void setCurrentFocusOwner(Component comp) {

	}

	@Override
	public Component getCurrentFocusOwner() {
		return null;
	}

	@Override
	public void clearGlobalFocusOwner(Window activeWindow) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (kb.containsKey(e.getKeyCode())) {
			c.accept(kb.get(e.getKeyCode()));
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
