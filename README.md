# `Droid Tag Group 2107 version`

[![](https://jitpack.io/v/LorenzoZaccagnini/DroidTag.svg)](https://jitpack.io/#LorenzoZaccagnini/DroidTag)

The TagGroup is a special layout with a set of tags. You can use it to tag people, books or anything you want.

Also you can contribute new idea to me.

# Demo

### Edit Tags
![screenshot1](https://preview.ibb.co/bBobP5/normaltag_Crop.png)
![screenshot1](https://preview.ibb.co/jMKfHQ/autocomplete_Crop.png)


# Usage

## Step 1

#### Gradle
```groovy
dependencies {
compile 'com.github.LorenzoZaccagnini:DroidTag:1.6'}
```

#### Maven
```xml
	<dependency>
	    <groupId>com.github.LorenzoZaccagnini</groupId>
	    <artifactId>DroidTag</artifactId>
	    <version>1.6</version>
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

#### How to delete a tag?

To delete a tag as user press "Backspace" key or double-tap the tag which you want to delete.


#### How to add an autocomplete list?

Just add a string array, this an example for a fragment
	
	private String[] suggestionsArray = { "Tag1", "Tag2", "Tag3", "Tag4", "Tag5"};
        
	mTagGroup.setOnTagCharEntryListener(new TagGroup.OnTagCharEntryListener() {
            @Override
            public void onCharEntry(String text) {
		
                    mTagGroup.getTagView().setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, suggestionsArray));
            }
        });

#### How to detect tag click event?

Implement a callback interface: `TagGroup.OnTagClickListener`, and set the listener via `setOnTagClickListener()`.


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
| atg_inputHint   	        | Add Tag/添加标签  | The hint of the INPUT tag.                              |
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

# New version by

Lorenzo Zaccagnini - <info@nanadevs.com>

<a href="https://www.facebook.com/DrLorenzoZaccagnini">
  <img alt="Follow me on Facebook" src="https://www.brandsbay.com/media/wysiwyg/facebook-icon.jpg"/>
</a>
<a href="https://www.linkedin.com/in/lorenzo-zaccagnini/">
  <img alt="Follow me on LinkedIn" src="https://www.rochester.edu/templatefiles/rwd/img/social-linkedin.png"/>
</a>

# Fork of

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
