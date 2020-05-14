package cse512

import org.apache.spark.sql.SparkSession

object SpatialQuery extends App{
  def runRangeQuery(spark: SparkSession, arg1: String, arg2: String): Long = {

    val pointDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg1);
    pointDf.createOrReplaceTempView("point")

    // YOU NEED TO FILL IN THIS USER DEFINED FUNCTION
    spark.udf.register("ST_Contains",(queryRectangle:String, pointString:String)=>(st_Contains(pointString, queryRectangle)))

    val resultDf = spark.sql("select * from point where ST_Contains('"+arg2+"',point._c0)")
    resultDf.show()

    return resultDf.count()
  }

  def runRangeJoinQuery(spark: SparkSession, arg1: String, arg2: String): Long = {

    val pointDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg1);
    pointDf.createOrReplaceTempView("point")

    val rectangleDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg2);
    rectangleDf.createOrReplaceTempView("rectangle")

    // YOU NEED TO FILL IN THIS USER DEFINED FUNCTION
    spark.udf.register("ST_Contains",(queryRectangle:String, pointString:String)=>(st_Contains(pointString, queryRectangle)))

    val resultDf = spark.sql("select * from rectangle,point where ST_Contains(rectangle._c0,point._c0)")
    resultDf.show()

    return resultDf.count()
  }

  def runDistanceQuery(spark: SparkSession, arg1: String, arg2: String, arg3: String): Long = {

    val pointDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg1);
    pointDf.createOrReplaceTempView("point")

    // YOU NEED TO FILL IN THIS USER DEFINED FUNCTION
    spark.udf.register("ST_Within",(pointString1:String, pointString2:String, distance:Double)=>(st_Within(pointString1, pointString2, distance)))

    val resultDf = spark.sql("select * from point where ST_Within(point._c0,'"+arg2+"',"+arg3+")")
    resultDf.show()

    return resultDf.count()
  }

  def runDistanceJoinQuery(spark: SparkSession, arg1: String, arg2: String, arg3: String): Long = {

    val pointDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg1);
    pointDf.createOrReplaceTempView("point1")

    val pointDf2 = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg2);
    pointDf2.createOrReplaceTempView("point2")

    // YOU NEED TO FILL IN THIS USER DEFINED FUNCTION
    spark.udf.register("ST_Within",(pointString1:String, pointString2:String, distance:Double)=>(st_Within(pointString1, pointString2, distance)))
    val resultDf = spark.sql("select * from point1 p1, point2 p2 where ST_Within(p1._c0, p2._c0, "+arg3+")")
    resultDf.show()

    return resultDf.count()
  }
  
  def st_Contains(pointString:String, queryRectangle:String) : Boolean = {
    val point = pointString.split(",")
    val pX = point(0).trim().toDouble
    val pY = point(1).trim().toDouble
    
    val rectangle = queryRectangle.split(",")
    val r_x1 = rectangle(0).trim().toDouble
    val r_y1 = rectangle(1).trim().toDouble
    val r_x2 = rectangle(2).trim().toDouble
    val r_y2 = rectangle(3).trim().toDouble
    
    var minX: Double = 0
    var maxX: Double = 0
    if(r_x1 < r_x2) {
      minX = r_x1
      maxX = r_x2
    } else {
      minX = r_x2
      maxX = r_x1
    }
    
    var minY: Double = 0
    var maxY: Double = 0
    if(r_y1 < r_y2) {
      minY = r_y1
      maxY = r_y2
    } else {
      minY = r_y2
      maxY = r_y1
    }
    
    if(pX >= minX && pX <= maxX && pY >= minY && pY <= maxY) {
      return true
    } else {
      return false
    }
  }

  def st_Within(pointString1:String, pointString2:String, distance:Double) : Boolean = {
    val point1 = pointString1.split(",")
    val pX_1 = point1(0).trim().toDouble
    val pY_1 = point1(1).trim().toDouble
    
    val point2 = pointString2.split(",")
    val pX_2 = point2(0).trim().toDouble
    val pY_2 = point2(1).trim().toDouble
    
    val euclidean_distance = scala.math.pow(scala.math.pow((pX_1 - pX_2), 2) + scala.math.pow((pY_1 - pY_2), 2), 0.5)
    
    return euclidean_distance <= distance
  }


}
