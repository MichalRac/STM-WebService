/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/WebService.java to edit this template
 */
package MapProvider;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import jdk.jpackage.internal.Log;

/**
 *
 * @author racmi
 */
@WebService(serviceName = "MapProviderService", targetNamespace = "MapProvider")
public class MapProviderService {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getEncodedMap")
    public String getEncodedMap(@WebParam(name = "lat0") double lat0, @WebParam(name = "long0") double long0, @WebParam(name = "lat1") double lat1, @WebParam(name = "long1") double long1) {
        
        System.out.println("lat0 = " + lat0);
        System.out.println("lat1 = " + lat1);
        System.out.println("long0 = " + long0);
        System.out.println("long1 = " + long1);

        Double ImageSize = 800D;
        
        lat0 = GetLatitudeAnchor(lat0);
        lat1 = GetLatitudeAnchor(lat1);
        long0 = GetLongitudeAnchor(long0);
        long1 = GetLongitudeAnchor(long1);

        System.out.println("lat0 = " + lat0);
        System.out.println("lat1 = " + lat1);
        System.out.println("long0 = " + long0);
        System.out.println("long1 = " + long1);
        
        int x = (int)(long0 * ImageSize);
        int y = (int)(ImageSize - (lat1 * ImageSize));

        int x2 = (int)((long1 * ImageSize));
        int y2 = (int)(ImageSize - (lat0 * ImageSize));
        
        int width = x2 - x;
        int height = y2 - y;
        
        System.out.println("x = " + x);
        System.out.println("y = " + y);
        System.out.println("x2 = " + x2);
        System.out.println("y2 = " + y2);
        System.out.println("width = " + width);
        System.out.println("height = " + height);

        try{
            InputStream fullMapImageBase64 = getClass().getClassLoader().getResourceAsStream("map.png");
            BufferedImage img = ImageIO.read(fullMapImageBase64);
            BufferedImage croppedImage = cropImage(img, new Rectangle(x, y, width, height));

            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            ImageIO.write(croppedImage, "png", baos);
            String cropped = Base64.getEncoder().encodeToString(baos.toByteArray());
            return cropped;

        }
        catch(IOException e)
        {
            
        }


        //TODO write your implementation code here:
        String testString = "hello world";
        String encodedString = Base64.getEncoder().encodeToString(testString.getBytes());
        return encodedString;
    }
    
    private Double GetLatitudeAnchor(Double lat)
    {
        Double minLat = 54.333251000649454D;
        Double maxLat = 54.413205389562194D;
        
        lat = Math.min(lat, maxLat);
        lat = Math.max(lat, minLat);
        
        Double divisor = maxLat - minLat;
        Double result = (lat - minLat) / divisor;
        
        return result;
    }
    
    private Double GetLongitudeAnchor(Double lon)
    {
        Double minLon = 18.572490218700082;
        Double maxLon = 18.709215200253727;
        
        lon = Math.min(lon, maxLon);
        lon = Math.max(lon, minLon);
        
        Double divisor = maxLon - minLon;
        Double result = (lon - minLon) / divisor;
        
        return result;
    }
    
    private BufferedImage cropImage(BufferedImage src, Rectangle rect) {
      BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
      return dest; 
   }
}
