## fabric-mod-template

[![License](https://img.shields.io/github/license/Fallen-Breath/fabric-mod-template.svg)](http://www.gnu.org/licenses/lgpl-3.0.html)

This mod uses Fallen's fabric mod template.

Stuff to change when making a new mod:
- gradle.properties
- rename template_mod.mixins.json
- rename assets/template_mod
- add sources link to fabric.mod.json

Stuff for making it depend on carpet:
- uncomment lines 34 and 88 in common.gradle
- add dependency to the bottom of fabric.mod.json
- uncomment carpet in all gradle.properties
