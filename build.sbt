name := "Avengers"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)     

play.Project.playJavaSettings

lazy val common =  project.in(file("modules/common")).settings(playJavaSettings: _*)

lazy val hulk = project.in(file("modules/hulk")).settings(playJavaSettings: _*).dependsOn(common)

lazy val ironman = project.in(file("modules/ironman")).settings(playJavaSettings: _*).dependsOn(common)

lazy val root = project.in(file("."))
  .settings(playJavaSettings: _*)
  .dependsOn(common)
  .dependsOn(hulk)
  .dependsOn(ironman)
  .aggregate(common,hulk,ironman)

