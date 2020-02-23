package com.example.minibus.ui.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import timber.log.Timber;

public abstract class BasePresenter<V extends Contract.View> implements Contract.Presenter<V> {

    private CompositeDisposable compositeDisposable;
    private WeakViewReference<V> weakView;

    private void deleteSubscription() {
        if (compositeDisposable != null) compositeDisposable.dispose();
    }

    protected void addSubscription(DisposableSingleObserver<?> observer) {
        if (compositeDisposable == null) compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observer);
    }

    @Override
    public WeakViewReference<V> getView() {
        return weakView;
    }

    @Override
    public void attachView(V view) {
        Timber.d("Attaching view %s", view.getClass().getSimpleName());

        weakView = new WeakViewReference<>(view);
    }

    @Override
    public void detachView() {
        Timber.d("Detaching view %s", weakView.get().getClass().getSimpleName());

        weakView = new WeakViewReference<>(null);
        deleteSubscription();
    }
}
