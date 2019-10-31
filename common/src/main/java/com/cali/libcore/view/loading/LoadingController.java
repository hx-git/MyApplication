package com.cali.libcore.view.loading;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.cali.common.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;


/**
 * @version 1.0
 */
public class LoadingController implements LoadingInterface {

    private Context context;
    private View loadingTargetView;
    // loading
    private int loadingImageResource;
    private Drawable loadingImageDrawable;
    private String loadingMessage;
    // network error

    // other error
    private int errorImageResource;
    private Drawable errorImageDrawable;
    private String errorMessage;
    // empty
    private int emptyViewImageResource;
    private Drawable emptyViewImageDrawable;
    private String emptyMessage;

    // listener
    private String networkErrorRetryText;
    private OnClickListener onNetworkErrorRetryClickListener;
    private String errorRetryText;
    private OnClickListener onErrorRetryClickListener;
    private String emptyTodoText;
    private OnClickListener onEmptyTodoClickListener;

    private LayoutInflater inflater;
    /**
     * {@link #loadingTargetView} 的父布局
     */
    private ViewGroup parentView;
    /**
     * {@link #loadingTargetView} 在父布局中的位置
     */
    private int currentViewIndex;
    /**
     * {@link #loadingTargetView} 的LayoutParams
     */
    private ViewGroup.LayoutParams params;

    private View loadingView;
    private AnimationDrawable loadingAnimationDrawable;
    private View networkErrorView;
    private View errorView;
    private View emptyView;

    private LoadingController(Builder builder) {
        context = builder.context;
        loadingTargetView = builder.loadingTargetView;
        loadingImageResource = builder.loadingImageResource;
        loadingImageDrawable = builder.loadingImageDrawable;
        loadingMessage = builder.loadingMessage;
        errorImageResource = builder.errorImageResource;
        errorImageDrawable = builder.errorImageDrawable;
        errorMessage = builder.errorMessage;
        emptyViewImageResource = builder.emptyViewImageResource;
        emptyViewImageDrawable = builder.emptyViewImageDrawable;
        emptyMessage = builder.emptyMessage;
        networkErrorRetryText = builder.networkErrorRetryText;
        onNetworkErrorRetryClickListener = builder.onNetworkErrorRetryClickListener;
        errorRetryText = builder.errorRetryText;
        onErrorRetryClickListener = builder.onErrorRetryClickListener;
        emptyTodoText = builder.emptyTodoText;
        onEmptyTodoClickListener = builder.onEmptyTodoClickListener;

        if (builder.customLoadingView != null) {
            loadingView = builder.customLoadingView;
        }
        if (builder.customNetworkErrorView != null) {
            networkErrorView = builder.customNetworkErrorView;
        }
        if (builder.customErrorView != null) {
            errorView = builder.customErrorView;
        }
        if (builder.customEmptyView != null) {
            emptyView = builder.customEmptyView;
        }

        init();
    }

    private void init() {
        inflater = LayoutInflater.from(context);
        params = loadingTargetView.getLayoutParams();
        if (loadingTargetView.getParent() != null) {
            parentView = (ViewGroup) loadingTargetView.getParent();
        } else {
            parentView = loadingTargetView.getRootView().findViewById(android.R.id.content);
        }
        int count = parentView.getChildCount();
        for (int i = 0; i < count; i++) {
            if (loadingTargetView == parentView.getChildAt(i)) {
                currentViewIndex = i;
                break;
            }
        }
    }

    /**
     * 切换状态
     *
     * @param view 目标View
     */
    private void showView(View view) {
        // 如果当前状态和要切换的状态相同，则不做处理，反之切换
        if (parentView.getChildAt(currentViewIndex) != view) {
            // 先把view从父布局移除
            ViewGroup viewParent = (ViewGroup) view.getParent();
            if (viewParent != null) {
                viewParent.removeView(view);
            }
            parentView.removeViewAt(currentViewIndex);
            parentView.addView(view, currentViewIndex, params);
            if (loadingAnimationDrawable != null) {
                if (view == loadingView) {
                    loadingAnimationDrawable.start();
                } else {
                    loadingAnimationDrawable.stop();
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public void showLoading() {
        if (loadingView != null) {
            showView(loadingView);
            return;
        }
        loadingView = inflater.inflate(R.layout.loading_layout, null);
        ProgressBar pbLoading = loadingView.findViewById(R.id.pb_loading);
        Sprite sprite = new ThreeBounce();
        sprite.start();
        sprite.setBounds(0, 0, 100, 100);
        //setColor(R.color.**)错误
        sprite.setColor(Color.parseColor("#f80000"));
        pbLoading.setIndeterminateDrawable(sprite);

        ImageView ivLoading = loadingView.findViewById(R.id.iv_loading);
        TextView tvLoadingMessage = loadingView.findViewById(R.id.tv_loadingMessage);
        pbLoading.setVisibility(View.GONE);
        ivLoading.setVisibility(View.GONE);
        tvLoadingMessage.setVisibility(View.GONE);

        if (loadingImageResource != 0) {
            ivLoading.setVisibility(View.VISIBLE);
            ivLoading.setImageResource(loadingImageResource);
            Drawable imageDrawable = ivLoading.getDrawable();
            if (imageDrawable instanceof AnimationDrawable) {
                loadingAnimationDrawable = (AnimationDrawable) imageDrawable;
            }
        } else if (loadingImageDrawable != null) {
            ivLoading.setVisibility(View.VISIBLE);
            ivLoading.setImageDrawable(loadingImageDrawable);
            Drawable imageDrawable = ivLoading.getDrawable();
            if (imageDrawable instanceof AnimationDrawable) {
                loadingAnimationDrawable = (AnimationDrawable) imageDrawable;
            }
        } else {
            pbLoading.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(loadingMessage)) {
            tvLoadingMessage.setVisibility(View.VISIBLE);
            tvLoadingMessage.setText(loadingMessage);
        }
        showView(loadingView);
    }

    @SuppressLint("InflateParams")
    @Override
    public void showNetworkError() {
        if (networkErrorView != null) {
            showView(networkErrorView);
            return;
        }
        networkErrorView = inflater.inflate(R.layout.loading_error_layout, null);
        TextView tvNetworkErrorMessage = networkErrorView.findViewById(R.id.tv_errorMessage);
        Button btnRetry = networkErrorView.findViewById(R.id.btn_retry);

        tvNetworkErrorMessage.setText(context.getResources().getString(R.string.LoadingController_network_error_message));

        if (!TextUtils.isEmpty(networkErrorRetryText)) {
            btnRetry.setText(networkErrorRetryText);
        }
        if (onNetworkErrorRetryClickListener != null) {
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNetworkErrorRetryClickListener.onClick();
                }
            });
        }

        showView(networkErrorView);
    }

    @SuppressLint("InflateParams")
    @Override
    public void showError() {
        if (errorView != null) {
            showView(errorView);
            return;
        }
        errorView = inflater.inflate(R.layout.loading_error_layout, null);
        ImageView ivError = errorView.findViewById(R.id.iv_error);
        TextView tvErrorMessage = errorView.findViewById(R.id.tv_errorMessage);
        Button btnRetry = errorView.findViewById(R.id.btn_retry);

        ivError.setImageResource(R.drawable.loading_error);
        if (errorImageResource != 0) {
            ivError.setImageResource(errorImageResource);
        } else if (errorImageDrawable != null) {
            ivError.setImageDrawable(errorImageDrawable);
        }

        if (!TextUtils.isEmpty(errorMessage)) {
            tvErrorMessage.setText(errorMessage);
        } else {
            tvErrorMessage.setText(context.getResources().getString(R.string.LoadingController_error_message));
        }

        if (!TextUtils.isEmpty(errorRetryText)) {
            btnRetry.setText(errorRetryText);
        }
        if (onErrorRetryClickListener != null) {
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onErrorRetryClickListener.onClick();
                }
            });
        }

        showView(errorView);
    }

    @SuppressLint("InflateParams")
    @Override
    public void showEmpty() {
        if (emptyView != null) {
            showView(emptyView);
            return;
        }
        emptyView = inflater.inflate(R.layout.loading_error_layout, null);
        ImageView ivEmpty = emptyView.findViewById(R.id.iv_error);
        TextView tvEmptyMessage = emptyView.findViewById(R.id.tv_errorMessage);
        TextView btnTodo = emptyView.findViewById(R.id.btn_retry);
        ivEmpty.setVisibility(View.GONE);
        tvEmptyMessage.setVisibility(View.GONE);
        btnTodo.setVisibility(View.GONE);

        if (emptyViewImageResource != 0) {
            ivEmpty.setVisibility(View.VISIBLE);
            ivEmpty.setImageResource(emptyViewImageResource);
        } else if (emptyViewImageDrawable != null) {
            ivEmpty.setVisibility(View.VISIBLE);
            ivEmpty.setImageDrawable(emptyViewImageDrawable);
        }

        if (!TextUtils.isEmpty(emptyMessage)) {
            tvEmptyMessage.setVisibility(View.VISIBLE);
            tvEmptyMessage.setText(emptyMessage);
        }

        if (!TextUtils.isEmpty(emptyTodoText)) {
            btnTodo.setVisibility(View.VISIBLE);
            btnTodo.setText(emptyTodoText);
        }
        if (onEmptyTodoClickListener != null) {
            btnTodo.setOnClickListener(v -> onEmptyTodoClickListener.onClick());
        }

        showView(emptyView);
    }

    @Override
    public void dismissLoading() {
        showView(loadingTargetView);
    }

    public static class Builder {

        private Context context;
        private View loadingTargetView;
        // qlk_loading
        private int loadingImageResource;
        private Drawable loadingImageDrawable;
        private String loadingMessage;
        private View customLoadingView;
        // network qlk_loading_result
        private View customNetworkErrorView;

        // normal qlk_loading_result
        private int errorImageResource;
        private Drawable errorImageDrawable;
        private String errorMessage;
        private View customErrorView;
        // empty
        private int emptyViewImageResource;
        private Drawable emptyViewImageDrawable;
        private String emptyMessage;
        private View customEmptyView;

        // listener
        private String networkErrorRetryText;
        private OnClickListener onNetworkErrorRetryClickListener;
        private String errorRetryText;
        private OnClickListener onErrorRetryClickListener;
        private String emptyTodoText;
        private OnClickListener onEmptyTodoClickListener;

        public Builder(@NonNull Context context, @NonNull View loadingTargetView) {
            this.context = context;
            this.loadingTargetView = loadingTargetView;
        }

        public Builder setLoadingImageResource(int loadingImageResource) {
            this.loadingImageResource = loadingImageResource;
            return this;
        }

        public Builder setLoadingImageDrawable(Drawable loadingImageDrawable) {
            this.loadingImageDrawable = loadingImageDrawable;
            return this;
        }

        public Builder setLoadingMessage(String loadingMessage) {
            this.loadingMessage = loadingMessage;
            return this;
        }

        /**
         * 自定义loadingView
         *
         * @param loadingView view for loading
         * @return this Builder
         */
        public Builder setLoadingView(View loadingView) {
            this.customLoadingView = loadingView;
            return this;
        }

        /**
         * 自定义networkErrorView
         *
         * @param networkErrorView view for networkError
         * @return this Builder
         */
        public Builder setNetworkErrorView(View networkErrorView) {
            this.customNetworkErrorView = networkErrorView;
            return this;
        }

        public Builder setErrorImageResource(int errorImageResource) {
            this.errorImageResource = errorImageResource;
            return this;
        }

        public Builder setErrorImageDrawable(Drawable errorImageDrawable) {
            this.errorImageDrawable = errorImageDrawable;
            return this;
        }

        public Builder setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        /**
         * 自定义errorView
         *
         * @param errorView view for error
         * @return this Builder
         */
        public Builder setErrorView(View errorView) {
            this.customErrorView = errorView;
            return this;
        }

        public Builder setEmptyViewImageResource(int emptyViewImageResource) {
            this.emptyViewImageResource = emptyViewImageResource;
            return this;
        }

        public Builder setEmptyViewImageDrawable(Drawable emptyViewImageDrawable) {
            this.emptyViewImageDrawable = emptyViewImageDrawable;
            return this;
        }

        public Builder setEmptyMessage(String emptyMessage) {
            this.emptyMessage = emptyMessage;
            return this;
        }

        public Builder setTodoMessage(String todoMessage) {
            this.emptyTodoText = todoMessage;
            return this;
        }

        /**
         * 自定义emptyView
         *
         * @param emptyView view for empty
         * @return this Builder
         */
        public Builder setEmptyView(View emptyView) {
            this.customEmptyView = emptyView;
            return this;
        }

        public Builder setOnNetworkErrorRetryClickListener(OnClickListener listener) {
            this.onNetworkErrorRetryClickListener = listener;
            return this;
        }

        public Builder setOnNetworkErrorRetryClickListener(String networkErrorRetryText, OnClickListener listener) {
            this.networkErrorRetryText = networkErrorRetryText;
            this.onNetworkErrorRetryClickListener = listener;
            return this;
        }

        public Builder setOnErrorRetryClickListener(OnClickListener listener) {
            this.onErrorRetryClickListener = listener;
            return this;
        }

        public Builder setOnErrorRetryClickListener(String errorRetryText, OnClickListener listener) {
            this.errorRetryText = errorRetryText;
            this.onErrorRetryClickListener = listener;
            return this;
        }

        public Builder setOnEmptyTodoClickListener(OnClickListener listener) {
            this.onEmptyTodoClickListener = listener;
            return this;
        }

        public Builder setOnEmptyTodoClickListener(String emptyTodoText, OnClickListener listener) {
            this.emptyTodoText = emptyTodoText;
            this.onEmptyTodoClickListener = listener;
            return this;
        }

        public LoadingController build() {
            return new LoadingController(this);
        }

    }
}
