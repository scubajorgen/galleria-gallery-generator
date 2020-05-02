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
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
/**
 *
 * @author jorgen
 */
@RunWith(SpringRunner.class)
@Configuration
@EnableAutoConfiguration
@Import({ BeanConfiguration.class })

public class GeneratorTest
{
    @Autowired
    ObjectMapper mapper;
    
    @Mock
    ObjectWriter writer;
    
    @Mock
    FileBackupper backupper;
    
    public GeneratorTest()
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
     * Test of generate method, of class Generator.
     */
    @Test
    public void testGenerateNonSplit() throws IOException
    {
        ArgumentCaptor<Object>  galleryCaptor;
        ArgumentCaptor<File>    fileCaptor;
        Album                   album;
        Gallery                 gallery;
        Image                   image;
        
        galleryCaptor=ArgumentCaptor.forClass(Object.class);
        fileCaptor=ArgumentCaptor.forClass(File.class);
        
        System.out.println("generate MERGE, new album, no split");
        Options options         = new Options();
        options.galleryDirectory="src/test/resources/gallery";
        options.albumDirectory  =null;
        options.mode            =Generator.ModeType.MODE_MERGEEXISTING;
        options.addExifDateTime =true;
        options.split           =false;
        Generator instance      = new Generator(mapper, writer, backupper);
        instance.generate(options);
        verify(writer, times(1)).writeValue(fileCaptor.capture(), galleryCaptor.capture());

        assertEquals("src/test/resources/gallery/gallery.json", fileCaptor.getValue().getPath().replace("\\", "/"));

        assertEquals(true, galleryCaptor.getValue() instanceof Gallery);

        gallery=(Gallery)galleryCaptor.getValue();
        
        album=gallery.getAlbum("album1");
        assertEquals(3, album.getImages().size());
        image=album.getImages().get(0);
        assertEquals("album1_image2.jpg", image.getImage());
        assertEquals("", image.getTitle());
        assertEquals("", image.getCaption());
        assertEquals("2020-05-01 19:00:13", image.getCaptureDateTime());
        assertEquals(null, image.getVideo());
        
        image=album.getImages().get(1);
        assertEquals("album1_image3.jpg", image.getImage());
        assertEquals("2020-05-01 20:00:12", image.getCaptureDateTime());

        image=album.getImages().get(2);
        assertEquals("album1_image1.jpg", image.getImage());
        assertEquals("2020-05-02 08:19:00", image.getCaptureDateTime());

        album=gallery.getAlbum("album2");
        assertEquals(2, album.getImages().size());
        
        verify(backupper, times(0)).backupFile(any(), any());
    }
    
    
    /**
     * Test of generate method, of class Generator.
     */
    @Test
    public void testGenerateSplit() throws IOException
    {
        ArgumentCaptor<Object>  galleryCaptor;
        ArgumentCaptor<File>    fileCaptor;
        Album                   album1;
        Album                   album2;
        Gallery                 gallery;
        
        galleryCaptor=ArgumentCaptor.forClass(Object.class);
        fileCaptor=ArgumentCaptor.forClass(File.class);
        
        System.out.println("generate, MERGE, new album, split");
        Options options         = new Options();
        options.galleryDirectory="src/test/resources/gallery";
        options.albumDirectory  =null;
        options.mode            =Generator.ModeType.MODE_MERGEEXISTING;
        options.addExifDateTime =true;
        options.split           =true;
        Generator instance      = new Generator(mapper, writer, backupper);
        instance.generate(options);
        verify(writer, times(3)).writeValue(fileCaptor.capture(), galleryCaptor.capture());
        
        assertEquals("src/test/resources/gallery/album1/album.json", fileCaptor.getAllValues().get(0).getPath().replace("\\", "/"));
        assertEquals("src/test/resources/gallery/album2/album.json", fileCaptor.getAllValues().get(1).getPath().replace("\\", "/"));
        assertEquals("src/test/resources/gallery/gallery.json", fileCaptor.getAllValues().get(2).getPath().replace("\\", "/"));

        assertEquals(true, galleryCaptor.getAllValues().get(0) instanceof Album);
        assertEquals(true, galleryCaptor.getAllValues().get(1) instanceof Album);
        assertEquals(true, galleryCaptor.getAllValues().get(2) instanceof Gallery);

        album1=(Album)galleryCaptor.getAllValues().get(0);
        album2=(Album)galleryCaptor.getAllValues().get(1);
        gallery=(Gallery)galleryCaptor.getAllValues().get(2);
        
        verify(backupper, times(0)).backupFile(any(), any());
    }


    /**
     * Test of generate method, of class Generator.
     */
    @Test
    public void testGenerateExistingGalleryNonSplit() throws IOException
    {
        ArgumentCaptor<Object>  galleryCaptor;
        ArgumentCaptor<File>    fileCaptor;
        ArgumentCaptor<File>    backupFileCaptor;
        Album                   album;
        Gallery                 gallery;
        Image                   image;
        
        galleryCaptor   =ArgumentCaptor.forClass(Object.class);
        fileCaptor      =ArgumentCaptor.forClass(File.class);
        backupFileCaptor=ArgumentCaptor.forClass(File.class);
        
        System.out.println("generate, MERGE, existing gallery, only merge album 1");
        Options options         = new Options();
        options.galleryDirectory="src/test/resources/gallery2";
        options.albumDirectory  ="album1";
        options.mode            =Generator.ModeType.MODE_MERGEEXISTING;
        options.addExifDateTime =true;
        options.split           =false;
        Generator instance      = new Generator(mapper, writer, backupper);
        instance.generate(options);
        
        verify(writer, times(1)).writeValue(fileCaptor.capture(), galleryCaptor.capture());

        assertEquals("src/test/resources/gallery2/gallery.json", fileCaptor.getValue().getPath().replace("\\", "/"));

        assertEquals(true, galleryCaptor.getValue() instanceof Gallery);

        gallery=(Gallery)galleryCaptor.getValue();
        
        album=gallery.getAlbum("album1");
        assertEquals("Test Album 1", album.getName());
        assertEquals("Description 1", album.getDescription());
        assertEquals("album1", album.getDirectory());
        assertEquals(3, album.getImages().size());
        image=album.getImages().get(0);
        assertEquals("album1_image3.jpg", image.getImage());
        assertEquals("Image 2", image.getTitle());
        assertEquals("Description", image.getCaption());
        assertEquals("2020-05-01 20:00:12", image.getCaptureDateTime());
        assertEquals(null, image.getVideo());
        
        image=album.getImages().get(1);
        assertEquals("album1_image1.jpg", image.getImage());
        assertEquals("2020-05-02 08:19:00", image.getCaptureDateTime());

        image=album.getImages().get(2);
        assertEquals("album1_image2.jpg", image.getImage());
        assertEquals("2020-05-01 19:00:13", image.getCaptureDateTime());

        album=gallery.getAlbum("album2");
        assertEquals("Test Album 2", album.getName());
        assertEquals("Description 2", album.getDescription());
        assertEquals("album2", album.getDirectory());
        assertEquals(1, album.getImages().size());
        image=album.getImages().get(0);
        assertEquals("album2_image2.jpg", image.getImage());
        assertEquals("Image 1", image.getTitle());
        assertEquals(null, image.getCaptureDateTime());
        
        verify(backupper, times(1)).backupFile(backupFileCaptor.capture(), any());
        assertEquals("src/test/resources/gallery2/gallery.json", backupFileCaptor.getValue().getPath().replace("\\", "/"));
    }

    /**
     * Test of generate method, of class Generator.
     */
    @Test
    public void testGenerateComplementExistingGalleryNonSplit() throws IOException
    {
        ArgumentCaptor<Object>  galleryCaptor;
        ArgumentCaptor<File>    fileCaptor;
        ArgumentCaptor<File>    backupFileCaptor;
        Album                   album;
        Gallery                 gallery;
        Image                   image;
        
        galleryCaptor   =ArgumentCaptor.forClass(Object.class);
        fileCaptor      =ArgumentCaptor.forClass(File.class);
        backupFileCaptor=ArgumentCaptor.forClass(File.class);
        
        System.out.println("generate COMPLEMENT, existing gallery, all albums");
        Options options         = new Options();
        options.galleryDirectory="src/test/resources/gallery2";
        options.albumDirectory  =null;
        options.mode            =Generator.ModeType.MODE_COMPLEMENT;
        options.addExifDateTime =true;
        options.split           =false;
        Generator instance      = new Generator(mapper, writer, backupper);
        instance.generate(options);
        
        verify(writer, times(1)).writeValue(fileCaptor.capture(), galleryCaptor.capture());

        assertEquals("src/test/resources/gallery2/gallery.json", fileCaptor.getValue().getPath().replace("\\", "/"));

        assertEquals(true, galleryCaptor.getValue() instanceof Gallery);

        gallery=(Gallery)galleryCaptor.getValue();
        
        album=gallery.getAlbum("album1");
        assertEquals("Test Album 1", album.getName());
        assertEquals("Description 1", album.getDescription());
        assertEquals("album1", album.getDirectory());
        assertEquals(2, album.getImages().size());
        image=album.getImages().get(0);
        assertEquals("album1_image3.jpg", image.getImage());
        assertEquals("Image 2", image.getTitle());
        assertEquals("Description", image.getCaption());
        assertEquals("2020-05-01 20:00:12", image.getCaptureDateTime());
        assertEquals(null, image.getVideo());
        
        image=album.getImages().get(1);
        assertEquals("album1_image1.jpg", image.getImage());
        assertEquals("2020-05-02 08:19:00", image.getCaptureDateTime());

        album=gallery.getAlbum("album2");
        assertEquals("Test Album 2", album.getName());
        assertEquals("Description 2", album.getDescription());
        assertEquals("album2", album.getDirectory());
        assertEquals(1, album.getImages().size());
        image=album.getImages().get(0);
        assertEquals("album2_image2.jpg", image.getImage());
        assertEquals("Image 1", image.getTitle());
        assertEquals("2020-04-29 19:00:13", image.getCaptureDateTime());
        
        verify(backupper, times(1)).backupFile(backupFileCaptor.capture(), any());
        assertEquals("src/test/resources/gallery2/gallery.json", backupFileCaptor.getValue().getPath().replace("\\", "/"));
    }


    
}
