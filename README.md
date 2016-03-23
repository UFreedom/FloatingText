# FloatingText

FloatingText 是一个能够在任何控件之上执行漂浮效果动画的控件，

目前已有的漂浮效果有 '位移漂浮' ，'缩放漂浮'，'曲线路径漂浮',更重要的是 FloatingText 能够自定义漂浮效果.

<img src="/demo.gif" width="270" height="480" />



## 使用:

1. 使用 FloatingText.FloatingTextBuilder 去创建一个FloatingText，通过FloatingTextBuilder可以配置下面几个属性：

```

final FloatingText   floatingText = new FloatingText.FloatingTextBuilder(Activity)
                .textColor(Color.RED) // floating text color
                .textSize(100)   // floating text size
                .textContent("+1000") // floating text content
                .offsetX(100) // FloatingText 相对其所贴附View的水平位移偏移量
                .offsetY(100) // FloatingText 相对其所贴附View的垂直位移偏移量
                .floatingAnimatorEffect(FloatingAnimator) // 通过FloatingAnimator能够自定义漂浮动画
                .floatingPathEffect(FloatingPathEffect) // 通过FloatingPathEffect能够自定义漂浮的路径
                .build();

```

2.启动漂浮效果

```
             floatingText.attach2Window(); //将FloatingText贴附在Window上
             floatingText.startFly(View); // 传入一个View，FloatingText 就会相对于这个View执行漂浮效果
```


## 自定义：

#### 1.自定义漂浮动画

 通过实现 FloatingAnimator 接口可以实现自定义漂浮动画：

```
public interface FloatingAnimator {

     public void applyFloatingAnimation(FloatingTextView view);

 }

```

当执行动画时 FloatingAnimator 的 applyFloatingAnimation 方法会被回调，所以可以在 applyFloatingAnimation 中执行动画。


**ReboundFloatingAnimator**

ReboundFloatingAnimator 实现了 FloatingAnimator 并提供回弹功能，ReboundFloatingAnimator 使用 Facebook 的回弹动画库Rebound。

- 使用 `createSpringByBouncinessAndSpeed` 配置回弹动画的弹力和速度，并创建Spring。
- 使用 `createSpringByTensionAndFriction` 配置回弹动画的张力和摩擦力，并创建Spring
- 调用 `transition(float progress, float startValue, float endValue)` 并传入当前动画的进度，动画的起始值，结束值参数可以获取当前的动画值

目前库内的 ScaleFloatingAnimator ，TranslateFloatingAnimator，BaseFloatingPathAnimator 都是实现了 ReboundFloatingAnimator。

例如 缩放回弹效果: ScaleFloatingAnimator

```
public class ScaleFloatingAnimator extends ReboundFloatingAnimator {

    public long duration;
    public ScaleFloatingAnimator() {
        duration = 1000;
    }

    public ScaleFloatingAnimator(long duration) {
        this.duration = duration;
    }

    @Override
    public void applyFloatingAnimation(final FloatingTextView view) {
        Spring scaleAnim = createSpringByBouncinessAndSpeed(10, 15)
                .addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        float transition = transition((float) spring.getCurrentValue(), 0.0f, 1.0f);
                        view.setScaleX(transition);
                        view.setScaleY(transition);
                    }
                });
        ValueAnimator alphaAnimator = ObjectAnimator.ofFloat(1.0f, 0.0f);
        alphaAnimator.setDuration(duration);
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });
        scaleAnim.setEndValue(1f);
        alphaAnimator.start();
    }

}

```

#### 2.自定义漂浮路径

通过实现 FloatingPathEffect 和 FloatingPathAnimator 可以自定义路径动画

- FloatingPathEffect 代表浮动路径
```

public interface   FloatingPathEffect {

    abstract Path getFloatingPath(FloatingTextView floatingTextView);

    abstract PathMeasure getFloatPathMeasure(Path path,FloatingTextView floatingTextView);

}

```

其中 getFloatingPath(FloatingTextView floatingTextView) 用于配置 浮动的路径，getFloatPathMeasure(Path path,FloatingTextView floatingTextView) 用于计算路径并使用动画执行路径

例如：CurveFloatingPathEffect 实现了曲线路径效果:

```
public class CurveFloatingPathEffect implements FloatingPathEffect {


    @Override
    public Path getFloatingPath(FloatingTextView floatingTextView) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.quadTo(-100, -200, 0, -300);
        path.quadTo(200, -400, 0, -500);
        return path;
    }

    @Override
    public PathMeasure getFloatPathMeasure(Path path, FloatingTextView floatingTextView) {
        return new PathMeasure(path, false);
    }
}


```

####  3.定义路径漂浮动画

当使用 FloatingPathEffect 定义完 路径后，就可以实现 BaseFloatingPathAnimator 来执行路径动画

```
public abstract class BaseFloatingPathAnimator extends ReboundFloatingAnimator implements FloatingPathAnimator {

    private PathMeasure pathMeasure;
    private float pos[];

    public float[] getFloatingPosition(float progress) {
        pathMeasure.getPosTan(progress, pos, null);
        return pos;
    }

    @Override
    public void applyFloatingAnimation(FloatingTextView view) {
        pathMeasure = view.getPathMeasure();
        if (pathMeasure == null) {
            return;
        }
        pos = new float[2];
        applyFloatingPathAnimation(view,0,pathMeasure.getLength());
    }

}

```

- 通过 getFloatingPosition(float progress) 可以获取当前路径动画的位置信息 [0]代表x值，[1]代表y值

- 在 applyFloatingPathAnimation(final FloatingTextView view, float start, float end) 实现路径动画
参数 start 代表路径的开始位置，end代表路径的结束位置.

例如 CurvePathFloatingAnimator 的效果是曲线漂浮动画:
