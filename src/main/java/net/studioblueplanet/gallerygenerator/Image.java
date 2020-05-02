/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.gallerygenerator;

/**
 *
 * @author jorgen
 */
public class Image implements Comparable<Image>
{
    String title;
    String caption;
    String image;
    String video;
    String captureDateTime;

    public Image(String title, String caption, String captureDateTime, String image, String video)
    {
        this.title=title;
        this.caption=caption;
        this.captureDateTime=captureDateTime;
        this.image=image;
        this.video=video;
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
        int    compare;

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
        return compare;
    }    
   
}
