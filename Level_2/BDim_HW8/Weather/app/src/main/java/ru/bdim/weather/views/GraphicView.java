package ru.bdim.weather.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import ru.bdim.weather.addiyional.Constants;

public class GraphicView extends View {
    private Paint paint;
    private Path path;
    private int[] datapoints = new int[] {};

    public GraphicView(Context context) {
        super(context);
        init();
    }
    public GraphicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initattr(context, attrs);
    }
    public GraphicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initattr(context, attrs);
    }
    public GraphicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        initattr(context, attrs);
    }

    public void init(){
        paint = new Paint();
    }
    private void initattr(Context context, AttributeSet attrs) {
    }
    public void setChartData(int[] datapoints) {
        this.datapoints = datapoints;
        path = new Path();
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (datapoints.length == 0) return;
            //рассчет необходимых параметров
            final int margin = 2;
            final int countLines = 3;
            int width = getWidth() - getPaddingStart() - getPaddingEnd();
            int height = getHeight() - margin*2;
            int min = min(datapoints);
            int max = max(datapoints);
            int dx = width/(datapoints.length);

            //нарисовать координатную сеть
            paint.setColor(Color.YELLOW);
            paint.setStrokeWidth(1);
            paint.setTextSize(30);
            for (float y = height, value = min; y > margin;
                 y -= height/countLines, value += (max-min)/(float)countLines){
                canvas.drawLine(10, y, width - 10, y, paint);
                canvas.drawText(Integer.toString(Math.round(value)), 10, y + margin, paint);
            }

            //нарисовать график
            paint.setColor(Color.CYAN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            int x = dx/2 + getPaddingStart();
            int y = getY(datapoints[0], min, max, height);
            path.moveTo(x, y);
            for (int i = 1; i < datapoints.length; i++){
                x += dx;
                y = getY(datapoints[i], min, max, height);
                path.lineTo(x, y);
            }
            canvas.drawPath(path, paint);
    }
    private int getY(int value, int min, int max, int height){
        return height - (value - min)*height/(max - min);
    }
    private int min(int[] array){
        int min = array[0];
        for (int i = 1; i < array.length; i++){
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }
    private int max(int[] array){
        int max = array[0];
        for (int i = 1; i < array.length; i++){
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
}