# Source

SourceReader can extract text from a Source using a StreamReader.
The main StreamReader is TikaTextExtractingStreamReader that will use Tika.

Tika isn't completely configurable in how it handles separated value files
which leads to an interesting bug concerning a UTF_8 character that represents
no character (BOM).  We want to ignore this character but Tika throws an exception, therefore the custom class SeparatedValueStreamReader is used instead.

# Testing
`sbt +clean coverage +test coverageReport`

## Notes
The `org.xenial.sqlite-jdbc` driver isn't actually used but was previously included to suppress a warning that Apache Tika needs it to process sqlite files.
We are not processing sqlite so is now removed. It has been left in the `build.sbt` but commented out.
