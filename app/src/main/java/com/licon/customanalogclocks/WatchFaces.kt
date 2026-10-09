package com.licon.customanalogclocks

import androidx.annotation.ColorInt

/** The watch faces shown by the sample app. */
object WatchFaces {

    /** Every available face; the first one is the default for a new [CustomAnalogClock]. */
    val all: List<WatchFace> = listOf(
        WatchFace(
            name = "Emerald Datejust",
            brand = "ROLEX",
            captionTop = "OYSTER PERPETUAL",
            captionBottom = "DATEJUST",
            metalLight = rgb(0xF6E096), metalDark = rgb(0xA07628),
            dialCenter = rgb(0x105C3E), dialEdge = rgb(0x041E14),
            textColor = rgb(0xF6E096), lumeColor = rgb(0xECF4D6), secondHandColor = rgb(0xDEBE64),
            bezelStyle = BezelStyle.FLUTED,
            hasDateWindow = true,
        ),
        WatchFace(
            name = "Royal Octagon",
            brand = "LICON",
            captionTop = "AUTOMATIC",
            captionBottom = "TAPISSERIE",
            metalLight = rgb(0xF2F4F7), metalDark = rgb(0x7B828C),
            dialCenter = rgb(0x1E3F78), dialEdge = rgb(0x0A1A3A),
            textColor = rgb(0xE8ECF2), lumeColor = rgb(0xF4F7E8), secondHandColor = rgb(0xE8ECF2),
            bezelStyle = BezelStyle.OCTAGON,
            dialPattern = DialPattern.GRID,
            hasDateWindow = true,
        ),
        WatchFace(
            name = "Tank Classic",
            brand = "LICON PARIS",
            captionBottom = "SWISS MADE",
            metalLight = rgb(0xF4F1E8), metalDark = rgb(0x8A8578),
            dialCenter = rgb(0xFBF6E6), dialEdge = rgb(0xE6DCC0),
            textColor = rgb(0x2A2A33), lumeColor = rgb(0xFBF6E6), secondHandColor = rgb(0x1F3A8A),
            markerColor = rgb(0x1B1B22), handColor = rgb(0x1F3A8A),
            markerStyle = MarkerStyle.ROMAN,
            handStyle = HandStyle.LEAF,
        ),
        WatchFace(
            name = "Panda Chronograph",
            brand = "LICON",
            captionTop = "CHRONOGRAPH",
            metalLight = rgb(0xE4E6EA), metalDark = rgb(0x5E636B),
            dialCenter = rgb(0xFAFAF7), dialEdge = rgb(0xDADAD3),
            textColor = rgb(0x15151A), lumeColor = rgb(0xFFFFFF), secondHandColor = rgb(0xD62828),
            markerColor = rgb(0x15151A), handColor = rgb(0x15151A),
            hasSubDials = true,
        ),
        WatchFace(
            name = "Deep Diver",
            brand = "LICON",
            captionTop = "PROFESSIONAL",
            captionBottom = "300 M / 1000 FT",
            metalLight = rgb(0x8E949C), metalDark = rgb(0x15171A),
            dialCenter = rgb(0x16181C), dialEdge = rgb(0x050506),
            textColor = rgb(0xF2F2F2), lumeColor = rgb(0xBFF2D2), secondHandColor = rgb(0xFF7A1A),
            markerColor = rgb(0xBFF2D2), handColor = rgb(0xD5D9DE),
            markerStyle = MarkerStyle.DOT,
            hasDateWindow = true,
        ),
        WatchFace(
            name = "Pilot Aviator",
            brand = "LICON",
            captionBottom = "AUTOMATIC PILOT",
            metalLight = rgb(0xB8BDC4), metalDark = rgb(0x3A3E44),
            dialCenter = rgb(0x24262A), dialEdge = rgb(0x0E0F11),
            textColor = rgb(0xF4F4F0), lumeColor = rgb(0xF4F4F0), secondHandColor = rgb(0xFF8A00),
            markerColor = rgb(0xF4F4F0), handColor = rgb(0xF4F4F0),
            markerStyle = MarkerStyle.NUMBERS,
            hasDateWindow = true,
        ),
        WatchFace(
            name = "Calatrava Minimal",
            brand = "LICON",
            captionBottom = "GENÈVE",
            metalLight = rgb(0xF2C9B0), metalDark = rgb(0xA8683F),
            dialCenter = rgb(0xF8F1E4), dialEdge = rgb(0xE8DCC6),
            textColor = rgb(0x5B3A24), lumeColor = rgb(0xF8F1E4), secondHandColor = rgb(0xA8683F),
            handStyle = HandStyle.LEAF,
        ),
        WatchFace(
            name = "Rose Gold Dress",
            brand = "LICON",
            captionTop = "MANUFACTURE",
            captionBottom = "ROSE GOLD",
            metalLight = rgb(0xF7D3BC), metalDark = rgb(0xB0704A),
            dialCenter = rgb(0xEAD6BE), dialEdge = rgb(0xC9AE8E),
            textColor = rgb(0x4A2E1C), lumeColor = rgb(0xFFF6E8), secondHandColor = rgb(0x8C4A28),
            markerColor = rgb(0x6B4128), handColor = rgb(0x6B4128),
            bezelStyle = BezelStyle.FLUTED,
            markerStyle = MarkerStyle.ROMAN,
            handStyle = HandStyle.LEAF,
            hasDateWindow = true,
        ),
        WatchFace(
            name = "Midnight Moonlight",
            brand = "LICON",
            captionTop = "SUPERLATIVE",
            captionBottom = "CHRONOMETER",
            metalLight = rgb(0xF2F4F7), metalDark = rgb(0x7B828C),
            dialCenter = rgb(0x1A2250), dialEdge = rgb(0x070A1E),
            textColor = rgb(0xE8ECF2), lumeColor = rgb(0xEAF1FF), secondHandColor = rgb(0xC9D4EE),
            bezelStyle = BezelStyle.FLUTED,
            hasDateWindow = true,
        ),
        WatchFace(
            name = "Racing Red",
            brand = "LICON",
            captionTop = "TACHYMETER",
            metalLight = rgb(0xD9DCE0), metalDark = rgb(0x3C4046),
            dialCenter = rgb(0xB3141F), dialEdge = rgb(0x4A060B),
            textColor = rgb(0xFFFFFF), lumeColor = rgb(0xFFFFFF), secondHandColor = rgb(0xFFD400),
            markerColor = rgb(0xFFFFFF), handColor = rgb(0xFFFFFF),
            markerStyle = MarkerStyle.NUMBERS,
            hasSubDials = true,
        ),
    )

    /** Turns a 0xRRGGBB value into an opaque ARGB color. */
    @ColorInt
    private fun rgb(value: Int): Int = 0xFF000000.toInt() or value
}
