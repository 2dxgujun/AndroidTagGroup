# `AndroidTagGroup`

[![Join the chat at https://gitter.im/leonacky/AndroidTagGroup](https://badges.gitter.im/leonacky/AndroidTagGroup.svg)](https://gitter.im/leonacky/AndroidTagGroup?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

The TagGroup is a special layout with a set of tags forked from 2dxgujun/AndroidTagGroup
. I have implemented:
1. TagGravity
2. Tag Selected
3. Get all tags is selected

# Demo

### Screenshot
![screenshot1](http://ww4.sinaimg.cn/large/bce2dea9jw1esbsby9v5fj20u00w8jxx.jpg)

### Edit Tags
![screenshot2](http://ww4.sinaimg.cn/large/bce2dea9jw1esbsbngv8fj20u005w75v.jpg)
![screenshot3](http://ww4.sinaimg.cn/large/bce2dea9jw1esbsbmoagij20u005sabl.jpg)

### Tag Gravity
![screenshot4](https://dl.dropboxusercontent.com/u/25101600/tag_gravity.png)

[Download Demo](https://dl.dropboxusercontent.com/u/25101600/TagGroup-demo-release.apk)

# Usage

## Step 1

#### Gradle
```groovy
dependencies {
   compile 'com.aotasoft.taggroup:library:1.6'
}
```

## Step 2

Use it in your own code:
```xml
<com.aotasoft.taggroup.TagGroup
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

#### How to set a tag is selected?

To set tag is sellected for TagGroup: use `tagroup.setSelectedTag(...)`

#### How to get all tags is selected?

To get all tags is sellected for TagGroup: use `tagroup.getSelectedTags(...)` will return all position of tag is selected

#### Gravity for TagGroup

To set gravity for TagGroup: use `setGravity(...)` supported `TagGroup.Gravity.LEFT`, `TagGroup.Gravity.MIDDLE` and `TagGroup.Gravity.RIGHT`

# Build

run `./gradlew assembleDebug` (Mac/Linux)

or

run `gradlew.bat assembleDebug` (Windows)

# Attributes

There are several attributes you can set:

![Dimension illustrate](http://ww2.sinaimg.cn/large/bce2dea9gw1epov0i8x6kj20rk054q4g.jpg)

|           attr        	|     default      |                         mean                          	 |
|:------------------------- |:---------------- |:------------------------------------------------------- |
| atg_isAppendMode      	| false            | Determine the TagGroup mode, APPEND or single DISPLAY.  |
| atg_inputHint   	        | Add Tag  | The hint of the INPUT tag.                              |
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

# Developed By
Tuan Dinh - <leonacky@gmail.com> from origin project 2dxgujun/AndroidTagGroup

# License

    Copyright 2015 Tuan Dinh

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
