package com.comp2042.event;

/**
 * Represents a move event in the game.
 * Contains information about the type of movement (direction, rotation, etc.)
 * and the source that triggered the event (user input or game loop).
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructs a MoveEvent with the specified type and source.
     * 
     * @param eventType the type of move event (LEFT, RIGHT, DOWN, ROTATE, etc.)
     * @param eventSource the source of the event (USER or THREAD)
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Gets the type of this move event.
     * 
     * @return the EventType indicating what kind of move this is
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Gets the source of this move event.
     * 
     * @return the EventSource indicating who/what triggered this event
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}
