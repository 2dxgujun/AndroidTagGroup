## AndroidTagGroup
---

The AndroidTagGroup is a layout for a set of tags.

You can use it to group people, books or anything you want.

And also you can contribute new idea to me.


### Demo
---

Screenshot:
![Demo screenshot](https://raw.github.com/2dxgujun/AndroidTagGroup/master/assets/demo_screenshot.png)

Append tag:
![Append mode](https://raw.github.com/2dxgujun/AndroidTagGroup/master/assets/append_mode.png)

Delete tag:
![Delete tag](https://raw.github.com/2dxgujun/AndroidTagGroup/master/assets/delete_tag.png)

[Download Demo]()

### Usage
---

#### Gradle

#### Maven


Use it in your own code:
```xml
<me.gujun.android.taggroup.TagGroup
    android:id="@+id/tag_group"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content">
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


### Build

run `./gradlew assembleDebug` (Mac/Linux)

or

run `gradlew.bat assembleDebug` (Windows)

### Attributes

There are several attributes you can set:

![Dimension illustrate](https://raw.github.com/2dxgujun/AndroidTagGroup/master/assets/dimension_illustrate.png)

The  **dimension**:

- Horizontal tag spacing (Mark 1)
- Vertical tag spacing (Mark 2)
- Horizontal tag padding (Mark 3)
- Vertical tag padding (Mark 4)
- Tag text size
- Tag outline border stroke width

The **color**:

- Bright tag color
- Dim tag color

The **mode** related:

- Append mode flag
- Append mode INPUT state tag hint

For example, the default style:

```xml
<style name="TagGroup">
    <item name="android:layout_width">match_parent</item>
    <item name="android:layout_height">wrap_content</item>
    <item name="isAppendMode">false</item>
    <item name="inputTagHint">@string/add_tag</item>
    <item name="brightColor">#49C120</item>
    <item name="dimColor">#AAAAAA</item>
    <item name="borderStrokeWidth">0.5dp</item>
    <item name="textSize">13sp</item>
    <item name="horizontalSpacing">8dp</item>
    <item name="verticalSpacing">4dp</item>
    <item name="horizontalPadding">12dp</item>
    <item name="verticalPadding">3dp</item>
</style>
```

License
---

This project is licensed under the [MIT License](https://raw.github.com/2dxgujun/AndroidTagGroup/master/assets/LICENSE).