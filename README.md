## fabric-mod-template

[![License](https://img.shields.io/github/license/Fallen-Breath/fabric-mod-template.svg)](http://www.gnu.org/licenses/lgpl-3.0.html)

This mod uses Fallen's fabric mod template.

# Colorful MiniHUD

A MiniHUD extension that allows custom colors and extensive formatting for each info line.

### Formats Syntax

The format strings are written in JSON, which uses the same syntax as vanilla's `tellraw` command. Some valid format strings are:

- `"abc"`
- `{"color":"red", "text":"abc"}`
- `{"color":"#FF0000", "text":"abc"}`
- `[{"color":"#FF0000", "text":"abc"}, {"color":"green", "text":"def"}]`

![minihud1](https://github.com/axialeaa/colorful-minihud/assets/29168747/e952d8b1-2899-4571-9a30-1cbfca7c3074)

For each miniHUD info line, there are values that can be displayed. These values are denoted using placeholder strings, which start with `%`. For example, for the `infoCoordinates` line, the 3 available placeholders are `%x`, `%y`, and `%z`. Some valid `infoCoordinates` formats are:

- `"XYZ: %x, %y, %z"`
- `"XYZ: ", {"color":"red", "text":"%x, %y, %z"}`
- `["XYZ: ", {"color":"red", "text":"%x"}, ", ", {"color":"green", "text":"%y"}, ", ", {"color":"blue", "text":"%z"}]`
- `"%x%y%z"`
- `"%xhello"` (This will insert the variable `x` followed by the string `"hello"`. There is no need for a space in between if you don't want it.)

![minihud2](https://github.com/axialeaa/colorful-minihud/assets/29168747/26069a2b-7c33-40f1-8b90-eb8854baa9d4)

18 color variables are available for making a global color scheme: `colorBG`, `colorFG`, `color0`, `color1`, ..., `color15`:

- `["XYZ: ", {"color":"%color1", "text":"%x"}, ", ", {"color":"%color2", "text":"%y"}, ", ", {"color":"%color3", "text":"%z"}]`

![minihud3](https://github.com/axialeaa/colorful-minihud/assets/29168747/cd33db45-c8ce-4942-9828-045095a27ade)

For more advanced customization, custom Java format strings are usable for all variables, by placing a `$` after the variable. For example, `%x$.2f` translates to `String.format(%1$.2f, x)`. For more information on how to use Java's Formatter, see the [javadoc](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Formatter.html#syntax).

- `"XYZ: %x$.4f, %y$.1f, %z$.4f"`

![minihud4](https://github.com/axialeaa/colorful-minihud/assets/29168747/ed64489f-c875-4d4f-be6a-283372443145)

`infoTimeIRL` is the only info line that requires this type of formatting to be used. See the [javadoc](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Formatter.html#dt) for how to customize the time format.
