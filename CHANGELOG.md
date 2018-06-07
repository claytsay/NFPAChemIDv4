# Changelog

## Introduction
This document is designed to list out the past and present versions
of the program and to document the changes involved with each new version.
See [VERSION GUIDELINES.md](VERSION GUIDELINES.md) for version numbering guidelines.

## Log
### 00.01.02
- Fixed the `ChemDB` class:
  - It can now properly generate `chemList`s (i.e. the list of 
chemicals and their NFPA 704 ratings)
  - Changed to using `StringBuffer` in some cases to increase
efficiency
- Added `.md` files to the project: most text is copied from
[NFPA-Chem-ID-v3](https://github.com/claytsay/NFPA-ChemID-v3)
- Got the `Intent` going from `MainActivity` to `ResultsActivity` to work properly
  - Deprecated:
```
main
EnumMapInstanceCreator
DataConverter
```
as they are no longer used at all

### 00.01.01
- Changed methods to "be more general" as it is
supposedly good programming practice (e.g. `ArrayList` -> `List`)
- Improved Javadocs with `ul` and `li` tags
- Improved Chemical to generate with symbols defaulted as `false`
(unless specified otherwise)
  - Made querying with special symbols work
  
  
### 00.01.00
- "Initial release"
- Changed 'IDGet' to an abstract class to allow for different
ID-getting services to extend 'IDGetter' (the new class)
  - Edited 'Chemical' class
  - Wrote the 'IDGManager' class to manage multiple 'IDGetter's
- Added support for using the OPSIN service
- Organized version numbering and logging documents and guidelines
(see 'VERSION_GUIDELINES.md')
- Refined Javadocs
  - Added '{@link XXX}'s to the class descriptors (not methods)
  - Added '@since' tags to all public methods within classes
  - Added '@version' tags to all classes (presumably to denote
  when said classes were last updated)