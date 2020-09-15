package org.minibus.app.ui.base;

import android.content.Context;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.minibus.app.di.components.ActivityComponent;
import org.minibus.app.ui.main.MainActivity;

import butterknife.Unbinder;

public abstract class BaseSheetDialogFragment extends BottomSheetDialogFragment implements Contract.View {

    private MainActivity activity;
    private Unbinder unbinder;

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

    public MainActivity getMainActivity() {
        return activity;
    }

    public ActivityComponent getActivityComponent() {
        return activity.getActivityComponent();
    }

    public void setUnbinder(Unbinder unbinder) {
        this.unbinder = unbinder;
    }
}
