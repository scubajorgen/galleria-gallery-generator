/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.gallerygenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jorgen
 */
public class Image implements Comparable<Image>
{
    public enum Sorting
    {
        SORTING_FILENAME,
        SORTING_DATETIME,
        SORTING_KEEPEXISTING
    }
    
    private static final Logger LOG = LoggerFactory.getLogger(Image.class);    
    private static Sorting sorting=Sorting.SORTING_KEEPEXISTING;
    
    String title;           // title
    String caption;         // caption
    String image;           // image filename, null for video
    String video;           // video filename, null for photo
    String captureDateTime; // capture date time

    public Image(String title, String caption, String captureDateTime, String image, String video)
    {
        this.title=title;
        this.caption=caption;
        this.captureDateTime=captureDateTime;
        this.image=image;
        this.video=video;
    }

    /**
     * Set the sorting method. Default is datetime
     * @param sortingMethod SORTING_FILENAME or SORTING_DATETIME
     */
    public static void setSortingMethod(Sorting sortingMethod)
    {
        sorting=sortingMethod;
    }
    
    public String getCaptureDateTime()
    {
        return captureDateTime;
    }

    public void setCaptureDateTime(String captureDateTime)
    {
        this.captureDateTime = captureDateTime;
    }
    
    public Image()
    {
        
    }
    
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getCaption()
    {
        return caption;
    }

    public void setCaption(String caption)
    {
        this.caption = caption;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getVideo()
    {
        return video;
    }

    public void setVideo(String video)
    {
        this.video = video;
    }
    
    @Override
    public String toString()
    {
        String string;
        string ="    Image    "+title+"\n";
        string+="             "+caption+"\n";
        if (image!=null)
        {
            string+="             "+image+"\n";
        }
        if (video!=null)
        {
            string+="             "+video+"\n";
        }
        return string;
    }    
    
    /**
     * Comparator based on datetime of the photo
     * @param image Image to compare to 
     * @return -1 if image date time is earlier or null
     */
    @Override
    public int compareTo(Image image) 
    {
        String dateTime;
        String thisName =null;
        String name     =null;
        int    compare;
        
        compare=0;

        if (sorting==Sorting.SORTING_FILENAME)
        {
            if (this.image!=null && !this.image.equals(""))
            {
                thisName=this.image;
            }
            else if (this.video!=null && !this.video.equals(""))
            {
                thisName=this.video;
            }
            else
            {
                LOG.error("No suitable filename found for sorting");
            }
            
            if (image.image!=null && !image.image.equals(""))
            {
                name=image.image;
            }
            else if (image.video!=null && !image.video.equals(""))
            {
                name=image.video;
            }
            else
            {
                LOG.error("No suitable filename found for sorting");
            }
            if (thisName!=null && name!=null)
            {
                compare=thisName.compareTo(name);
            } 
        }
        else if (sorting==Sorting.SORTING_DATETIME)
        {
            dateTime=image.getCaptureDateTime();

            if (this.captureDateTime==null)
            {
                compare=-1;
            }
            else
            {
                if (dateTime==null)
                {
                    compare=1;
                }
                else
                {
                    compare=this.captureDateTime.compareTo(dateTime);
                }
            }
        }
        else if (sorting==Sorting.SORTING_KEEPEXISTING)
        {
            compare=0;
        }

        return compare;
    }    
   
}
