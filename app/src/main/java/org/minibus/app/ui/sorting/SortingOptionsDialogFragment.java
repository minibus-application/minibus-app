package org.minibus.app.ui.sorting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.minibus.app.AppConstants;
import org.minibus.app.ui.R;
import org.minibus.app.ui.base.BaseDialogFragment;

import java.util.ArrayList;

public class SortingOptionsDialogFragment extends BaseDialogFragment {

    public interface SortingOptionClickListener {
        void onSortingOptionClick(int position);
    }

    public static final int REQ_CODE = AppConstants.SORT_BY_FRAGMENT_REQ_CODE;
    public static final String SORT_OPTIONS_KEY = "sort_options_key";
    public static final String SORT_OPTION_POS_KEY = "sort_option_pos_key";
    private SortingOptionClickListener listener;
    private ArrayList<String> options;
    private int lastPosition;
    private int position;

    public static SortingOptionsDialogFragment newInstance() {
        return new SortingOptionsDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            options = getArguments().getStringArrayList(SORT_OPTIONS_KEY);
            lastPosition = getArguments().getInt(SORT_OPTION_POS_KEY);
        } else {
            dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        listener = (SortingOptionClickListener) getTargetFragment();

        AlertDialog.Builder builder = new AlertDialog.Builder(getMainActivity());
        builder.setTitle(getResources().getString(R.string.sort_by));
        builder.setSingleChoiceItems(options.toArray(new CharSequence[0]), lastPosition, (dialogInterface, i) -> {
            position = i;
        });

        builder.setPositiveButton("Ok", (dialogInterface, i) -> {
            listener.onSortingOptionClick(position);
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            onBack();
        });

        return builder.create();
    }

    @Override
    protected void onBack() {
        dismiss();
    }
}
