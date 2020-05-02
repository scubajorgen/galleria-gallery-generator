/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.gallerygenerator;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 *
 * @author jorgen
 */
public class AlbumTest
{
   
    public AlbumTest()
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
     * Test of getName method, of class Album.
     */
    @Test
    public void testGetName()
    {
        System.out.println("getName");
        String expResult = "Test Name";
        Album instance = new Album(expResult, "", "");
        instance.setName(expResult);
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setName method, of class Album.
     */
    @Test
    public void testSetName()
    {
        System.out.println("setName");
        String name = "";
        Album instance = new Album("", "", "");
        String expResult = "Test Name";
        instance.setName(expResult);
        assertEquals(expResult, instance.getName());
    }

    /**
     * Test of getDescription method, of class Album.
     */
    @Test
    public void testGetDescription()
    {
        System.out.println("getDescription");
        String expResult = "Test description";
        Album instance = new Album("", expResult, "");
        String result = instance.getDescription();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDescription method, of class Album.
     */
    @Test
    public void testSetDescription()
    {
        System.out.println("setDescription");
        String expResult = "";
        Album instance = new Album("", "", "");
        instance.setDescription(expResult);
        assertEquals(expResult, instance.getDescription());
    }

    /**
     * Test of getDirectory method, of class Album.
     */
    @Test
    public void testGetDirectory()
    {
        System.out.println("getDirectory");
        String expResult = "SomeDir";
        Album instance = new Album("", "", expResult);
        String result = instance.getDirectory();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDirectory method, of class Album.
     */
    @Test
    public void testSetDirectory()
    {
        System.out.println("setDirectory");
        String directory = "SomeDirThing";
        Album instance = new Album();
        instance.setDirectory(directory);
        assertEquals(directory, instance.getDirectory());
    }

    /**
     * Test of getThumbnail method, of class Album.
     */
    @Test
    public void testGetThumbnail()
    {
        System.out.println("getThumbnail");
        Album instance = new Album();
        String expResult = null;
        String result = instance.getThumbnail();
        assertEquals(expResult, result);
        
        instance = new Album("", "", "");
        expResult = "thumbnail.jpg";
        result = instance.getThumbnail();
        assertEquals(expResult, result);
    }

    /**
     * Test of setThumbnail method, of class Album.
     */
    @Test
    public void testSetThumbnail()
    {
        System.out.println("setThumbnail");
        String thumbnail = "someOtherThumbnail.jpg";
        Album instance = new Album();
        instance.setThumbnail(thumbnail);
        assertEquals(thumbnail, instance.getThumbnail());
    }

    /**
     * Test of setImages method, of class Album.
     */
    @Test
    public void testSetGetImages()
    {
        System.out.println("setImages");
        List<Image> images = new ArrayList<Image>();
        Album instance = new Album();
        instance.setImages(images);
        assertEquals(images, instance.getImages());
    }

    /**
     * Test of addImage method, of class Album.
     */
    @Test
    public void testAddImage()
    {
        System.out.println("addImage");
        Image image = new Image("title", "caption", "date", "image", null);
        Album instance = new Album();
        instance.setImages(new ArrayList<Image>());
        instance.addImage(image);
        assertEquals("title", instance.getImages().get(0).getTitle());
    }

    /**
     * Test of isChanged method, of class Album.
     */
    @Test
    public void testIsChanged()
    {
        System.out.println("isChanged");
        Album instance = new Album();
        boolean expResult = false;
        instance.setImages(new ArrayList<Image>());
        boolean result = instance.isChanged();
        assertEquals(expResult, result);
        
        expResult = true;
        instance.addImage(new Image());
        result = instance.isChanged();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Album.
     */
    @Test
    public void testToString()
    {
        System.out.println("toString");
        Album instance = new Album("name", "description", "directory");
        instance.setImages(new ArrayList<Image>());
        instance.addImage(new Image("title", "caption", "date", "image", null));
        String expResult = "  Album    name\n           description\n    Image    title\n             caption\n             image\n";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
}
