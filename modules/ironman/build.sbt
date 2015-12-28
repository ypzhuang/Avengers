name := "ironman"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)


libraryDependencies += "org.igniterealtime" % "rest-api-client" % "1.1.3"

dependencyOverrides += "org.glassfish.hk2" % "hk2-api" % "2.4.0-b34"

dependencyOverrides += "org.glassfish.hk2.external" % "javax.inject" % "2.4.0-b34"

dependencyOverrides += "org.glassfish.hk2" % "hk2-locator" % "2.4.0-b34"

dependencyOverrides += "org.glassfish.hk2" % "hk2-utils" % "2.4.0-b34"



play.Project.playJavaSettings
