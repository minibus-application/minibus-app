package org.minibus.app.ui.base;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.minibus.app.di.components.ActivityComponent;
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

    public MainActivity getMainActivity() {
        return activity;
    }

    public ActivityComponent getActivityComponent() {
        return activity.getActivityComponent();
    }

    public void setUnbinder(Unbinder unbinder) {
        this.unbinder = unbinder;
    }

    protected <T extends DialogFragment> T openDialogFragment(T fragment, int reqCode, Bundle bundle) {
        String tag = fragment.getClass().getName();
        FragmentTransaction transaction = requireFragmentManager().beginTransaction();
        Fragment prevFragment = requireFragmentManager().findFragmentByTag(tag);

        fragment.setTargetFragment(this, reqCode);

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        if (prevFragment != null) {
            transaction.remove(prevFragment);
        } else {
            transaction.addToBackStack(null);
            fragment.show(transaction, tag);
        }

        return fragment;
    }
}
