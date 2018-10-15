# CircleImageView

一个可以显示圆形图像和圆角矩形图像的控件。

## 效果图
不带边框的圆形图像。
![圆形图像，borderWidth="0dp"](https://github.com/Aaron-DBJ/CircleImageView/blob/master/screenshots/borderwidth_0dp.jpg)

边框宽度为1dp的圆形图像。                           
![圆形视图，borderWidth="1dp"](https://github.com/Aaron-DBJ/CircleImageView/blob/master/screenshots/borderwidth_1dp.jpg)

边框宽度为6dp的圆形图像。
![圆形视图，borderWidth="6dp"]((https://github.com/Aaron-DBJ/CircleImageView/blob/master/screenshots/borderwidth_6dp.jpg)

边框宽度为15dp的圆形图像。
![圆形视图，borderWidth="15dp"](https://github.com/Aaron-DBJ/CircleImageView/blob/master/screenshots/borderwidth_15dp.jpg)

圆角矩形，圆角半径10dp。
![圆角矩形视图，圆角半径10dp]((https://github.com/Aaron-DBJ/CircleImageView/blob/master/screenshots/roundRectangle.jpg)


## Gradle

```Java
dependencies {
    ...
    implementation 'com.github.aaron_dbj:circleimageview:1.0.4'
}
```



## 使用方法

圆角矩形

```java
<com.arron_dbj.circleimageview.CircleImageView
    app:src="@drawable/drawable"
    app:shapeStyle="roundRect"
    app:roundRadius="8dp"
    android:layout_width="100dp"
    android:layout_height="100dp" />
```

圆形

```java
<com.arron_dbj.circleimageview.CircleImageView
    app:src="@drawable/drawble"
    app:shapeStyle="circle"
    app:borderWidth="8dp"
    app:borderColor="#000000"
    android:layout_width="100dp"
    android:layout_height="100dp" />
```

[![](https://jitpack.io/v/Aaron-DBJ/CircleImageView.svg)](https://jitpack.io/#Aaron-DBJ/CircleImageView)

