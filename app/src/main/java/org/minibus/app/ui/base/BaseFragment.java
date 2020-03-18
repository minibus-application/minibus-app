package org.minibus.app.ui.base;

import android.content.Context;
import androidx.fragment.app.Fragment;

import org.minibus.app.di.components.ActivityComponent;
import org.minibus.app.helpers.AppAlertsHelper;
import org.minibus.app.ui.main.MainActivity;

import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements Contract.View {

    private MainActivity activity;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            this.activity = (MainActivity) context;
        }
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
    public void showError(String msg) {
        AppAlertsHelper.showSnackbar(getMainActivity(), msg);
    }

    @Override
    public void showError(int msgResId) {
        this.showError(getMainActivity().getResources().getString(msgResId));
    }

    @Override
    public void showInfo(int msgResId) {
        this.showInfo(getMainActivity().getResources().getString(msgResId));
    }

    @Override
    public void showInfo(String msg) {
        AppAlertsHelper.showSnackbar(getMainActivity(), msg);
    }
}
