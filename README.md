# FloatingText

[![Build Status](https://travis-ci.org/UFreedom/FloatingText.svg?branch=master)](https://travis-ci.org/UFreedom/FloatingText) [ [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-FloatingText-green.svg?style=true)](https://android-arsenal.com/details/1/3360) ![Download](https://api.bintray.com/packages/ufreedom/maven/FloatingTextLibrary/images/download.svg) ](https://bintray.com/ufreedom/maven/FloatingTextLibrary/_latestVersion)


FloatingText is a text widget that can floating above view with animation .

Now we have 'Scale Floating','Scale Floating','Curve Floating',and you can also design custom a animation.

[中文说明](https://github.com/UFreedom/FloatingText/blob/master/README_CN.md)


<img src="/demo.gif" width="270" height="480" />

# Compatibility ![Requirements](https://img.shields.io/badge/Requirements-Android%203.0%2B-green.svg)

  * Library - Android Honeycomb 3.0+
  * Sample - Android Honeycomb 3.0+


## Usage:

1.Add Snapshot repository and add to dependencies:

```groovy
dependencies {

     compile 'com.ufreedom.uikit:FloatingTextLibrary:0.1.0'
}

```

2. Use FloatingText.FloatingTextBuilder to create a FloatingText：

```java
 FloatingText   floatingText = new FloatingText.FloatingTextBuilder(Activity)
                               .textColor(Color.RED) // floating  text color
                               .textSize(100)   // floating  text size
                               .textContent("+1000") // floating  text content
                               .offsetX(100) // the x offset  relate to the attached view
                               .offsetY(100) // the y offset  relate to the attached view  
                               .floatingAnimatorEffect(FloatingAnimator) // floating animation
                               .floatingPathEffect(FloatingPathEffect) // floating path
                               .build();

floatingText.attach2Window(); // let FloatingText attached to the Window
```

3.Start floating

```java
floatingText.startFloating(View); // FloatingText do floating animation relate to the view
```



## Customisation:

#### 1.Coordinates

 <img src="/coordinates.png" width="423" height="419"/>



#### 2.Floating Animation

 Implements `FloatingAnimator` interface and do animation in the `applyFloatingAnimation` method：

```java
public interface FloatingAnimator {
     public void applyFloatingAnimation(FloatingTextView view);
}
```

**[ReboundFloatingAnimator](https://github.com/UFreedom/FloatingText/blob/master/FloatingTextLibrary%2Fsrc%2Fmain%2Fjava%2Fcom%2Fufreedom%2Feffect%2FReboundFloatingAnimator.java)**

`ReboundFloatingAnimator` implements FloatingAnimator and support rebound animation.The rebound animation is  Facebook's library [Rebound](https://github.com/facebook/rebound)。

There are  three help methods;

- `createSpringByBouncinessAndSpeed` : config rebound  width bounciness and speed
- `createSpringByTensionAndFriction` : config rebound  width tension and friction
- `transition(float progress, float startValue, float endValue)`: use the method to get animated value .

progress : the progress of current animation
startValue : the start value of your animation
endValue : the end value of your animation

In this library : [ScaleFloatingAnimator](https://github.com/UFreedom/FloatingText/blob/master/FloatingTextLibrary%2Fsrc%2Fmain%2Fjava%2Fcom%2Fufreedom%2Feffect%2FScaleFloatingAnimator.java)  ，[TranslateFloatingAnimator](https://github.com/UFreedom/FloatingText/blob/master/FloatingTextLibrary%2Fsrc%2Fmain%2Fjava%2Fcom%2Fufreedom%2Feffect%2FTranslateFloatingAnimator.java)，[BaseFloatingPathAnimator](https://github.com/UFreedom/FloatingText/blob/master/FloatingTextLibrary%2Fsrc%2Fmain%2Fjava%2Fcom%2Fufreedom%2FBaseFloatingPathAnimator.java) all implements `ReboundFloatingAnimator` interface。

For example : `ScaleFloatingAnimator`

```java
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

#### 3.Floating Path

Implements `FloatingPathEffect` and `FloatingPathAnimator` interface can custom you own floating path animation.

- `FloatingPath` is the floating path

```java
public interface FloatingPathEffect {

    abstract FloatingPath getFloatingPath(FloatingTextView floatingTextView);

}
```

create a path  and then  use `FloatingPath.create(Path path, boolean forceClose)` to create FloatingPath.
if the forceClose's value is true,the path will be forced to close .

For example：CurveFloatingPathEffect :

```java
public class CurveFloatingPathEffect implements FloatingPathEffect {

  @Override
     public FloatingPath getFloatingPath(FloatingTextView floatingTextView) {
         Path path = new Path();
         path.moveTo(0, 0);
         path.quadTo(-100, -200, 0, -300);
         path.quadTo(200, -400, 0, -500);
         return FloatingPath.create(path, false);
     }
}
```

####  4.Floating Path Animation

After use  `FloatingPathEffect` to define the path,Then you should implements the  `BaseFloatingPathAnimator` interface and do path animation.

For example: `CurvePathFloatingAnimator` ：


```java
public class CurvePathFloatingAnimator extends BaseFloatingPathAnimator {

    @Override
    public void applyFloatingPathAnimation(final FloatingTextView view, float start, float end) {

        ValueAnimator translateAnimator = ObjectAnimator.ofFloat(start, end);
        translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                float pos[] = getFloatingPosition(value);
                float x = pos[0];
                float y = pos[1];
                view.setTranslationX(x);
                view.setTranslationY(y);

            }
        });

        translateAnimator.setDuration(3000);
        translateAnimator.setStartDelay(50);
        translateAnimator.start();

    }
}
```

-  `applyFloatingPathAnimation(final FloatingTextView view, float start, float end)` :

  The param 'start' is the start value of the path,and the param 'end' is the end value of the path.

- `getFloatingPosition(float progress)` :

  get the current animated path value ,[0] is x，[1] is y.


# Changelog


### Version: 0.2.0

* Fix the position bug - Thanks [MarsVard](https://github.com/MarsVard) contribution


### Version: 0.1.0

* Initial Build



License ![License](https://img.shields.io/hexpm/l/plug.svg)
--------

    Copyright 2015 UFreedom

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
