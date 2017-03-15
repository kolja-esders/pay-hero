package edu.pietro.team.payhero.event;

/**
 * Fired when there is a detection (e.g. a scanned barcode) and there is further
 * processing like an API request needed.
 */
public class OnStartDetectionPostProcessing {

    public String message = null;

    public OnStartDetectionPostProcessing() {
    }

    public OnStartDetectionPostProcessing(String message) {
        this.message = message;
    }

}
