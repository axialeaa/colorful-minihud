## fabric-mod-template

[![License](https://img.shields.io/github/license/Fallen-Breath/fabric-mod-template.svg)](http://www.gnu.org/licenses/lgpl-3.0.html)

This mod uses Fallen's fabric mod template.

# Colorful MiniHUD

A MiniHUD extension that allows custom colors and extensive formatting for each info line.

### Formats Syntax

The format strings are written using vanilla minecraft's [Raw JSON text format](https://minecraft.wiki/w/Raw_JSON_text_format). Some valid examples are:

- `"abc"`
- `{"color":"red", "text":"abc"}`
- `{"color":"#FF0000", "text":"abc"}`
- `[{"color":"#FF0000", "text":"abc"}, {"color":"green", "text":"def"}]`

![minihud1](https://github.com/axialeaa/colorful-minihud/assets/29168747/e952d8b1-2899-4571-9a30-1cbfca7c3074)

We have added some convenience syntax to make these strings more compact to write:

- `#0`, `#1`, ..., `#15` are reserved color variables. You can change their values in the settings, and then use them in any info line to insert that color. This allows for consistent global color themes. For example: `{"color":"#FF0000","text":"abc"}` can become `{"color":"#1","text":"abc"}`.
- `#a"b"` is included as a short version of `{"color":"#a","text":"b"}`. For example: `#red"abc"`, `#1"abc"`, `#FF0000"abc"`.
- The entire text box is automatically surrounded by `[]`. This means that instead of writing `[#red"abc", #green"def"]` you can write `#red"abc", #green"def"`.
- To change the default format of the whole string, encase it in square brackets and add the desired default syntax to an empty string at the start. For example, `#red"x", ", ", #green"y", ", ", #aqua"z"` has white commas, and `[gold"", #red"x", ", ", #green"y", ", ", #aqua"z"]` has gold commas.

For each miniHUD info line, there are values that can be displayed. These values are denoted using placeholder strings, which start with `%`. For example, for the `infoCoordinates` line, the 3 available placeholders are `%x`, `%y`, and `%z`. Some valid `infoCoordinates` formats are:

- `"XYZ: %x, %y, %z"`
- `"XYZ: ", {"color":"red", "text":"%x, %y, %z"}`
- `"XYZ: ", #red"%x", ", ", #green"%y", ", ", #blue"%z"`
- `"%x%y%z"`
- `"%xhello"` (This will insert the variable `x` followed by the string `"hello"`. There is no need for a space in between if you don't want it.)

![minihud2](https://github.com/axialeaa/colorful-minihud/assets/29168747/26069a2b-7c33-40f1-8b90-eb8854baa9d4)

For more advanced customization, custom Java format strings are usable for all variables, by placing a `$` after the variable. For example, `%x$.2f` translates to `String.format(%1$.2f, x)`. For more information on how to use Java's Formatter, see the [javadoc](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Formatter.html#syntax).

- `"XYZ: %x$.4f, %y$.1f, %z$.4f"`

![minihud4](https://github.com/axialeaa/colorful-minihud/assets/29168747/ed64489f-c875-4d4f-be6a-283372443145)

`infoTimeIRL` is the only info line that requires this type of formatting to be used. See the [javadoc](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Formatter.html#dt) for how to customize the time format.
