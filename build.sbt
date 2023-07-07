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
      val kleinUtilVersion = "1.2.4"

      val apachePoiVersion = "5.2.2"
      val apachePoiXMLVersion = "4.1.2"
      val tikaVersion = "1.28.5"
      val scalacticVersion = "3.2.15"
      val scalaTestVersion = "3.2.15"
      val scalaMockVersion = "5.2.0"
      val commonsFileUpload = "1.5"
      val scalaLoggingVersion = "3.9.5"
      val jaiImageJPEG2000Version = "1.4.0"
      val jbig2ImageioVersion = "3.0.4"
      val log4jVersion = "2.20.0"

      Seq(
        "io.mdcatapult.klein" %% "util"                 % kleinUtilVersion % Test,
        "org.scalactic" %% "scalactic"                  % scalacticVersion % Test,
        "org.scalatest" %% "scalatest"                  % scalaTestVersion % Test,
        "org.scalamock" %% "scalamock"                  % scalaMockVersion % Test,
        "commons-fileupload" % "commons-fileupload"     % commonsFileUpload,
        "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
        "org.apache.logging.log4j" % "log4j-core"       % log4jVersion,
        "org.apache.tika" % "tika-core"                 % tikaVersion,
        "org.apache.tika" % "tika-parsers"              % tikaVersion,
        "org.apache.tika" % "tika-langdetect"           % tikaVersion,
        "org.apache.poi" % "poi"                        % apachePoiVersion,
        "org.apache.poi" % "poi-ooxml"                  % apachePoiVersion,
        "org.apache.pdfbox" % "jbig2-imageio"           % jbig2ImageioVersion,
        "com.github.jai-imageio" % "jai-imageio-jpeg2000" % jaiImageJPEG2000Version
//      "org.xerial" % "sqlite-jdbc"                    % "3.32.3.1"
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
