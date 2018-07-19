package com.sparkFpgaApp.scala

import org.apache.spark.{SparkConf, SparkContext}
import org.json.JSONObject

object app {
  def main(args: Array[String]) {
    // create Spark context with Spark configuration
    val sc = new SparkContext(new SparkConf().setAppName("Spark Count"))
    // Read in text file, each line is a json string
    val messages = sc.textFile("/home/puxi/Desktop/spark_data/")

    //Parse the String as JSON
    val data = messages.map(parseJson(_))

    //Filter the records if event type is "view"
    val filteredOnView = data.filter(_(4).equals("view"))

    //project the event, basically filter the fields.
    val projected = filteredOnView.map(eventProjection(_))

    projected.saveAsTextFile("/home/puxi/Desktop/spark_result")

  }

  def parseJson(jsonString: String): Array[String] = {
    val parser = new JSONObject(jsonString)
    Array(
      parser.getString("user_id"),
      parser.getString("page_id"),
      parser.getString("ad_id"),
      parser.getString("ad_type"),
      parser.getString("event_type"),
      parser.getString("event_time"),
      parser.getString("ip_address"))
  }

  def eventProjection(event: Array[String]): String = {
    Array(
      event(2), //ad_id
      event(5)).mkString(" ") //event_time
  }

}