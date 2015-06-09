name := "SIAMB"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaCore,
  javaWs,
  "org.webjars" % "bootstrap" % "3.3.4",
  "com.h2database" % "h2" % "1.4.181",
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "org.springframework" % "spring-context" % "4.1.1.RELEASE",
  "org.springframework" % "spring-orm" % "4.1.1.RELEASE",
  "org.springframework" % "spring-jdbc" % "4.1.1.RELEASE",
  "org.springframework" % "spring-tx" % "4.1.1.RELEASE",
  "org.springframework" % "spring-expression" % "4.1.1.RELEASE",
  "org.springframework" % "spring-aop" % "4.1.1.RELEASE",
  "org.springframework" % "spring-test" % "4.1.1.RELEASE" % "test",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.6.Final",
  "org.apache.commons" % "commons-io" % "1.3.2",
  "mysql" % "mysql-connector-java" % "5.1.35",
  "com.twilio.sdk" % "twilio-java-sdk" % "3.4.5"
)

lazy val root = (project in file(".")).enablePlugins(PlayJava)
