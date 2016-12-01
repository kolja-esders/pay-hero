package edu.pietro.team.payhero.helper;

/**
 * Since there is apparently no proper generic callback <3.
 * @param <T>
 */
public interface GenericCallbackInterface<T> {

    void onSuccess(T result);

    void onFailure(Throwable e);
}
