# `AndroidTagGroup`

[![Release 1.5](https://img.shields.io/badge/Release-1.5-green.svg)](https://github.com/alhazmy13/AndroidTagGroup/releases)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.gujun.android.taggroup/library/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/me.gujun.android.taggroup/library)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AndroidTagGroup-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1539)


The TagGroup is a special layout with a set of tags. You can use it to tag people, books or anything you want.



### Screenshot
![screenshot1](https://cloud.githubusercontent.com/assets/4659608/17962806/2570fca0-6abb-11e6-93c8-62287977a029.gif)


# Usage

## Step 1

#### Gradle
```groovy
dependencies {
   compile 'net.alhazmy13.androidtaggroup:library:1.5.0'
}
```

#### Maven
```xml
<dependency>
    <groupId>net.alhazmy13.androidtaggroup</groupId>
    <artifactId>library</artifactId>
    <version>1.5.0</version>
    <type>apklib</type>
</dependency>
```

## Step 2

Use it in your own code:
```xml
<me.gujun.android.taggroup.TagGroup
    android:id="@+id/tag_group"
    style="@style/TagGroup" />
```

```java
TagGroup mTagGroup = (TagGroup) findViewById(R.id.tag_group);
mTagGroup.setTags(new String[]{"Tag1", "Tag2", "Tag3"});
```
Use `setTags(...)` to set the initial tags in the group.

#### How to submit a new tag?

To "submit" a new tag as user press "Enter" or tap the blank area of the tag group, also you can "submit" a new tag via `submitTag()`.

**Note**: Google keyboard (a few soft keyboard not honour the key event) currently not supported "Enter" key to "submit" a new tag.

#### How to delete a tag?

To delete a tag as user press "Backspace" key or double-tap the tag which you want to delete.

#### How to detect tag click event?

Implement a callback interface: `TagGroup.OnTagClickListener`, and set the listener via `setOnTagClickListener()`.



# Attributes

There are several attributes you can set:

![Dimension illustrate](http://ww2.sinaimg.cn/large/bce2dea9gw1epov0i8x6kj20rk054q4g.jpg)

|           attr        	|     default      |                         mean                          	 |
|:------------------------- |:---------------- |:------------------------------------------------------- |
| atg_isAppendMode      	| false            | Determine the TagGroup mode, APPEND or single DISPLAY.  |
| atg_showRemoveConfirmation      	| false            | To show confirmation alert before remove any tag  |
| atg_inputHint   	        | null  | The hint of the INPUT tag.                              |
| atg_borderColor	        | #49C120          | The tag outline border color.                           |
| atg_textColor          	| #49C120          | The tag text color.                           	         |
| atg_backgroundColor       | #FFFFFF          | The tag background color.                               |
| atg_dashBorderColor       | #AAAAAA          | The tag dash outline border color.                      |
| atg_inputHintColor        | #80000000        | The input tag hint text color.                          |
| atg_inputTextColor        | #DE000000        | The input tag type text color..                         |
| atg_checkedBorderColor    | #49C120          | The checked tag outline border color.                   |
| atg_checkedTextColor      | #FFFFFF          | The checked text color.                                 |
| atg_checkedMarkerColor    | #FFFFFF          | The checked marker color.                               |
| atg_checkedBackgroundColor| #49C120          | The checked tag background color.                       |
| atg_pressedBackgroundColor| #EDEDED          | The tag background color when the tag is being pressed. |
| atg_borderStrokeWidth     | 0.5dp            | The tag outline border stroke width.        	         |
| atg_textSize          	| 13sp             | The tag text size.                                  	 |
| atg_horizontalSpacing     | 8dp              | The horizontal tag spacing.(Mark1)                      |
| atg_verticalSpacing  	    | 4dp              | The vertical tag spacing.(Mark2)                      	 |
| atg_horizontalPadding	    | 12dp             | The horizontal tag padding.(Mark3)                      |
| atg_verticalPadding  	    | 3dp              | The vertical tag padding.(Mark4)                        |


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
