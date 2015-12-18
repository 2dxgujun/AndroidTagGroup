# AndroidTagGroup

AndroidTagGroup is a special layout with a set of tags. You can use it to tag people, books or anything you want.

### Screenshot
![screenshot1](http://ww4.sinaimg.cn/large/bce2dea9jw1esbsby9v5fj20u00w8jxx.jpg)
![screenshot2](http://ww4.sinaimg.cn/large/bce2dea9jw1esbsbngv8fj20u005w75v.jpg)
![screenshot3](http://ww4.sinaimg.cn/large/bce2dea9jw1esbsbmoagij20u005sabl.jpg)

## Usage
### Step 1
Follow [these steps](https://jitpack.io/#alirezaaa/AndroidTagGroup/v1.4).

### Step 2
Use it in your own code:
```xml
<me.gujun.android.taggroup.AndroidTagGroup
    android:id="@+id/androidTagGroup"
    style="@style/AndroidTagGroup" />
```

```java
TagGroup mTagGroup = (TagGroup) findViewById(R.id.androidTagGroup);
mTagGroup.setTags(new String[]{"Tag 1", "Tag 2", "Tag 3"});
```

### Notes
- Use `setTags(...)` to set the initial tags in the group.
- To submit a new tag as user press "Enter" key or tap the blank area of the tag group, use `submitTag()`.
- To delete a tag as user press "Backspace", double-tap the tag which you want to delete.
- There are following interfaces to implement; `OnTagLimitationExceedListener`, `OnTagChangeListener`, and `OnTagClickListener`.

# Attributes
|           Attribute          	|     Default      |                         Description                         	 |
|:------------------------- |:---------------- |:------------------------------------------------------- |
| atg_isAppendMode        	| false            | Determine the TagGroup mode, APPEND or single DISPLAY.  |
| atg_inputHint   	        | Add Tag  | The hint of the INPUT tag.                              |
| atg_borderColor	          | #49C120          | The tag outline border color.                           |
| atg_textColor           	| #49C120          | The tag text color.                           	         |
| atg_backgroundColor       | #FFFFFF          | The tag background color.                               |
| atg_dashBorderColor       | #AAAAAA          | The tag dash outline border color.                      |
| atg_inputHintColor        | #80000000        | The input tag hint text color.                          |
| atg_inputTextColor        | #DE000000        | The input tag type text color..                         |
| atg_checkedBorderColor    | #49C120          | The checked tag outline border color.                   |
| atg_checkedTextColor      | #FFFFFF          | The checked text color.                                 |
| atg_checkedMarkerColor    | #FFFFFF          | The checked marker color.                               |
| atg_checkedBackgroundColor| #49C120          | The checked tag background color.                       |
| atg_pressedBackgroundColor| #EDEDED          | The tag background color when the tag is being pressed. |
| atg_borderStrokeWidth     | 0.5dp            | The tag outline border stroke width.        	           |
| atg_textSize          	  | 13sp             | The tag text size.                                  	   |
| atg_horizontalSpacing     | 8dp              | The horizontal tag spacing.(Mark1)                      |
| atg_verticalSpacing  	    | 4dp              | The vertical tag spacing.(Mark2)                      	 |
| atg_horizontalPadding	    | 12dp             | The horizontal tag padding.(Mark3)                      |
| atg_verticalPadding  	    | 3dp              | The vertical tag padding.(Mark4)                        |
| atg_tagsLimitation  	    | -1 (no limitation)                | Adding tags limitation                                  |
| atg_charsLimitation  	    | -1 (no limitation)                | Characters limitation                                  |

## Developed By
- Jun Gu - <2dxgujun@gmail.com>
- Alireza Eskandarpour Shoferi - <aesshoferi@gmail.com>

## License
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
