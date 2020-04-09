package ru.bdim.weather.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

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
        this.datapoints = datapoints.clone();
        path = new Path();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //оптимизировать код!!!
        //Прошу извинить за "кашу", к следующей сдаче домашней работы приведу в порядок
if (datapoints.length == 0) return;
        int width = getWidth()-getPaddingStart()-getPaddingEnd();
        int height = getHeight()-4;
        int min = min(datapoints);
        int max = max(datapoints);
        int dx = width/(datapoints.length);
        int x0 = dx/2 + getPaddingStart();
        int y0 = getY(datapoints[0], min, max, height);
        int x = x0, y;
        path.moveTo(x0, y0);
        for (int i = 1; i < datapoints.length; i++) {
            x += dx;
            y = getY(datapoints[i], min, max, height);
            //path.cubicTo(); Изучить фигуры Безье!!!
            path.lineTo(x, y+2);
            //x0 = x; y0 = y;
        }
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        canvas.drawPath(path, paint);
        //координатная сетка (сделать метод с циклом!!!)
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(1);
        paint.setTextSize(30);
        y = height - 2;
        canvas.drawLine(10, y, width-10, y+2, paint);
        int value = min;
        canvas.drawText(Integer.toString(value), 10, y + 2, paint);
        y = height - height/3;
        canvas.drawLine(10, y, width-10, y, paint);
        value += (max-min)/3;
        canvas.drawText(Integer.toString(value), 10, y + 2, paint);
        y /= 2;
        canvas.drawLine(10, y, width-10, y, paint);
        value += (max-min)/3;
        canvas.drawText(Integer.toString(value), 10, y + 2, paint);
    }
    private int getY(int value, int min, int max, int height){
        return height - (value - min)*height/(max - min);
    }
    private int min(int[] array){
        int min = array.length;
        for (int i = 0; i < array.length; i++){
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }
    private int max(int[] array){
        int max = array.length;
        for (int i = 0; i < array.length; i++){
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
    //стал не нужен
    private int avg(int[] array){
        int sum = 0;
        for(int a: array){
            sum += a;
        }
        return sum/array.length;
    }
}