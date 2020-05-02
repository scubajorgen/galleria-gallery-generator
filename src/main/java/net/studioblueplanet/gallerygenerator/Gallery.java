/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.gallerygenerator;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author jorgen
 */
public class Gallery 
{
    private String      name;
    private String      description;
    private List<Album> albums;
    
    /**
     * Constructor
     * @param name Name of the Gallery
     * @param description Description of the gallery
     * @param directory Directoryh
     */
    public Gallery(String name, String description, String directory)
    {
        this.name           =name;
        this.description    =description;
        this.albums=new ArrayList<>();
    }
    
    public Gallery()
    {
        
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<Album> getAlbums()
    {
        return albums;
    }

    public void setAlbums(List<Album> albums)
    {
        this.albums = albums;
    }
    
    public void addAlbum(Album album)
    {
        this.albums.add(album);
    }
    
    /**
     * Checks if an album exists with given name.
     * @param albumDirectory Album name to check
     * @return True if exists, false if not.
     */
    public boolean albumExists(String albumDirectory)
    {
        boolean exists;
        
        exists=false;
        for (Album album : albums)
        {
            if (album.getDirectory().equals(albumDirectory))
            {
                exists=true;
            }
        }
        return exists;
    }
    
    /**
     * Returns the Album with given name
     * @param albumDirectory Album to look for
     * @return The Album instance or null if not found
     */
    public Album getAlbum(String albumDirectory)
    {
        Album theAlbum;

        theAlbum=null;
        for (Album album : albums)
        {
            if (album.getDirectory().equals(albumDirectory))
            {
                theAlbum=album;
            }
        }
        return theAlbum;       
    }
    
    /**
     * Checks if the image file exists in indicated Album in this Gallery
     * @param albumName Album name
     * @param fileName Image or Video file name
     * @return True if it exists, false if not
     */
    public boolean imageFileExists(String albumName, String fileName)
    {
        boolean exists;
        Album   album;
        
        exists=false;
        album=getAlbum(albumName);
        if (album!=null)
        {
            for (Image image : album.getImages())
            {
                if (fileName.equals(image.getImage()) || fileName.equals(image.getVideo()))
                {
                    exists=true;
                }
            }
        }
        return exists;
    }
    
    /**
     * Retrieve image by album directory and file name
     * @param albumName Album name
     * @param fileName Image file name
     * @return The image or null if not found
     */
    public Image getImage(String albumName, String fileName)
    {
        Image   theImage;
        Album   album;
        
        theImage=null;
        album=getAlbum(albumName);
        if (album!=null)
        {
            for (Image image : album.getImages())
            {
                if (fileName.equals(image.getImage()) || fileName.equals(image.getVideo()))
                {
                    theImage=image;
                }
            }
        }
        return theImage;        
    }
    
    @Override
    public String toString()
    {
        String string;
        
        string= "Gallery: "+name+"\n";
        string+="         "+description+"\n";
        for (Album album : albums)
        {
            string+=album.toString();
        }
        
        return string;
    }
    
}
