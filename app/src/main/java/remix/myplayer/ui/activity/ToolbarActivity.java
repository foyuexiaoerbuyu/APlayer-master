package remix.myplayer.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import remix.myplayer.R;
import remix.myplayer.theme.ToolbarContentTintHelper;
import remix.myplayer.ui.activity.base.BaseMusicActivity;

import static remix.myplayer.theme.ThemeStore.getMaterialPrimaryColor;


/**
 * Created by taeja on 16-3-15.
 */
@SuppressLint("Registered")
public class ToolbarActivity extends BaseMusicActivity {

    protected Toolbar toolbar;

    public static int getToolbarBackgroundColor(Toolbar toolbar) {
        return toolbar != null && toolbar.getBackground() instanceof ColorDrawable
                ? ((ColorDrawable) toolbar.getBackground()).getColor() : 0;
    }

    protected void setUpToolbar(String title, @DrawableRes int iconRes) {
        if (toolbar == null) {
            toolbar = findViewById(R.id.toolbar);
        }
        toolbar.setTitle(title);

        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getMaterialPrimaryColor());
        toolbar.setNavigationIcon(iconRes);
        toolbar.setNavigationOnClickListener(v -> onClickNavigation());
    }

    protected void setUpToolbar(String title) {
        setUpToolbar(title, R.drawable.ic_arrow_back_white_24dp);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar toolbar = this.getToolbar();
        ToolbarContentTintHelper
                .handleOnCreateOptionsMenu(this, toolbar, menu, getToolbarBackgroundColor(toolbar));
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(this, this.getToolbar());
        return super.onPrepareOptionsMenu(menu);
    }

    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        this.toolbar = toolbar;
        super.setSupportActionBar(toolbar);
    }

    protected Toolbar getToolbar() {
        return this.toolbar;
    }

    protected void onClickNavigation() {
        finish();
    }

}
