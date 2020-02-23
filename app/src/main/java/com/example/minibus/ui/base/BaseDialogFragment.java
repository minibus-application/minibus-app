package com.example.minibus.ui.base;

import android.content.Context;
import androidx.fragment.app.DialogFragment;

import com.example.minibus.di.components.ActivityComponent;
import com.example.minibus.ui.main.MainActivity;

import butterknife.Unbinder;

public abstract class BaseDialogFragment extends DialogFragment implements Contract.View {

    private MainActivity activity;
    private Unbinder unbinder;

    public MainActivity getMainActivity() {
        return activity;
    }

    public ActivityComponent getActivityComponent() {
        return activity.getActivityComponent();
    }

    public void setUnbinder(Unbinder unbinder) {
        this.unbinder = unbinder;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            this.activity = (MainActivity) context;
        }
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        activity = null;
        super.onDetach();
    }

    @Override
    public void showEmptyView() {

    }
}
