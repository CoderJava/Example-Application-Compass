package com.ysn.exampleapplicationcompass.views.base;

/**
 * Created by root on 17/04/17.
 */

public interface Presenter<T extends View> {
    void onAttach(T view);

    void onDetach();
}
