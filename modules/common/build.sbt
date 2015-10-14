name := "common"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "com.typesafe" %% "play-plugins-mailer" % "2.2.0",
  "mysql" % "mysql-connector-java" % "5.1.18",
  "com.notnoop.apns" % "apns" % "0.2.3",
  "org.json" % "json" % "20090211",
  "org.apache.httpcomponents" % "httpclient" % "4.3.5"
)     

play.Project.playJavaSettings
