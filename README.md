## fabric-mod-template

[![License](https://img.shields.io/github/license/Fallen-Breath/fabric-mod-template.svg)](http://www.gnu.org/licenses/lgpl-3.0.html)

This mod uses Fallen's fabric mod template.

# Colorful MiniHUD

A MiniHUD extension that makes all info lines have customizable formatting and colors.

### Formats Syntax

The format strings are written in JSON, which uses the same syntax as vanilla's `tellraw` command. Some valid string are:

- `"abc"`
- `"{"color":"red", "text":"abc"}"`
- `"{"color":"#FF0000", "text":"abc"}"`
- `"[{"color":"#FF0000", "text":"abc"}, {"color":"green", "text":"def"}]"`

The info is inserted using variables, starting with a `%`. For example:

- `"XYZ: %x, %y, %z"`
- `"{"color":"red", "text":"XYZ: %x, %y, %z"}"`
- `"{"color":"#FF0000", "text":"XYZ: %x, %y, %z"}"`
- `"["XYZ: ", {"color":"red", "text":"%x"}, ", ", {"color":"green", "text":"%y"}, ", ", {"color":"blue", "text":"%z"}]"`
- `"%x%y%z"`
- `"%xhello"` (This will insert the variable `x` followed by the string `"hello"`. There is no need for a space in between if you don't want it.)

18 color variables are available for making a global color scheme: `colorBG`, `colorFG`, `color0`, `color1`, ..., `color15`:

- `"["XYZ: ", {"color":"%color1", "text":"%x"}, ", ", {"color":"%color2", "text":"%y"}, ", ", {"color":"%color3", "text":"%z"}]"`

For more advanced customization, custom Java format strings are usable for all variables, by placing a `$` after the variable. For example, `%x$.2f` translates to `String.format(%1$.2f, x)`. For more information on how to use Java's Formatter, see the [javadoc](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Formatter.html#syntax).

- `"XYZ: %x$.2f, %y$.5f, %z$.2f"`

`infoTimeIRL` is the only info line that requires this type of formatting to be used. See the [javadoc](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Formatter.html#dt) for how to customize the time format.
