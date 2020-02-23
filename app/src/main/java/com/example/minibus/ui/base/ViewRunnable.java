package com.example.minibus.ui.base;

public interface ViewRunnable<V extends Contract.View> {
    void run(V view);
}
