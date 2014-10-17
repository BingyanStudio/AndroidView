package net.bingyan.androidview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2014/10/16 0016.
 */
public class PercentCycle extends View {
    private static float SQRT_2 = (float) Math.sqrt(2);
    private static int DEFAULT_COLOR = 0xffffffff;

    private float bigCircleRadius = 0;
    private int bigCircleColor = DEFAULT_COLOR;

    private float smallCircleDiameterRate = 0; // 按照跟大圆半径的比率，默认为斜杠的比率
    private int smallCircleColor = DEFAULT_COLOR;

    private float slashWidth = 0;
    private float slashLengthRate = 0; // 按照跟大圆直径的比率
    private int slashColor = DEFAULT_COLOR;

    private int numberTopLeft = 0;
    private float numberTopLeftSize = 0;
    private int numberTopLeftColor = DEFAULT_COLOR;

    private int numberBottomRight = 0;
    private float numberBottomRightSize = 0;
    private int numberBottomRightColor = DEFAULT_COLOR;

    private Paint paint;
    private Path path;

    public PercentCycle(Context context) {
        super(context);
    }

    public PercentCycle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PercentCycle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        path = new Path();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PercentCycle);
        bigCircleColor = array.getColor(R.styleable.PercentCycle_percent_big_circle_color, bigCircleColor);
        bigCircleRadius = array.getDimension(R.styleable.PercentCycle_percent_big_circle_radius, bigCircleRadius);
        smallCircleColor = array.getColor(R.styleable.PercentCycle_percent_small_circle_color, smallCircleColor);
        smallCircleDiameterRate = array.getFloat(R.styleable.PercentCycle_percent_small_circle_diameter_rate, smallCircleDiameterRate);
        slashColor = array.getColor(R.styleable.PercentCycle_percent_slash_color, slashColor);
        slashLengthRate = array.getFloat(R.styleable.PercentCycle_percent_slash_length_rate, slashLengthRate);
        slashWidth = array.getDimension(R.styleable.PercentCycle_percent_slash_width, slashWidth);
        numberTopLeftColor = array.getColor(R.styleable.PercentCycle_percent_number_top_left_color, numberTopLeftColor);
        numberTopLeftSize = array.getDimension(R.styleable.PercentCycle_percent_number_top_left_size, numberTopLeftSize);
        numberBottomRightColor = array.getColor(R.styleable.PercentCycle_percent_number_bottom_right_color, numberBottomRightColor);
        numberBottomRightSize = array.getDimension(R.styleable.PercentCycle_percent_number_bottom_right_size, numberBottomRightSize);
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas); // 画背景，边框之类的默认属性

        // 画大圆
        float centerX = getWidth() / 2.0f;
        float centerY = getHeight() / 2.0f;
        paint.setColor(bigCircleColor);
        canvas.drawCircle(centerX, centerY, bigCircleRadius, paint);

        // 画斜杠
        path.rewind();
        float lengthTemporary = slashLengthRate * bigCircleRadius / SQRT_2;
        float widthTemporary = slashWidth / SQRT_2 / 2;
        float topRightX = centerX + lengthTemporary;
        float topRightY = centerY - lengthTemporary;
        path.moveTo(topRightX - widthTemporary, topRightY - widthTemporary);
        path.lineTo(topRightX + widthTemporary, topRightY + widthTemporary);
        float bottomLeftX = centerX - lengthTemporary;
        float bottomLeftY = centerY + lengthTemporary;
        path.lineTo(bottomLeftX + widthTemporary, bottomLeftY + widthTemporary);
        path.lineTo(bottomLeftX - widthTemporary, bottomLeftY - widthTemporary);
        path.close();
        paint.setColor(slashColor);
        canvas.drawPath(path, paint);

        // 画小圆
        float halfBigCircleRadius = bigCircleRadius / 2;
        float temporaryBigCircleRadius = halfBigCircleRadius / SQRT_2;
        paint.setColor(smallCircleColor);
        canvas.drawCircle(centerX - temporaryBigCircleRadius, centerY - temporaryBigCircleRadius,
                smallCircleDiameterRate * halfBigCircleRadius, paint);

        // 画分子
        paint.setColor(numberTopLeftColor);
        paint.setTextSize(numberTopLeftSize);
        canvas.drawText(expandNumber(numberTopLeft), centerX - temporaryBigCircleRadius,
                centerY - temporaryBigCircleRadius - getTextOffset(), paint);

        // 画分母
        paint.setColor(numberBottomRightColor);
        paint.setTextSize(numberBottomRightSize);
        canvas.drawText(expandNumber(numberBottomRight), centerX + temporaryBigCircleRadius,
                centerY + temporaryBigCircleRadius - getTextOffset(), paint);
    }

    /**
     * @return 结果为负
     */
    private float getTextOffset() {
        return (paint.ascent() + paint.descent()) / 2;
    }

    /**
     * 两位数时无用，一位数时前补零
     */
    private String expandNumber(int number) {
        String result = String.valueOf(number);
        if (number < 10) result = '0' + result;
        return result;
    }

    public int getNumberTopLeft() {
        return numberTopLeft;
    }

    public void setNumberTopLeft(int numberTopLeft) {
        this.numberTopLeft = numberTopLeft;
        invalidate();
    }

    public int getNumberBottomRight() {
        return numberBottomRight;
    }

    public void setNumberBottomRight(int numberBottomRight) {
        this.numberBottomRight = numberBottomRight;
        invalidate();
    }
}
