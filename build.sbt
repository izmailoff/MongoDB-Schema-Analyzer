name := "MongoDbSchema"

version := "0.0.3"

scalaVersion := "2.12.4"

scalacOptions ++= Seq("-deprecation")

com.github.retronym.SbtOneJar.oneJarSettings

resolvers ++= Seq(//"snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"      at "http://oss.sonatype.org/content/repositories/releases")

libraryDependencies ++= {
  val liftVersion = "3.2.0-M2"
  Seq(
    "net.liftweb" %% "lift-mongodb-record" % liftVersion,
    "net.liftweb" %% "lift-json" % liftVersion,
    "org.scalatest" %% "scalatest" % "3.0.4" % Test,
    "org.specs2" %% "specs2-core" % "4.0.1" % Test,
    "junit" % "junit" % "4.12" % "test",
//    "org.mongodb" %% "casbah" % "2.5.0",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
//    "org.mongeez" % "mongeez" % "0.9.2",
//    "org.springframework" % "spring-core" % "3.2.1.RELEASE",
//    "log4j" % "log4j" % "1.2.17",
//    "commons-lang" % "commons-lang" % "2.6",
//    "com.foursquare" %% "rogue-field"         % "2.0.0-beta22" intransitive(),
//    "com.foursquare" %% "rogue-core"          % "2.0.0-beta22" intransitive(),
//    "com.foursquare" %% "rogue-lift"          % "2.0.0-beta22" intransitive(),
  )
}

