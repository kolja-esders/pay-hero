package edu.pietro.team.payhero.helper;

/**
 * Created by maxim on 26.11.16.
 */

public interface IImageOcrResolver {
    interface OcrListener {
        String onSuccess();
        Exception onError();
    }

    void resolveImage(String imgBinary, OcrListener l);
}