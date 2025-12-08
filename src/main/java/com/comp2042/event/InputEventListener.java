package com.comp2042.event;

import com.comp2042.view.ViewData;

/**
 * Interface defining the contract for handling player input events.
 * Implementations process various input events (movement, rotation, hard drop)
 * and return updated view data or game state information.
 */
public interface InputEventListener {

    /**
     * Handles the down movement event, moving the brick down one cell.
     * May trigger row clearing, score updates, and game state changes.
     * 
     * @param event the move event containing event type and source information
     * @return DownData containing information about cleared rows and updated view data
     */
    DownData onDownEvent(MoveEvent event);

    /**
     * Handles the left movement event, moving the brick left one cell.
     * 
     * @param event the move event containing event type and source information
     * @return ViewData containing the updated brick position and view information
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Handles the right movement event, moving the brick right one cell.
     * 
     * @param event the move event containing event type and source information
     * @return ViewData containing the updated brick position and view information
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Handles the rotation event, rotating the brick counter-clockwise.
     * 
     * @param event the move event containing event type and source information
     * @return ViewData containing the updated brick rotation and view information
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Handles the hard drop event, instantly dropping the brick to its lowest position.
     * May trigger row clearing, score updates, and game state changes.
     * 
     * @param event the move event containing event type and source information
     * @return DownData containing information about cleared rows and updated view data
     */
    DownData onHardDropEvent(MoveEvent event);

    /**
     * Resets the game to start a new game.
     * Clears the board and resets game state.
     */
    void createNewGame();
}
