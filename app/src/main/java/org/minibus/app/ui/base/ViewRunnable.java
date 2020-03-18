package org.minibus.app.ui.base;

public interface ViewRunnable<V extends Contract.View> {
    void run(V view);
}
