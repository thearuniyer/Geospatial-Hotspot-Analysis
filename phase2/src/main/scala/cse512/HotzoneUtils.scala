package cse512

object HotzoneUtils {

  def ST_Contains(queryRectangle: String, pointString: String ): Boolean = {
    // YOU NEED TO CHANGE THIS PART
    // gather point coordinates
    val p = pointString.split(",")
    val pX = p(0).trim().toDouble
    val pY = p(1).trim().toDouble

    // gather rectangle coordinates
    val r = queryRectangle.split(",")
    val rX1 = r(0).trim().toDouble
    val rY1 = r(1).trim().toDouble
    val rX2 = r(2).trim().toDouble
    val rY2 = r(3).trim().toDouble


    var minX : Double = 0
    var maxX : Double = 0

    if(rX1 < rX2)
    {
      minX = rX1
      maxX = rX2
    }
    else
    {
      minX = rX2
      maxX = rX1
    }

    var minY : Double = 0
    var maxY : Double = 0

    if(rY1 < rY2)
    {
      minY = rY1
      maxY = rY2
    }
    else
    {
      minY = rY2
      maxY = rY1
    }

    if(pX >= minX && pX <= maxX && pY >= minY && pY <= maxY)
    {
      return true
    }
    else
    {
      return false
    }
    // return true // YOU NEED TO CHANGE THIS PART
  }

  // YOU NEED TO CHANGE THIS PART

}
