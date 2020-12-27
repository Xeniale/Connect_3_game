package com.xeniale.connect3game;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.gridlayout.widget.GridLayout;

import petrov.kristiyan.colorpicker.ColorPicker;


public class MainActivity extends AppCompatActivity {
    private Connect3Game.Player activePlayer = Connect3Game.Player.O;
    private Connect3Game game = new Connect3Game();
    boolean endOfGame = false;

    private int currentXColor = Color.BLACK;
    private int currentOColor = Color.RED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);


        ImageView viewX = (ImageView) findViewById(R.id.pickColorXImageButton);
        Drawable drawableX = viewX.getDrawable();
        //setColorFilter Mode.MULTIPLY because it's non-vector drawable
        drawableX.setColorFilter(new PorterDuffColorFilter(currentXColor, PorterDuff.Mode.MULTIPLY));

        Drawable drawableO = ((ImageView) findViewById(R.id.pickColorOImageButton)).getDrawable();
        drawableO.setColorFilter(new PorterDuffColorFilter(currentOColor, PorterDuff.Mode.MULTIPLY));
    }

    // Menu icons are inflated just as they were with actionbar
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    public void pickColorForX(View view) {
        ImageView button = (ImageView) view;
        ColorPicker colorPicker = new ColorPicker(MainActivity.this);
        configureColorPicker(colorPicker, currentXColor)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        currentXColor = color;
                        button.getDrawable().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                        colorPicker.setDefaultColorButton(color);
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();
    }

    public void pickColorForO(View view) {
        ImageView button = (ImageView) view;
        ColorPicker colorPicker = new ColorPicker(MainActivity.this);
        configureColorPicker(colorPicker, currentOColor)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        currentOColor = color;
                        button.getDrawable().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                        colorPicker.setDefaultColorButton(color);
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();
    }

    public void dropIn(View view) {
        ImageView imageView = (ImageView) view;

        if (imageView.getDrawable() != null || endOfGame) {
            return;
        }

        imageView.setTranslationY(-1500);
        String[] rowCol = imageView.getTag().toString().split("_");
        Connect3Game.MoveResult result = game.nextMove(activePlayer,
                new Connect3Game.RowCol(Short.parseShort(rowCol[0]), Short.parseShort(rowCol[1]))
        );


        TextView whoWonTextView = findViewById(R.id.whoWonText);
        String whoWonText = "";
        switch (result) {
            case Win:
                whoWonText = "Player " + activePlayer.name() + " won!";
                endOfGame = true;
                break;
            case Draw:
                whoWonText = "It's a draw!";
                endOfGame = true;
                break;
        }

        if (activePlayer == Connect3Game.Player.O) {
            imageView.setImageDrawable(changeOXColor(view, R.drawable.sign_o, currentOColor));
            activePlayer = Connect3Game.Player.X;
        } else {
            imageView.setImageDrawable(changeOXColor(view, R.drawable.sign_x, currentXColor));
            activePlayer = Connect3Game.Player.O;
        }

        imageView.animate().translationYBy(1500).setDuration(800);

        if (endOfGame) {
            whoWonTextView.setVisibility(View.VISIBLE);
            whoWonTextView.setText(whoWonText);

            findViewById(R.id.newGameButton).setVisibility(View.VISIBLE);
        }
    }

    public void runNewGame(View view) {
        findViewById(R.id.whoWonText).setVisibility(View.INVISIBLE);
        activePlayer = Connect3Game.Player.O;
        game = new Connect3Game();
        endOfGame = false;
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ((ImageView) gridLayout.getChildAt(i)).setImageDrawable(null);
        }
        findViewById(R.id.newGameButton).setVisibility(View.INVISIBLE);
    }

    private ColorPicker configureColorPicker(ColorPicker colorPicker, int initialColor) {
        colorPicker.setColumns(5)
                .setDefaultColorButton(initialColor)
                .setColors(Color.RED, Color.YELLOW, Color.CYAN, Color.BLUE,
                        Color.MAGENTA, Color.BLACK, Color.GREEN)
                .setRoundColorButton(true);
        return colorPicker;
    }

    private Drawable changeOXColor(View view, @DrawableRes int resource, int color) {
        Drawable d = ContextCompat.getDrawable(view.getContext(), resource);
        if (d == null) {
            throw new IllegalArgumentException("Missing drawable resource: " + resource);
        }
        d = DrawableCompat.wrap(d);
        //setTint because it's vector drawable
        DrawableCompat.setTint(d.mutate(), color);
        return d;
    }

}