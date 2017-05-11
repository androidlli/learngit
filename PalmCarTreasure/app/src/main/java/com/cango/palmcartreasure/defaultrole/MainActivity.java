package com.cango.palmcartreasure.defaultrole;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.Toast;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;

import java.util.Arrays;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setstatusBarColorByDrawableId(R.mipmap.ic_launcher_round);
    }

    /**
     * 根据Palette提取的颜色，修改tab和toolbar以及状态栏的颜色
     */
    private void setstatusBarColorByDrawableId(int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        Palette.Builder builder = Palette.from(bitmap);
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                setStatusBarColor(colorBurn(vibrant.getRgb()));
            }
        });
    }

    /**
     * 颜色加深处理
     */
    private int colorBurn(int RGBValues) {
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void setDisableShortcuts(View view) {
        if (Build.VERSION.SDK_INT >= 25) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            shortcutManager.disableShortcuts(Arrays.asList("id1"), getString(R.string.shortcut_display_prompt));
            Toast.makeText(this, "set disable true", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void updateShortcuts(View view) {
        if (Build.VERSION.SDK_INT >= 25) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(this, "id1")
                    .setShortLabel("setShortLabel")
                    .setLongLabel("setLongLabel")
                    .setDisabledMessage("setDisabledMessage")
                    .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
                    .setIntent(intent)
                    .build();
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            boolean updateShortcuts = shortcutManager.updateShortcuts(Arrays.asList(shortcutInfo));
            if (updateShortcuts) {
                Toast.makeText(this, "updateShortcuts true", Toast.LENGTH_SHORT).show();

            }
        }

    }
}
