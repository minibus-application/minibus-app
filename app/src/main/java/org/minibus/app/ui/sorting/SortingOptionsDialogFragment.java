package org.minibus.app.ui.sorting;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.minibus.app.AppConstants;
import org.minibus.app.ui.R;
import org.minibus.app.ui.base.BaseDialogFragment;

import java.util.ArrayList;

public class SortingOptionsDialogFragment extends BaseDialogFragment {

    public static final String SORT_OPTIONS_KEY = "sort_options_key";
    public static final String SORT_OPTION_POS_KEY = "sort_option_pos_key";
    public static final int REQ_CODE = AppConstants.SORT_BY_FRAGMENT_REQ_CODE;
    private OnSortingOptionClickListener listener;
    private ArrayList<String> options;
    private int lastSelectedPosition;
    private int selectedPosition;

    public static SortingOptionsDialogFragment newInstance() {
        return new SortingOptionsDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            options = getArguments().getStringArrayList(SORT_OPTIONS_KEY);
            lastSelectedPosition = getArguments().getInt(SORT_OPTION_POS_KEY);
        } else {
            dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        listener = (OnSortingOptionClickListener) getTargetFragment();

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getMainActivity());
        builder.setTitle(getResources().getString(R.string.sort_by));
        builder.setSingleChoiceItems(options.toArray(new CharSequence[0]), lastSelectedPosition, (dialogInterface, i) -> {
            selectedPosition = i;
        });

        builder.setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
            listener.onSortingOptionItemClick(selectedPosition);
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
            onBack();
        });

        return builder.create();
    }

    @Override
    protected void onBack() {
        dismiss();
    }

    public interface OnSortingOptionClickListener {
        void onSortingOptionItemClick(int position);
    }
}
