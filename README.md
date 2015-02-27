# AndroidTagGroup

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.gujun.android.taggroup/library/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/me.gujun.android.taggroup/library)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AndroidTagGroup-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1539)
[![Build Status](https://travis-ci.org/2dxgujun/AndroidTagGroup.png?branch=master)](https://travis-ci.org/2dxgujun/AndroidTagGroup)

The AndroidTagGroup is a layout for a set of tags.You can use it to group people, books or anything you want.

Also you can contribute new idea to me.


# Demo

### Screenshot
![Demo screenshot](https://raw.github.com/2dxgujun/AndroidTagGroup/master/assets/demo_screenshot.png)

### Append Tag
![Append mode](https://raw.github.com/2dxgujun/AndroidTagGroup/master/assets/append_mode.png)

### Delete tag
![Delete tag](https://raw.github.com/2dxgujun/AndroidTagGroup/master/assets/delete_tag.png)

[Download Demo](https://github.com/2dxgujun/AndroidTagGroup/releases/download/v1.0/AndroidTagGroup-Demo-v1.0.apk)

# Usage

## Step 1

#### Gradle
```groovy
dependencies {
   compile 'me.gujun.android.taggroup:library:1.1@aar'
}
```

#### Maven
```xml
<dependency>
    <groupId>me.gujun.android.taggroup</groupId>
    <artifactId>library</artifactId>
    <version>1.1</version>
    <type>apklib</type>
</dependency>
```

## Step 2

Use it in your own code:
```xml
<me.gujun.android.taggroup.TagGroup
    android:id="@+id/tag_group"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content">
```

```java
TagGroup mTagGroup = (TagGroup) findViewById(R.id.tag_group);
mTagGroup.setTags(new String[]{"Tag1", "Tag2", "Tag3"});
```

I made some pre-design style. You can use them via `style` property.

![Present color](https://raw.github.com/2dxgujun/AndroidTagGroup/master/assets/present_color.png)

Use the present style just like below:

```xml
<me.gujun.android.taggroup.TagGroup
    android:id="@+id/tag_group"
    style="@style/TagGroup.Beauty_Red"/>
```

In the above picture, the style is:

`TagGroup`
`TagGroup.Beauty_Red`
`TagGroup.Holo_Dark`
`TagGroup.Light_Blue`
`TagGroup.Indigo`

You can get more beautiful color from [Adobe Color CC](https://color.adobe.com), and you can also contribute your color style to AndroidTagGroup!

# Build

run `./gradlew assembleDebug` (Mac/Linux)

or

run `gradlew.bat assembleDebug` (Windows)

# Attributes

There are several attributes you can set:

![Dimension illustrate](https://raw.github.com/2dxgujun/AndroidTagGroup/master/assets/dimension_illustrate.png)

|       attr      	|     default      |                         mean                          	 |
|:-----------------:|:----------------:|:-------------------------------------------------------:|
| isAppendMode  	| false            | Determine the TagGroup mode, APPEND or single DISPLAY.  |
| inputTagHint   	| Add Tag/添加标签  | Hint of the INPUT state tag.                            |
| brightColor	    | #49C120          | The bright color of the tag.                            |
| dimColor       	| #AAAAAA          | The dim color of the tag.                           	 |
| borderStrokeWidth | 0.5dp            | The tag outline border stroke width.        	         |
| textSize      	| 13sp             | The tag text size.                                  	 |
| horizontalSpacing | 8dp              | The horizontal tag spacing.(Mark1)                      |
| verticalSpacing  	| 4dp              | The vertical tag spacing.(Mark2)                      	 |
| horizontalPadding	| 12dp             | The horizontal tag padding.(Mark3)                      |
| verticalPadding  	| 3dp              | The vertical tag padding.(Mark4)                        |

# Developed By

Jun Gu - <2dxgujun@gmail.com>

<a href="http://weibo.com/2dxgujun">
  <img alt="Follow me on Weibo" src="http://ww4.sinaimg.cn/large/bce2dea9gw1epjhk9h9m6j20230233yb.jpg"/>
</a>
<a href="https://plus.google.com/u/0/113657331852211913645">
  <img alt="Follow me on Google Plus" src="http://ww1.sinaimg.cn/large/bce2dea9gw1epjhbx0ouij2023023jr6.jpg"/>
</a>

# License

    Copyright 2015 Jun Gu

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.