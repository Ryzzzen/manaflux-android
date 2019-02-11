package com.github.kko7.manaflux_android.CustomElements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import java.io.InputStream;

public class GifView extends View {

    private Movie gifMovie;
    private int movieWidth, movieHeight;
    private long mMovieStart;

    public GifView(Context context) {
        super(context);
    }

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ResourceType")
    private void init(Context context, int resource) {
        setFocusable(true);
        InputStream gifInputStream = context
                .getResources()
                .openRawResource(resource);

        gifMovie = Movie.decodeStream(gifInputStream);
        movieWidth = gifMovie.width();
        movieHeight = gifMovie.height();
    }

    public void startGif(int resource) {
        init(getContext(), resource);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(movieWidth, movieHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        long now = android.os.SystemClock.uptimeMillis();
        if (mMovieStart == 0) {
            mMovieStart = now;
        }

        if (gifMovie != null) {

            int dur = gifMovie.duration();

            int relTime = (int) ((now - mMovieStart) % dur);

            gifMovie.setTime(relTime);

            gifMovie.draw(canvas, 0, 0);
            invalidate();
        }
    }
}