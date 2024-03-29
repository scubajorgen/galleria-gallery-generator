/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.gallerygenerator;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collections;

/**
 *
 * @author jorgen
 */

public class Album 
{
    private String      name;
    private String      description;
    private String      directory;
    private String      thumbnail;
    private List<Image> images;
    
    @JsonIgnore
    private boolean     changed;
    
    @JsonIgnore
    private boolean     imagesReadFromGallery;

    /**
     * Constructor
     * @param name Name of the Album
     * @param description Description of the album
     * @param directory Subdirectory
     */
    public Album(String name, String description, String directory)
    {
        this.name=name;
        this.description=description;
        this.directory=directory;
        this.thumbnail="thumbnail.jpg";
        this.images=new ArrayList<>();
    }
    
    public Album()
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

    public String getDirectory()
    {
        return directory;
    }

    public void setDirectory(String directory)
    {
        this.directory = directory;
    }

    public String getThumbnail()
    {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail)
    {
        this.thumbnail = thumbnail;
    }

    public List<Image> getImages()
    {
        return images;
    }

    public void setImages(List<Image> images)
    {
        this.images = images;
        changed=false;
    }
    
    public void addImage(Image image)
    {
        this.images.add(image);
        changed=true;
    }
    
    public boolean isChanged()
    {
        return changed;
    }

    public boolean isImagesReadFromGallery()
    {
        return imagesReadFromGallery;
    }

    public void setImagesReadFromGallery(boolean imagesReadFromGallery)
    {
        this.imagesReadFromGallery = imagesReadFromGallery;
    }
    
    /**
     * Sort the images according to the method set
     */
    public void sort()
    {
        Collections.sort(images);
        changed=true;
    }


    
    /**
     * Creates a clone of this Album, however, without the images
     * @return The cloned album
     */
    public Album cloneNoImages()
    {
        Album album;
        
        album=new Album();
        
        album.name          =name;
        album.description   =description;
        album.thumbnail     =thumbnail;
        album.directory     =directory;
        album.changed       =changed;
        return album;
    }
    
    @Override
    public String toString()
    {
        String string;
        string ="  Album    "+name+"\n";
        string+="           "+description+"\n";
        for (Image image: images)
        {
            string+=image.toString();
        }
        return string;
    }
}
