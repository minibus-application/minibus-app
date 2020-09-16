package org.minibus.app.ui.login;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.minibus.app.AppConstants;
import org.minibus.app.helpers.AppAlertsHelper;
import org.minibus.app.ui.base.BaseDialogFragment;

import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.minibus.app.ui.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginFragment extends BaseDialogFragment implements LoginContract.View {

    public static final int REQ_CODE = AppConstants.LOGIN_FRAGMENT_REQ_CODE;

    @BindView(R.id.input_user_name_container) TextInputLayout inputContainerUserName;
    @BindView(R.id.input_user_phone_container) TextInputLayout inputContainerUserPhone;
    @BindView(R.id.input_user_pass_container) TextInputLayout inputContainerUserPass;
    @BindView(R.id.input_user_conf_pass_container) TextInputLayout inputContainerUserConfirmPass;
    @BindView(R.id.input_user_name) TextInputEditText inputUserName;
    @BindView(R.id.input_user_phone) TextInputEditText inputUserPhone;
    @BindView(R.id.input_user_pass) TextInputEditText inputUserPass;
    @BindView(R.id.input_user_conf_pass) TextInputEditText inputUserConfirmPass;
    @BindView(R.id.button_login) MaterialButton buttonLogin;
    @BindView(R.id.btn_form_switcher) MaterialButton buttonFormExpand;
    @BindView(R.id.appbar_login) AppBarLayout appbar;
    @BindView(R.id.toolbar_custom) Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title) TextView textToolbarTitle;

    @Inject LoginPresenter<LoginContract.View> presenter;

    public interface LoginFragmentCallback {
        void onLoggedIn();
    }

    private LoginFragmentCallback callback;
    private boolean isLoginForm = true;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogSlideAnimation;
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_DialogFragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        callback = (LoginFragmentCallback) getTargetFragment();
        setUnbinder(ButterKnife.bind(this, view));
        getActivityComponent().inject(this);
        presenter.attachView(this);

        toolbar.setNavigationIcon(R.drawable.ic_close_dark_24dp);
        toolbar.setNavigationOnClickListener(v -> presenter.onCloseButtonClick());
        textToolbarTitle.setText(getMainActivity().getResources().getString(R.string.login_title));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String countryCode = AppConstants.COUNTRY_CODE;

        inputUserPhone.setText(countryCode);
        Selection.setSelection(inputUserPhone.getText(), Objects.requireNonNull(inputUserPhone.getText()).length());

        inputUserPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith(countryCode)) {
                    inputUserPhone.setText(countryCode);
                    Selection.setSelection(inputUserPhone.getText(), inputUserPhone.getText().length());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @OnClick(R.id.button_login)
    public void onLoginClick() {
        hideKeyboard();
        presenter.onLoginButtonClick(inputUserName.getEditableText().toString(),
                inputUserPhone.getEditableText().toString(),
                inputUserPass.getEditableText().toString(),
                inputUserConfirmPass.getEditableText().toString(),
                isLoginForm);
    }

    @OnClick(R.id.btn_form_switcher)
    public void onFormSwitcherClick() {
        if (isLoginForm) {
            isLoginForm = false;
            inputContainerUserName.setVisibility(View.VISIBLE);
            inputContainerUserConfirmPass.setVisibility(View.VISIBLE);
            buttonFormExpand.setText(getResources().getString(R.string.login_account_exists));
        } else {
            isLoginForm = true;
            inputContainerUserName.setVisibility(View.GONE);
            inputContainerUserConfirmPass.setVisibility(View.GONE);
            buttonFormExpand.setText(getResources().getString(R.string.login_no_account));
        }
    }

    @Override
    public void showWelcomeMessage(@StringRes int msgResId, String userName) {
        showInfo(getResources().getString(msgResId, userName));
        callback.onLoggedIn();
    }

    @Override
    public void showNameFieldError(int msgResId) {
        inputContainerUserName.setError(getString(msgResId));
    }

    @Override
    public void showPhoneFieldError(int msgResId) {
        inputContainerUserPhone.setError(getString(msgResId));
    }

    @Override
    public void showPasswordFieldError(int msgResId) {
        inputContainerUserPass.setError(getString(msgResId));
    }

    @Override
    public void showConfirmPasswordFieldError(int msgResId) {
        inputContainerUserConfirmPass.setError(getString(msgResId));
    }

    @Override
    public void hideNameFieldError() {
        inputContainerUserName.setError(null);
    }

    @Override
    public void hidePhoneFieldError() {
        inputContainerUserPhone.setError(null);
    }

    @Override
    public void hidePasswordFieldError() {
        inputContainerUserPass.setError(null);
    }

    @Override
    public void hideConfirmPasswordFieldError() {
        inputContainerUserConfirmPass.setError(null);
    }

    private void hideKeyboard() {
        try {
            final InputMethodManager inputManager =
                    (InputMethodManager) getMainActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
        } catch (NullPointerException ignore) {
            // ignore
        }
    }

    @Override
    public void close() {
        dismiss();
    }

    @Override
    public void closeOnSuccessLogin() {
        final Handler handler = new Handler();
        AppAlertsHelper.setProgressHudCompleted(getMainActivity());
        handler.postDelayed(() -> {
            AppAlertsHelper.hideProgressHud();
            close();
        }, 1200);
    }

    @Override
    protected void onBack() {
        presenter.onCloseButtonClick();
    }
}
