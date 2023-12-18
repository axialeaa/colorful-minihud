# Hi there! Thanks for checking out MiniDeco!
<img align="right" width="140" src="src/main/resources/assets/minideco/icon.png">

[![GitHub downloads](https://img.shields.io/github/downloads/axialeaa/MiniDeco/total?label=Github%20downloads&logo=github&style=for-the-badge)](https://github.com/axialeaa/MiniDeco/releases)

<strong>Colorful MiniHUD</strong> is an extension for maruohon's [MiniHUD] which adds a Formats tab to the config screen, allowing for near-limitless customisation of its info line feature! This ability was initially implemented into a [fork of MiniHUD itself][minihud-intricate] developed by intricate, however it was incompatible with features from mods like [Tweakermore] and [MasaAdditions] which rely on MiniHUD being present in your mods folder. This new implementation resolves this issue building on top of MiniHUD's code at runtime!

![](https://github.com/axialeaa/colorful-minihud/assets/116074698/7b85949e-a310-4199-ab08-5e908ac7a84e)
> The Formats config screen

Please go and check out [intricate]'s profile. She did most of the work for this mod and created the concept in the first place!
***

### Getting Started
<strong>MiniDeco</strong> relies on [MiniHUD], and by extension [Malilib], being installed. Once you've downloaded both of these, simply drag them and <strong>MiniDeco</strong> into your Fabric mods folder.
***

### Formatting Syntax
<strong>MiniDeco</strong>'s text is written using vanilla Minecraft's [Raw JSON text format], including its 16 in-built colors. Some valid JSON examples are:

- `"abc"`
- `{"color":"red", "text":"abc"}`
- `{"color":"#FF0000", "text":"abc"}`
- `[{"color":"#FF0000", "text":"abc"}, {"color":"green", "text":"def"}]`

![](https://github.com/axialeaa/colorful-minihud/assets/116074698/d5ce24a1-33ff-4eb7-91f1-2653fc568cf6)
> Basic text formatting using JSON

Unfortunately, JSON tends to be quite bulky. In the fourth example shown above, nearly 70 characters are used to produce a string just 6 characters long, with 2 colors. To make this a bit more compact, we've added some shorthand methods, described as followed.

#### Color Variables
`#0` through `#15` are reserved color variables, modifiable in [MiniHUD]'s Colors tab via the `color0` through `color15` settings. These can be used in any info line format string to insert the assigned color. For example: `{"color":"#FF0000","text":"abc"}` can become `{"color":"#1","text":"abc"}`, if `#1` is set to `#FF0000`.

We've already filled these in with colors we think are versatile enough by default, but you should feel free to change them as you wish, and it should be noted that standard hex values are substitutable in any circumstance where vanilla color codes or custom color variables are acceptable.

#### Color and Text Pairing
In order to condense instances of `{"color":"#<color>","text":"abc"}`, the color can directly prepend the string itself, like this: `#<color>"abc"`. This works with color variables, hex numbers and vanilla Minecraft's color codes.
- The entire text box is automatically surrounded by `[]`. This means that instead of writing `[#red"abc", #green"def"]` you can write `#red"abc", #green"def"`.
- To change the default format of the whole string, encase it in square brackets and add the desired default syntax to an empty string at the start. For example, `#red"x", ", ", #green"y", ", ", #aqua"z"` has white commas, and `[gold"", #red"x", ", ", #green"y", ", ", #aqua"z"]` has gold commas.

#### String Variables

For each miniHUD info line, there are values that can be displayed. These values are denoted using placeholder strings, which start with `%`. For example, for the `infoCoordinates` line, the 3 available placeholders are `%x`, `%y`, and `%z`. Some valid `infoCoordinates` formats are:

- `"XYZ: %x, %y, %z"`
- `"XYZ: ", {"color":"red", "text":"%x, %y, %z"}`
- `"XYZ: ", #red"%x", ", ", #green"%y", ", ", #blue"%z"`
- `"%x%y%z"`
- `"%xhello"` (This will insert the variable `x` followed by the string `"hello"`. There is no need for a space in between if you don't want it.)

![](https://github.com/axialeaa/colorful-minihud/assets/29168747/26069a2b-7c33-40f1-8b90-eb8854baa9d4)

For more advanced customization, custom Java format strings are usable for all variables, by placing a `$` after the variable. For example, `%x$.2f` translates to `String.format(%1$.2f, x)`. For more information on how to use Java's Formatter, see the [javadoc](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Formatter.html#syntax).

- `"XYZ: %x$.4f, %y$.1f, %z$.4f"`

![](https://github.com/axialeaa/colorful-minihud/assets/29168747/ed64489f-c875-4d4f-be6a-283372443145)

`infoTimeIRL` is the only info line that requires this type of formatting to be used. See the [javadoc](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Formatter.html#dt) for how to customize the time format.

[MiniHUD]: https://github.com/maruohon/minihud
[Malilib]: https://github.com/maruohon/malilib
[Tweakermore]: https://github.com/Fallen-Breath/tweakermore
[MasaAdditions]: https://github.com/hp3721/masaadditions
[minihud-intricate]: https://github.com/lntricate1/minihud-intricate
[intricate]: https://github.com/lntricate1

[Raw JSON text format]: https://minecraft.wiki/w/Raw_JSON_text_format
