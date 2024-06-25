lazy val scala_2_13 = "2.13.14"

lazy val creds = {
  sys.env.get("CI_JOB_TOKEN") match {
    case Some(token) =>
      Credentials("GitLab Packages Registry", "gitlab.com", "gitlab-ci-token", token)
    case _ =>
      Credentials(Path.userHome / ".sbt" / ".credentials")
  }
}

// Registry ID is the project ID of the project where the package is published, this should be set in the CI/CD environment
val registryId = sys.env.get("REGISTRY_HOST_PROJECT_ID").getOrElse("")

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
    resolvers ++= Seq(
      "gitlab" at s"https://gitlab.com/api/v4/projects/$registryId/packages/maven",
      "Maven Public" at "https://repo1.maven.org/maven2"),
    publishTo := {
      Some("gitlab" at s"https://gitlab.com/api/v4/projects/$registryId/packages/maven")
    },
    credentials += creds,
    libraryDependencies ++= {
      val kleinUtilVersion = "1.2.6"

      val apachePoiVersion = "5.2.5"
      val apachePoiXMLVersion = "4.1.2"
      val tikaVersion = "2.9.2"
      val scalacticVersion = "3.2.15"
      val scalaTestVersion = "3.2.15"
      val scalaMockVersion = "6.0.0"
      val commonsFileUpload = "1.5"
      val scalaLoggingVersion = "3.9.5"
      val jaiImageJPEG2000Version = "1.4.0"
      val jbig2ImageioVersion = "3.0.4"
      val log4jVersion = "2.23.1"

      Seq(
        "io.mdcatapult.klein" %% "util"                 % kleinUtilVersion % Test,
        "org.scalactic" %% "scalactic"                  % scalacticVersion % Test,
        "org.scalatest" %% "scalatest"                  % scalaTestVersion % Test,
        "org.scalamock" %% "scalamock"                  % scalaMockVersion % Test,
        "commons-fileupload" % "commons-fileupload"     % commonsFileUpload,
        "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
        "org.apache.logging.log4j" % "log4j-core"       % log4jVersion,
        "org.apache.tika" % "tika-core"                 % tikaVersion,
        "org.apache.tika" % "tika-parsers-standard-package" % tikaVersion,
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
