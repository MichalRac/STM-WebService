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

@WebService(serviceName = "MapProviderService", targetNamespace = "MapProvider")
public class MapProviderService {
    @WebMethod(operationName = "getMap")
    public String getMap(@WebParam(name = "latMin") double latMin, @WebParam(name = "lngMin") double lngMin, @WebParam(name = "latMax") double latMax, @WebParam(name = "lngMax") double lngMax) {
        Double ImageSize = 800D;
        latMin = GetLatitudeAnchor(latMin);
        latMax = GetLatitudeAnchor(latMax);
        lngMin = GetLongitudeAnchor(lngMin);
        lngMax = GetLongitudeAnchor(lngMax);
        
        int x = (int)(lngMin * ImageSize);
        int y = (int)(ImageSize - (latMax * ImageSize));
        int x2 = (int)((lngMax * ImageSize));
        int y2 = (int)(ImageSize - (latMin * ImageSize));
        int width = x2 - x;
        int height = y2 - y;
        
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
            System.out.println("Error handling the input/converting the image");
        }
        
        // fallback image
        return "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==";
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
