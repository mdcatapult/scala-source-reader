lazy val scala_2_13 = "2.13.3"

lazy val root = (project in file("."))
  .settings(
    name := "source",
    organization := "io.mdcatapult.klein",
    scalaVersion := scala_2_13,
    useCoursier := false,
    crossScalaVersions := scala_2_13 :: Nil,
    scalacOptions ++= Seq(
      "-encoding", "utf-8",
      "-unchecked",
      "-deprecation",
      "-explaintypes",
      "-feature",
      "-Xlint",
      "-Xfatal-warnings",
    ),
    resolvers         ++= Seq(
      "MDC Nexus Releases" at "https://nexus.wopr.inf.mdc/repository/maven-public/"),
    credentials       += {
      sys.env.get("NEXUS_PASSWORD") match {
        case Some(p) =>
          Credentials("Sonatype Nexus Repository Manager", "nexus.wopr.inf.mdc", "gitlab", p)
        case None =>
          Credentials(Path.userHome / ".sbt" / ".credentials")
      }
    },
    libraryDependencies ++= {
      val apachePoiVersion = "[4.1.2,5["
      val tikaVersion = "[1.24.1,2["

      Seq(
        "io.mdcatapult.klein" %% "util"                 % "1.2.2" % Test,
        "org.scalactic" %% "scalactic"                  % "[3.2.0,4[" % Test,
        "org.scalatest" %% "scalatest"                  % "[3.2.0,4[" % Test,
        "org.scalamock" %% "scalamock"                  % "[4.4.0,5[" % Test,
        "commons-fileupload" % "commons-fileupload"     % "[1.4,2[",
        "com.typesafe.scala-logging" %% "scala-logging" % "[3.9.2,4[",
        "org.apache.tika" % "tika-core"                 % tikaVersion,
        "org.apache.tika" % "tika-parsers"              % tikaVersion,
        "org.apache.tika" % "tika-langdetect"           % tikaVersion,
        "org.apache.poi" % "poi"                        % apachePoiVersion,
        "org.apache.poi" % "poi-ooxml"                  % apachePoiVersion,
        "org.apache.poi" % "poi-ooxml-schemas"          % apachePoiVersion,
        "org.apache.pdfbox" % "jbig2-imageio"           % "[3.0.3,4[",
        "com.github.jai-imageio" % "jai-imageio-jpeg2000" % "[1.3.0,2[",
        "org.xerial" % "sqlite-jdbc"                    % "[3.32.3.1,4[",
      )
    }
  )
  .settings(
    publishSettings: _*
  )

lazy val publishSettings = Seq(
  publishTo := {
    val version = if (isSnapshot.value) "snapshots" else "releases"
    Some("MDC Maven Repo" at s"https://nexus.wopr.inf.mdc/repository/maven-$version/")
  },
  credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
)
