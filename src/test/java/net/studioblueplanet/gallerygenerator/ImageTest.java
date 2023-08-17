/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.gallerygenerator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jorgen
 */
public class ImageTest
{
    
    public ImageTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getCaptureDateTime method, of class Image.
     */
    @Test
    public void testGetCaptureDateTime()
    {
        System.out.println("getCaptureDateTime");
        String expResult = "2020-05-01 18:31:00";
        Image instance = new Image("title", "caption", expResult, "image.jpg", null);
        String result = instance.getCaptureDateTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCaptureDateTime method, of class Image.
     */
    @Test
    public void testSetGetCaptureDateTime()
    {
        System.out.println("setCaptureDateTime");
        String captureDateTime = "2020-05-01 18:32:00";
        Image instance = new Image("title", "caption", "", "image.jpg", null);
        instance.setCaptureDateTime(captureDateTime);
        assertEquals(captureDateTime, instance.getCaptureDateTime());
    }

    /**
     * Test of getTitle method, of class Image.
     */
    @Test
    public void testGetTitle()
    {
        System.out.println("getTitle");
        String expResult = "some title";
        Image instance = new Image(expResult, "caption", "", "image.jpg", null);
        String result = instance.getTitle();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTitle method, of class Image.
     */
    @Test
    public void testSetGetTitle()
    {
        System.out.println("setTitle");
        String title = "Some Other title";
        Image instance = new Image("title", "caption", "", "image.jpg", null);
        instance.setTitle(title);
        assertEquals(title, instance.getTitle());
    }

    /**
     * Test of getCaption method, of class Image.
     */
    @Test
    public void testGetCaption()
    {
        System.out.println("getCaption");
        String expResult = "Some Caption";
        Image instance = new Image("title", expResult, "", "image.jpg", null);
        String result = instance.getCaption();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCaption method, of class Image.
     */
    @Test
    public void testSetGetCaption()
    {
        System.out.println("setCaption");
        String caption = "Blah";
        Image instance = new Image();
        instance.setCaption(caption);
        // TODO review the generated test code and remove the default call to fail.
        assertEquals(caption, instance.getCaption());
    }

    /**
     * Test of getImage method, of class Image.
     */
    @Test
    public void testGetImage()
    {
        System.out.println("getImage");
        String expResult = "image.jpg";
        Image instance = new Image("title", "caption", expResult, "image.jpg", null);
        String result = instance.getImage();
        assertEquals(expResult, result);
    }

    /**
     * Test of setImage method, of class Image.
     */
    @Test
    public void testSetGetImage()
    {
        System.out.println("setImage");
        String image = "anotherImage.jpg";
        Image instance = new Image();
        instance.setImage(image);
        assertEquals(image, instance.getImage());
    }

    /**
     * Test of getVideo method, of class Image.
     */
    @Test
    public void testGetVideo()
    {
        System.out.println("getVideo");
        String expResult = "video.mp4";
        Image instance = new Image("title", "caption", "", null, expResult);
        String result = instance.getVideo();
        assertEquals(expResult, result);
    }

    /**
     * Test of setVideo method, of class Image.
     */
    @Test
    public void testSetGetVideo()
    {
        System.out.println("setVideo");
        String video = "blah.mp4";
        Image instance = new Image();
        instance.setVideo(video);
        assertEquals(video, instance.getVideo());
    }

    /**
     * Test of toString method, of class Image.
     */
    @Test
    public void testToString()
    {
        System.out.println("toString");
        String expResult = "    Image    title\n             caption\n             image.jpg\n";
        Image instance = new Image("title", "caption", "date", "image.jpg", null);
        String result = instance.toString();
        assertEquals(expResult, result);

        expResult = "    Image    title\n             caption\n             video.mp4\n";
        instance = new Image("title", "caption", "date", null, "video.mp4");
        result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class Image.
     */
    @Test
    public void testCompareToDateTime()
    {
        int expResult;
        int result;
        System.out.println("compareTo");
        Image instance  = new Image("", "", "2020-05-01 18:42:10", "", null);
        Image image1    = new Image("", "", "2020-05-01 18:42:09", "", null);
        Image image2    = new Image("", "", "2020-05-01 18:42:11", "", null);
        Image image3    = new Image("", "", "2020-05-01 18:42:10", "", null);
        expResult= +1;
        result = instance.compareTo(image1);
        assertEquals(expResult, result);
        expResult= -1;
        result = instance.compareTo(image2);
        assertEquals(expResult, result);
        expResult= 0;
        result = instance.compareTo(image3);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class Image.
     */
    @Test
    public void testCompareToFileName()
    {
        int expResult;
        int result;
        System.out.println("compareTo - Filename");
        Image.setSortingMethod(Image.Sorting.SORTING_FILENAME);
        Image instance  = new Image("", "", "2020-05-01 18:42:10", "5hoi", null);
        Image instance2  = new Image("", "", "2020-05-01 18:42:10", "5hoi", null);
        Image image1    = new Image("", "", "2020-05-01 18:42:10", "4greater", null);
        Image image2    = new Image("", "", "2020-05-01 18:42:10", "6smaller", null);
        Image image3    = new Image("", "", "2020-05-01 18:42:11", "5hoi", null);
        Image image4    = new Image("", "", "2020-05-01 18:42:10", null, "4greater");
        Image image5    = new Image("", "", "2020-05-01 18:42:10", null, "6smaller");
        Image image6    = new Image("", "", "2020-05-01 18:42:11", null, "5hoi");
        expResult= +1;
        result = instance.compareTo(image1);
        assertEquals(expResult, result);
        expResult= -1;
        result = instance.compareTo(image2);
        assertEquals(expResult, result);
        expResult= 0;
        result = instance.compareTo(image3);
        assertEquals(expResult, result);

        expResult= +1;
        result = instance.compareTo(image4);
        assertEquals(expResult, result);
        expResult= -1;
        result = instance.compareTo(image5);
        assertEquals(expResult, result);
        expResult= 0;
        result = instance.compareTo(image6);
        assertEquals(expResult, result);    

        expResult= +1;
        result = instance2.compareTo(image4);
        assertEquals(expResult, result);
        expResult= -1;
        result = instance2.compareTo(image5);
        assertEquals(expResult, result);
        expResult= 0;
        result = instance2.compareTo(image6);
        assertEquals(expResult, result);   
    }

    
}
