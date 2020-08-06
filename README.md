# Source

SourceReader can extract text from a Source using a StreamReader.
The main StreamReader is TikaTextExtractingStreamReader that will use Tika.

Tika isn't completely configurable in how it handles separated value files
which lead to an interesting bug concerning a UTF_8 character that represents
no character (BOM).  We desire to ignore this character, but Tika throws.
Therefore SeparatedValueStreamReader is used instead.

# Testing
`sbt +clean coverage +test coverageReport`
