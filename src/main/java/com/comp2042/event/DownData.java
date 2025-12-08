package com.comp2042.event;

import com.comp2042.model.ClearRow;
import com.comp2042.view.ViewData;

/**
 * Data class containing the result of a down movement event.
 * Includes information about cleared rows (if any) and updated view data
 * after the brick has moved down or been merged.
 */
public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    /**
     * Constructs a DownData object with the specified clear row and view data.
     * 
     * @param clearRow information about cleared rows (may be null if no rows were cleared)
     * @param viewData the updated view data after the down event
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /**
     * Gets the information about cleared rows, if any.
     * 
     * @return the ClearRow object, or null if no rows were cleared
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Gets the updated view data after the down movement.
     * 
     * @return the ViewData containing updated brick and board information
     */
    public ViewData getViewData() {
        return viewData;
    }
}
