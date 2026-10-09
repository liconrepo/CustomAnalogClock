package com.licon.customanalogclocks

import androidx.annotation.ColorInt

/** The shape and finish of the ring around the dial. */
enum class BezelStyle {
    /** Round bezel with fine grooves cut around its edge. */
    FLUTED,

    /** Plain round bezel. */
    SMOOTH,

    /** Eight-sided bezel with a screw on each side. */
    OCTAGON,
}

/** How the twelve hour positions are marked on the dial. */
enum class MarkerStyle {
    /** Bars with a luminous inlay; the 12 o'clock bar is doubled. */
    BATON,

    /** Round luminous dots, larger at 12, 3, 6 and 9. */
    DOT,

    /** Numerals 1 to 12. */
    NUMBERS,

    /** Roman numerals I to XII, with the traditional "IIII" for four. */
    ROMAN,
}

/** The shape of the hour and minute hands. */
enum class HandStyle {
    /** Straight hands with a pointed tip and a luminous dot on the hour and minute hands. */
    SWORD,

    /** Slim hands that widen towards the middle and taper to a point. */
    LEAF,
}

/** The texture painted over the dial. */
enum class DialPattern {
    /** Plain dial with a radial gradient. */
    SMOOTH,

    /** Fine square grid over the gradient. */
    GRID,
}

/**
 * Describes everything [CustomAnalogClock] needs to look like one particular watch.
 *
 * All colors are ARGB ints.
 *
 * @property name Human readable name of the face, for example in a list.
 * @property brand Text printed on the upper half of the dial.
 * @property captionTop Small text under the [brand].
 * @property captionBottom Small text on the lower half of the dial; hidden when [hasSubDials] is set.
 * @property metalLight Highlight color of the metal parts (bezel, case ring, default markers and hands).
 * @property metalDark Shadow color of the metal parts.
 * @property dialCenter Dial color at the center.
 * @property dialEdge Dial color at the rim.
 * @property textColor Color of the [brand], captions, minute track and sub-dial outlines.
 * @property lumeColor Color of the luminous inlays on markers and hands.
 * @property secondHandColor Color of the second hand and the sub-dial hands.
 * @property markerColor Solid color for markers and numerals; the metal gradient is used when null.
 * @property handColor Solid color for the hour and minute hands; the metal gradient is used when null.
 * @property bezelStyle Shape and finish of the bezel.
 * @property markerStyle How the hours are marked.
 * @property handStyle Shape of the hour and minute hands.
 * @property dialPattern Texture of the dial.
 * @property hasDateWindow Whether a window showing the day of the month sits at 3 o'clock.
 * @property hasSubDials Whether chronograph style sub-dials show seconds (9), minutes (3) and hours (6).
 */
data class WatchFace(
    val name: String,
    val brand: String,
    val captionTop: String = "",
    val captionBottom: String = "",
    @ColorInt val metalLight: Int,
    @ColorInt val metalDark: Int,
    @ColorInt val dialCenter: Int,
    @ColorInt val dialEdge: Int,
    @ColorInt val textColor: Int,
    @ColorInt val lumeColor: Int,
    @ColorInt val secondHandColor: Int,
    @ColorInt val markerColor: Int? = null,
    @ColorInt val handColor: Int? = null,
    val bezelStyle: BezelStyle = BezelStyle.SMOOTH,
    val markerStyle: MarkerStyle = MarkerStyle.BATON,
    val handStyle: HandStyle = HandStyle.SWORD,
    val dialPattern: DialPattern = DialPattern.SMOOTH,
    val hasDateWindow: Boolean = false,
    val hasSubDials: Boolean = false,
)
