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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
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
    
    private void validateNewAlbums(Album album1, Album album2)
    {
        Image image;
        
        assertEquals("", album1.getName());
        assertEquals("", album1.getDescription());
        assertEquals("album1", album1.getDirectory());
        assertEquals("thumbnail.jpg", album1.getThumbnail());
        
        assertEquals(3, album1.getImages().size());
        image=album1.getImages().get(0);
        assertEquals("album1_image2.jpg", image.getImage());
        assertEquals("", image.getTitle());
        assertEquals("", image.getCaption());
        assertEquals("2020-05-01 19:00:13", image.getCaptureDateTime());
        assertEquals(null, image.getVideo());
        
        image=album1.getImages().get(1);
        assertEquals("album1_image3.jpg", image.getImage());
        assertEquals("2020-05-01 20:00:12", image.getCaptureDateTime());

        image=album1.getImages().get(2);
        assertEquals("album1_image1.jpg", image.getImage());
        assertEquals("2020-05-02 08:19:00", image.getCaptureDateTime());        

        assertEquals("", album2.getName());
        assertEquals("", album2.getDescription());
        assertEquals("album2", album2.getDirectory());
        assertEquals("thumbnail.jpg", album2.getThumbnail());

        assertEquals(2, album2.getImages().size());
        image=album2.getImages().get(0);
        assertEquals("album2_image2.jpg", image.getImage());
        assertEquals("", image.getTitle());
        assertEquals("", image.getCaption());
        assertEquals("2020-04-29 19:00:13", image.getCaptureDateTime());
    }


    private void validateExistingAlbums(Album album1, int noImages1, Album album2, int noImages2)
    {
        Image image;
        
        assertEquals("Test Album 1", album1.getName());
        assertEquals("Description 1", album1.getDescription());
        assertEquals("album1", album1.getDirectory());
        assertEquals(noImages1, album1.getImages().size());

        image=album1.getImages().get(0);
        assertEquals("album1_image3.jpg", image.getImage());
        assertEquals("Image 2", image.getTitle());
        assertEquals("Description", image.getCaption());
        assertEquals("2020-05-01 20:00:12", image.getCaptureDateTime());
        assertEquals(null, image.getVideo());
        
        image=album1.getImages().get(1);
        assertEquals("album1_image1.jpg", image.getImage());
        assertEquals("2020-05-02 08:19:00", image.getCaptureDateTime());

        if (noImages1==3)
        {
            image=album1.getImages().get(2);
            assertEquals("album1_image2.jpg", image.getImage());
            assertEquals("", image.getTitle());
            assertEquals("", image.getCaption());
            assertEquals("2020-05-01 19:00:13", image.getCaptureDateTime());
        }
        
        assertEquals("Test Album 2", album2.getName());
        assertEquals("Description 2", album2.getDescription());
        assertEquals("album2", album2.getDirectory());
        assertEquals(noImages2, album2.getImages().size());
        image=album2.getImages().get(0);
        assertEquals("album2_image2.jpg", image.getImage());
        assertEquals("Image 1", image.getTitle()); 
        
        if (noImages2==2)
        {
            image=album2.getImages().get(1);
            assertEquals("album2_image1.jpg", image.getImage());
            assertEquals("", image.getTitle());            
        }
    }

    
    /**
     * Test of generate method, of class Generator.
     */
    @Test
    public void testGenerateNonSplit() throws IOException
    {
        ArgumentCaptor<Object>  galleryCaptor;
        ArgumentCaptor<File>    fileCaptor;
        Gallery                 gallery;
        
        galleryCaptor=ArgumentCaptor.forClass(Object.class);
        fileCaptor=ArgumentCaptor.forClass(File.class);
        
        System.out.println("generate: MERGE, new album, no split");
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
        
        validateNewAlbums(gallery.getAlbum("album1"), gallery.getAlbum("album2"));

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
        
        System.out.println("generate: MERGE, new album, split");
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
        
        assertEquals(null, gallery.getAlbum("album1").getImages());
        assertNotEquals(null, album1.getImages());
        assertNotEquals(null, album2.getImages());
        
        validateNewAlbums(album1, album2);
       
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
        Album                   album1;
        Album                   album2;
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
        
        album1=gallery.getAlbum("album1");
        album2=gallery.getAlbum("album2");
        validateExistingAlbums(album1, 3, album2, 1);
        
        assertEquals(null, album2.getImages().get(0).getCaptureDateTime());
        
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
        Album                   album1;
        Album                   album2;
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
        
        album1=gallery.getAlbum("album1");
        album2=gallery.getAlbum("album2");
        this.validateExistingAlbums(album1, 2, album2, 1);
        
        assertEquals("2020-04-29 19:00:13", album2.getImages().get(0).getCaptureDateTime());
        
        verify(backupper, times(1)).backupFile(backupFileCaptor.capture(), any());
        assertEquals("src/test/resources/gallery2/gallery.json", backupFileCaptor.getValue().getPath().replace("\\", "/"));
    }

    /**
     * Test of generate method, of class Generator.
     */
    @Test
    public void testGenerateSkipExistingGalleryNonSplit() throws IOException
    {
        ArgumentCaptor<Object>  galleryCaptor;
        ArgumentCaptor<File>    fileCaptor;
        ArgumentCaptor<File>    backupFileCaptor;
        Album                   album1;
        Album                   album2;
        Gallery                 gallery;
        Image                   image;
        
        galleryCaptor   =ArgumentCaptor.forClass(Object.class);
        fileCaptor      =ArgumentCaptor.forClass(File.class);
        backupFileCaptor=ArgumentCaptor.forClass(File.class);
        
        System.out.println("generate COMPLEMENT, existing gallery, all albums");
        Options options         = new Options();
        options.galleryDirectory="src/test/resources/gallery2";
        options.albumDirectory  =null;
        options.mode            =Generator.ModeType.MODE_SKIPEXISTING;
        options.addExifDateTime =true;
        options.split           =false;
        Generator instance      = new Generator(mapper, writer, backupper);
        instance.generate(options);
        
        verify(writer, times(1)).writeValue(fileCaptor.capture(), galleryCaptor.capture());

        assertEquals("src/test/resources/gallery2/gallery.json", fileCaptor.getValue().getPath().replace("\\", "/"));

        assertEquals(true, galleryCaptor.getValue() instanceof Gallery);

        gallery=(Gallery)galleryCaptor.getValue();
        
        album1=gallery.getAlbum("album1");
        album2=gallery.getAlbum("album2");
        this.validateExistingAlbums(album1, 2, album2, 1);
        assertEquals(null, album2.getImages().get(0).getCaptureDateTime());
        
        verify(backupper, times(1)).backupFile(backupFileCaptor.capture(), any());
        assertEquals("src/test/resources/gallery2/gallery.json", backupFileCaptor.getValue().getPath().replace("\\", "/"));
    }

    /**
     * Test of generate method, of class Generator.
     */
    @Test
    public void testGenerateExistingGallerySplit() throws IOException
    {
        ArgumentCaptor<Object>  galleryCaptor;
        ArgumentCaptor<File>    fileCaptor;
        ArgumentCaptor<File>    backupFileCaptor;
        Album                   album1;
        Album                   album2;
        Gallery                 gallery;
        Image                   image;
        
        galleryCaptor   =ArgumentCaptor.forClass(Object.class);
        fileCaptor      =ArgumentCaptor.forClass(File.class);
        backupFileCaptor=ArgumentCaptor.forClass(File.class);
        
        System.out.println("generate, MERGE, existing gallery, split");
        Options options         = new Options();
        options.galleryDirectory="src/test/resources/gallery2";
        options.albumDirectory  =null;
        options.mode            =Generator.ModeType.MODE_MERGEEXISTING;
        options.addExifDateTime =true;
        options.split           =true;
        Generator instance      = new Generator(mapper, writer, backupper);
        instance.generate(options);
        
        verify(writer, times(3)).writeValue(fileCaptor.capture(), galleryCaptor.capture());

        assertEquals("src/test/resources/gallery2/album1/album.json", fileCaptor.getAllValues().get(0).getPath().replace("\\", "/"));
        assertEquals("src/test/resources/gallery2/album2/album.json", fileCaptor.getAllValues().get(1).getPath().replace("\\", "/"));
        assertEquals("src/test/resources/gallery2/gallery.json", fileCaptor.getAllValues().get(2).getPath().replace("\\", "/"));

        assertEquals(true, galleryCaptor.getAllValues().get(0) instanceof Album);
        assertEquals(true, galleryCaptor.getAllValues().get(1) instanceof Album);
        assertEquals(true, galleryCaptor.getAllValues().get(2) instanceof Gallery);

        album1=(Album)galleryCaptor.getAllValues().get(0);
        album2=(Album)galleryCaptor.getAllValues().get(1);
        gallery=(Gallery)galleryCaptor.getAllValues().get(2);
        
        assertEquals(null, gallery.getAlbum("album1").getImages());
        assertEquals(null, gallery.getAlbum("album2").getImages());
        assertNotEquals(null, album1.getImages());
        assertNotEquals(null, album2.getImages());
        
        validateExistingAlbums(album1, 3, album2, 2);
       
        verify(backupper, times(1)).backupFile(backupFileCaptor.capture(), any());
        assertEquals("src/test/resources/gallery2/gallery.json", backupFileCaptor.getValue().getPath().replace("\\", "/"));
    }

    
}
