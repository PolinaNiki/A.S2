package com.example.colortiles;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import static java.sql.DriverManager.println;

class Tile {
    int color;
    int left;
    int right;
    int top;
    int bottom;


    Tile(int l, int r, int t, int b, int c) {
        color = c;
        left = l;
        right = r;
        top = t;
        bottom = b;
    }

    int getColor() {
        return color;
    }

    void setColor(int c) {
        color = c;
    }
}

public class TilesView extends View {

    boolean flag = true;
    int column = 4;
    int row = 4;
    int outline = 20;
    boolean win = false;

    Tile[][] tiles = new Tile[4][4];
    int darkColor = Color.parseColor("#4B0082");
    int brightColor = Color.parseColor("#E6E6FA");
    int width;
    int height;

    public TilesView(Context context) {
        super(context);
    }

    public TilesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {
        width = canvas.getWidth();
        height = canvas.getHeight();

        int tilewidth = width / column;
        int tileheight = height / row;
        int br=0;
        int dk = 0;

        Paint bright = new Paint();
        bright.setColor(brightColor);
        Paint dark = new Paint();
        dark.setColor(darkColor);

        bright.setStyle(Paint.Style.FILL);
        dark.setStyle(Paint.Style.FILL);


        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {

                int left = j * tilewidth;
                int top = i * tileheight;
                int right = left + tilewidth;
                int bottom = top + tileheight;

                Rect tile = new Rect();
                tile.set(left + outline, top + outline, right - outline, bottom - outline);
                int color;
                if (flag) {
                    if (Math.random() > 0.5) {
                        canvas.drawRect(tile, bright);
                        color = 1;
                    } else {
                        canvas.drawRect(tile, dark);
                        color = 0;
                    }
                    tiles[i][j] = new Tile(left, right, top, bottom, color);

                } else {
                    color = tiles[i][j].getColor();
                    if (color == 0) {
                        canvas.drawRect(tile, bright);
                        br += 1;
                    } else {
                        canvas.drawRect(tile, dark);
                        dk += 1;
                    }
                }
            }
        }
        if(flag) flag = false;
        if ((br==column*row)||(dk==column*row))
        {
            this.setAlpha((float)0.15);
            win = true;
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (win){
            return true;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();


        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    if (tiles[i][j].left < x && tiles[i][j].right > x) {
                        if (tiles[i][j].top < y && tiles[i][j].bottom > y) {
                            int k = i, m = j;

                            for (int ii = 0; ii < row; ii++) {
                                for (int jj = 0; jj < column; jj++) {

                                    if (ii == k || jj == m) {
                                        if (tiles[ii][jj].getColor() == 1) {
                                            tiles[ii][jj].setColor(0);
                                        }
                                        else {
                                            tiles[ii][jj].setColor(1);
                                        }
                                    }


                                }
                            }

                            break;
                        }
                    }


                }

            }



            invalidate();
        }
        return true;

    }
}

