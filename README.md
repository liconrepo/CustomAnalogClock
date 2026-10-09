# CustomAnalogClock

A customizable analog clock view for Android, written in Kotlin. The sample app shows
10 luxury-watch-inspired faces in a scrolling list: gold fluted bezels, octagonal bezels,
chronograph sub-dials, date windows, Roman and standard numerals, and a smoothly sweeping second hand.

## Watch faces

| Name | Highlights |
| --- | --- |
| Emerald Datejust | green dial, gold fluted bezel, date window |
| Royal Octagon | octagonal bezel with screws, navy grid dial |
| Tank Classic | ivory dial, Roman numerals, blue leaf hands |
| Panda Chronograph | white dial, three working sub-dials |
| Deep Diver | black dial, glowing dot markers |
| Pilot Aviator | black dial, large numerals |
| Calatrava Minimal | cream dial, rose-gold batons |
| Rose Gold Dress | champagne dial, Roman numerals, fluted bezel |
| Midnight Moonlight | deep blue dial, silver fluted bezel |
| Racing Red | red dial, sub-dials, numerals |

Brands other than the first face use the fictional name "LICON".

## Usage

`CustomAnalogClock` is a square view whose look is described entirely by a `WatchFace`.

```xml
<com.licon.customanalogclocks.CustomAnalogClock
    android:id="@+id/clock"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

```kotlin
clock.watchFace = WatchFaces.all[3]
```

### Creating your own face

Add a `WatchFace` to `WatchFaces.kt` (or build one anywhere) and assign it to the view:

```kotlin
WatchFace(
    name = "My Watch",
    brand = "MINE",
    metalLight = 0xFFF6E096.toInt(), metalDark = 0xFFA07628.toInt(),
    dialCenter = 0xFF105C3E.toInt(), dialEdge = 0xFF041E14.toInt(),
    textColor = 0xFFF6E096.toInt(), lumeColor = 0xFFECF4D6.toInt(),
    secondHandColor = 0xFFDEBE64.toInt(),
    bezelStyle = BezelStyle.FLUTED,   // FLUTED, SMOOTH, OCTAGON
    markerStyle = MarkerStyle.BATON,  // BATON, DOT, NUMBERS, ROMAN
    handStyle = HandStyle.SWORD,      // SWORD, LEAF
    dialPattern = DialPattern.GRID,   // SMOOTH, GRID
    hasDateWindow = true,
    hasSubDials = false,
)
```

## App icon

The launcher icon is a simple analog clock: white face, orange bezel, tick marks at 12, 3, 6 and 9,
navy hands and a red second hand on a navy background.

- Android 8.0+ uses an adaptive icon (`mipmap-anydpi-v26/ic_launcher.xml`) built from the vector
  drawables `ic_launcher_background.xml` and `ic_launcher_foreground.xml`.
- Older versions use the `ic_launcher.png` files in the `mipmap-*` folders.

## Building

Requires JDK 17 and the Android SDK (set `sdk.dir` in `local.properties` or `ANDROID_HOME`).

```sh
./gradlew assembleDebug
```

The APK is written to `app/build/outputs/apk/debug/`.

The project uses Gradle 8.9, Android Gradle Plugin 8.7.3, Kotlin 2.0.21,
`compileSdk`/`targetSdk` 35 and `minSdk` 21.

## License

See [LICENSE](LICENSE).
