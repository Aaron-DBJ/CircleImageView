package com.arron_dbj.circleimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CircleImageView extends View {
    /**
     * 图形画笔
     */
    private Paint mPaint;

    /**
     * 绘制在所画区域的贴图
     */
    private Bitmap bitmap;

    /**
     * 图片着色器
     */
    private BitmapShader shader;

    /**
     * 绘制边框的画笔
     */
    private Paint mBorderPaint;

    /**
     * 图形形状，0表示圆形，1表示圆角矩形
     */
    private int enumShapeStyle = 0;

    /**
     * 圆角半径，暂时长半轴和短半轴设置为一样
     */
    private float roundRadiusX = 5;
    private float roundRadiusY = 5;

    /**
     * 边框宽度
     */
    private float borderWidth = 0;

    /**
     * 边框颜色
     */
    private int borderColor = Color.TRANSPARENT;

    /**
     * 圆形视图的圆心x轴坐标
     */
    float circleX;

    /**
     * 圆形视图的圆心y轴坐标
     */
    float circleY;

    /**
     * 构造函数
     * @param context
     */
    public CircleImageView(Context context) {
        super(context);
    }
    /**
     * 构造函数, 并初始化属性和画笔
     * @param context
     * @param attrs
     */
    public CircleImageView(Context context, AttributeSet attrs) throws Exception{
        super(context, attrs);
        init(context, attrs);
    }


    /**
     * 主要用来处理视图宽或高为wrap_content的情况
     * 测量视图，精确模式下直接使用设置的宽高；
     * 如果宽高设置的是wrap_content，则使用图片的宽或高作为视图的宽或高。
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(width, height);
        }else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.EXACTLY){
            setMeasuredDimension(width, heightSize);
        }else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSize, height);
        }else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY){
            setMeasuredDimension(widthSize, heightSize);
        }
    }

    /**
     * 初始化属性和画笔
     * @param context
     * @param attrs
     * @throws Exception
     */
    private void init(Context context, AttributeSet attrs) throws Exception{
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        /**
         * 提取属性定义
         */
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        int bitmapId = typedArray.getResourceId(R.styleable.CircleImageView_src, -1);
        if (bitmapId == -1){
            throw new Exception("需要定义图片资源。");
        }
        bitmap = BitmapFactory.decodeResource(getResources(), bitmapId);
        enumShapeStyle = typedArray.getInt(R.styleable.CircleImageView_shapeStyle, 0);
        if (enumShapeStyle == 1){
            roundRadiusX = typedArray.getDimension(R.styleable.CircleImageView_roundRadiusX, 5);
            roundRadiusY = typedArray.getDimension(R.styleable.CircleImageView_roundRadiusY, 5);
        }
        roundRadiusX = typedArray.getDimension(R.styleable.CircleImageView_roundRadiusX, 0);
        roundRadiusY = typedArray.getDimension(R.styleable.CircleImageView_roundRadiusY, 0);
        borderWidth = typedArray.getDimension(R.styleable.CircleImageView_borderWidth, 0);
        borderColor = typedArray.getColor(R.styleable.CircleImageView_borderColor, Color.BLACK);
        //必须调用recycle方法来回收typearray，以便以后复用
        typedArray.recycle();
        initPaint();
    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStrokeWidth(borderWidth);
        mBorderPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 绘制视图
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //使用变换矩阵，将BitmapShader变换到与控件宽或高相同
        Matrix matrix = new Matrix();
        float scaleX = (float) getWidth() / bitmap.getWidth();
        float scaleY = (float) getHeight() / bitmap.getHeight();
        Log.d("MainActivity", "缩放比例scaleX："+scaleX+ ";"+ "scaleY: "+ scaleY);
        //从x方向和y方向放缩
        matrix.setScale(scaleX, scaleY);
        shader.setLocalMatrix(matrix);
        mPaint.setShader(shader);
        circleX = getWidth() / 2;
        circleY = getHeight() / 2;

        if (enumShapeStyle == 0){
            //这里减10主要是使圆形视图缩小一点，为边框留出空间
            canvas.drawCircle(circleX, circleY, Math.min(getWidth() / 2, getHeight() / 2) - 10, mPaint);
            drawBorder(canvas);
        }else if (enumShapeStyle == 1) {
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), roundRadiusX, roundRadiusY, mPaint);
            drawBorder(canvas);
        }
    }

    private void drawBorder(Canvas canvas){
        switch (getShapeStyle()) {
            case 0:
                if (borderWidth == 0) {
                    mBorderPaint.setAlpha(0);
                    canvas.drawCircle(circleX, circleY, Math.min(getWidth() / 2, getHeight() / 2), mBorderPaint);
                } else if (borderWidth > 15 && borderWidth < dp2px(getContext(), 15)) {
                    canvas.drawCircle(circleX, circleY, Math.min(getWidth() / 2, getHeight() / 2) - 20, mBorderPaint);
                } else if (borderWidth >= dp2px(getContext(), 15)) {
                    mBorderPaint.setStrokeWidth(dp2px(getContext(), 15));
                    canvas.drawCircle(circleX, circleY, Math.min(getWidth() / 2, getHeight() / 2) - 25, mBorderPaint);
                } else {
                    canvas.drawCircle(circleX, circleY, Math.min(getWidth() / 2, getHeight() / 2) - 10, mBorderPaint);
                }
                break;
            case 1:
                /**
                 * 直接这样绘制圆角矩形边框会发现四个圆角的线宽要大于四边直线线宽。
                 * 出现这种问题的原因是，使用Style.Stroke绘制粗线，基准线在中线，
                 * stroke边由中线向内外扩展。直接以矩形的最外一圈作为基准线，
                 * 导致直线边只能绘制向里的一半线宽。
                 */
                if (borderWidth <= dp2px(getContext(), 15)) {
                    float halfStrokeWidth = borderWidth / 2;
                    canvas.drawRoundRect(new RectF(halfStrokeWidth, halfStrokeWidth, getWidth() - halfStrokeWidth,
                            getHeight() - halfStrokeWidth), roundRadiusX, roundRadiusY, mBorderPaint);
                }else {
                    mBorderPaint.setStrokeWidth(dp2px(getContext(), 15));
                    float halfStrokeWidth = borderWidth / 2;
                    canvas.drawRoundRect(new RectF(halfStrokeWidth, halfStrokeWidth, getWidth() - halfStrokeWidth,
                            getHeight() - halfStrokeWidth), roundRadiusX, roundRadiusY, mBorderPaint);
                }
                break;
        }
    }
    private int getShapeStyle(){
        return enumShapeStyle;
    }

    private int dp2px(Context context, int dpValue){
        float scale = context.getResources().getDisplayMetrics().density;
        Log.d("dp2px", (int) (dpValue*scale + 0.5f)+"");
        return (int) (dpValue*scale + 0.5f);
    }

}
