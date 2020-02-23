package com.example.minibus.ui.base;

import java.lang.ref.WeakReference;

import timber.log.Timber;

/*
    Have to apply the following weak reference approach to get the view instance
    to avoid NullPointerException after trying to access the view while it's detached.
    The approach was borrowed from Brandon's Romano article on carrot.is
 */
public class WeakViewReference<V extends Contract.View> extends WeakReference<V> {

    public WeakViewReference(V referent) {
        super(referent);
    }

    public void ifAlive(ViewRunnable<V> runnable) {
        V view = get();
        if(view != null) runnable.run(view);
        else Timber.w("Attempt to access a detached view");
    }
}
