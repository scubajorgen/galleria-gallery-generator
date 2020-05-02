/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.gallerygenerator;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 *
 * @author jorgen
 */
@Component
public class Generator
{
    public enum ModeType
    {
        MODE_MERGEEXISTING,
        MODE_SKIPEXISTING,
        MODE_COMPLEMENT
    }

    private static final Logger LOG = LoggerFactory.getLogger(Generator.class);    
    
    private static final String JSONGALLERYFILE         ="gallery.json";
    private static final String JSONALBUMFILE           ="album.json";
    private static final String BACKUPJSONGALLERYFILE   ="gallery.json.bck";
    private static final String BACKUPJSONALBUMFILE     ="album.json.bck";
    private static final String IMAGEDIR                ="image";
    private static final String THUMBNAILDIR            ="thumbnail";
    
    private static final String[] PHOTOFILETYPES={"jpg", "jpeg", "gif", "png"};
    private static final String[] VIDEOFILETYPES={"mpg", "mp4", "mp2"};

    private final ObjectMapper  objectMapper;
    
    private final ObjectWriter  objectWriter;
    
    private final FileBackupper backupper;
    
    /**
     * Read existing json file defining the gallery
     * @param galleryDirectory Directory where to find the gallery json file
     * @return Gallery instance
     */
    private Gallery readJsonGallery(String galleryDirectory)
    {
        Gallery     gallery;
        String      fileName;
        List<Album> albums;
        Album       albumRead;
        
        gallery=null;
        fileName=galleryDirectory+'/'+JSONGALLERYFILE;
        try
        {
            gallery = objectMapper.readValue(new File(fileName), Gallery.class);
            albums  = gallery.getAlbums();
            for (Album album : albums)
            {
                if (album.getImages()==null)
                {
                    albumRead=readJsonAlbum(galleryDirectory, album);
                    album.setImages(albumRead.getImages());
                }
                else
                {
                    if (jsonAlbumExists(galleryDirectory, album))
                    {
                        albumRead=readJsonAlbum(galleryDirectory, album);
                        LOG.warn("This gallery contains image definitions in the gallery as well as an album file ({})", album.getDirectory());
                        if (albumRead.getImages()!=null)
                        {
                            LOG.warn("Album file ({}) and Gallery file contain image definitions", album.getDirectory());
                        }
                    }
                }
                    
            }
        }
        catch (IOException e)
        {
            LOG.error("Error reading file: {}", e.getMessage());
        }
        return gallery;
    }
    
    /**
     * Reads the album.json file, containing the images
     * @param albumDirectory Album directory, full path
     * @param album Album to add the images to
     * @throws IOException If file reading fails.
     */
    private Album readJsonAlbum(String galleryDirectory, Album album) throws IOException
    {
        Album           jsonAlbum;
        String          fileName;
        List<Image>     images;
        
        fileName=galleryDirectory+'/'+album.getDirectory()+'/'+JSONALBUMFILE;
        jsonAlbum=objectMapper.readValue(new File(fileName), Album.class);
        
        return jsonAlbum;
    }
    
    /**
     * Check if 
     * @param albumDirectory
     * @return 
     */
    private boolean jsonAlbumExists(String galleryDirectory, Album album)
    {
        File    file;
        
        file=new File(galleryDirectory+'/'+album.getDirectory()+'/'+JSONALBUMFILE);
        return file.exists();
    }
    
    /**
     * Write the Gallery as gallery.json file
     * @param gallery Gallery to serialize
     * @param galleryDirectory Directory to write the json file to
     */
    private void writeJsonGallery(Gallery gallery, Options options)
    {
        String      galleryFileName;
        String      albumFileName;
        File        galleryFile;
        File        albumFile;
        File        backupFile;
        List<Album> albums;
        
        galleryFileName=options.galleryDirectory+'/'+JSONGALLERYFILE;
        
        galleryFile =new File(galleryFileName);
        if (galleryFile.exists())
        {
            backupFile=new File(options.galleryDirectory+'/'+BACKUPJSONGALLERYFILE);
            backupper.backupFile(galleryFile, backupFile);
            LOG.info("Original json file renamed to {}", backupFile.getAbsolutePath());
        }
        try
        {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.setSerializationInclusion(Include.NON_NULL); 
//            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//            ObjectWriter objectWriter =objectMapper.writer(new MyPrettyPrinter());
            
            if (options.split)
            {
                albums=gallery.getAlbums();
                for (Album album : albums)
                {
                    albumFileName=options.galleryDirectory+'/'+album.getDirectory()+'/'+JSONALBUMFILE;
                    if (album.isChanged())
                    {
                        albumFileName=options.galleryDirectory+'/'+album.getDirectory()+'/'+JSONALBUMFILE;
                        albumFile =new File(albumFileName);
                        if (albumFile.exists())
                        {
                            backupFile=new File(options.galleryDirectory+'/'+album.getDirectory()+'/'+BACKUPJSONALBUMFILE);
                            backupper.backupFile(albumFile, backupFile);
                            LOG.info("Original json file renamed to {}", backupFile.getAbsolutePath());
                        }
                        objectWriter.writeValue(new File(albumFileName), album);
                        LOG.info("New json file written to {}", albumFile.getAbsolutePath());
                    }
                    else
                    {
                        LOG.info("Abum file {} not written because the album was not changed", albumFileName);
                    }
                    // set the images to null to prevent them from being also written to the gallery JSON file
                    album.setImages(null);
                }
            }
            objectWriter.writeValue(new File(galleryFileName), gallery);
            LOG.info("New json file written to {}", galleryFile.getAbsolutePath());
        }
        catch (IOException e)
        {
            LOG.error("Error writing file: {}", e.getMessage());
        }
        
    }
    
    /**
     * Indicates whether the file type is allowed, based on file type
     * @param fileName File name
     * @return True if allowed, false if not
     */
    private boolean isAllowedFileName(String fileName)
    {
        boolean isAllowed=false;
        
        for(String type : PHOTOFILETYPES)
        {
            if (fileName.toLowerCase().endsWith(type))
            {
                isAllowed=true;
            }
        }
        for(String type : VIDEOFILETYPES)
        {
            if (fileName.toLowerCase().endsWith(type))
            {
                isAllowed=true;
            }
        }
        return isAllowed;
    }
    
    /**
     * Checks if the file is a photo file, based on file extension
     * @param fileName File name
     * @return True if it is a photo
     */
    private boolean isPhotoFile(String fileName)
    {
        boolean isPhoto=false;
        
        for(String type : PHOTOFILETYPES)
        {
            if (fileName.toLowerCase().endsWith(type))
            {
                isPhoto=true;
            }
        }
        return isPhoto;        
    }
    
        /**
     * Checks if the file is a photo file, based on file extension
     * @param fileName File name
     * @return True if it is a photo
     */
    private boolean isJpgFile(String fileName)
    {
        boolean isPhoto=false;
        

        if (fileName.toLowerCase().endsWith("jpg") || fileName.toLowerCase().endsWith("jpeg"))
        {
            isPhoto=true;
        }

        return isPhoto;        
    }
    
    /**
     * Checks if the file is a video file, based on file extension
     * @param fileName File name
     * @return True if it is a photo
     */
    private boolean isVideoFile(String fileName)
    {
        boolean isVideo=false;
        
        for(String type : VIDEOFILETYPES)
        {
            if (fileName.toLowerCase().endsWith(type))
            {
                isVideo=true;
            }
        }
        return isVideo;        
    }    
    
    /**
     * Parse the indicated Album directory and make an inventory. Add the
     * found files to the Gallery
     * @param gallery Gallery to update
     * @param options Options controlling the process
     */
    private void parseAlbumDirectory(Gallery gallery, Options options)
    {
        File                imagePath;
        File                thumbnail;
        File[]              files;
        String              fileName;
        Album               album;
        int                 count;
        JpegImageMetadata   metadata;
        String              captureDateTime;
        boolean             isNew;
        
        count=0;
        if (!gallery.albumExists(options.albumDirectory) ||
            (gallery.albumExists(options.albumDirectory) && (options.mode==ModeType.MODE_MERGEEXISTING || 
                                                             options.mode==ModeType.MODE_COMPLEMENT)))
        {
            if (!gallery.albumExists(options.albumDirectory))
            {
                album=new Album("", "", options.albumDirectory);
                gallery.addAlbum(album);
                isNew=true;
            }
            else
            {
                album=gallery.getAlbum(options.albumDirectory);
                isNew=false;
            }

            imagePath=new File(options.galleryDirectory+"/"+options.albumDirectory+"/"+IMAGEDIR);
            files = imagePath.listFiles();
            for(File file : files)
            {
                if (file.isFile())
                {
                    fileName=file.getName();
                    if (isAllowedFileName(fileName))
                    {
                        // Get the capture date time
                        captureDateTime=null;
                        if (isJpgFile(fileName))
                        {
                            try
                            {
                                metadata = (JpegImageMetadata)Imaging.getMetadata(file);
                                TiffField field=metadata.findEXIFValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
                                if ((field!=null) && (options.addExifDateTime))
                                {
                                    captureDateTime=field.getValueDescription();
                                    captureDateTime=captureDateTime.replaceAll("'([0-9]{4}):([0-9]{2}):([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2})'", 
                                                                               "$1-$2-$3 $4:$5:$6");
                                }
                            }
                            catch(Exception e)
                            {
                                LOG.error("Error {}", e.getMessage());
                            }
                        }
                        
                        if (!gallery.imageFileExists(options.albumDirectory, fileName) && (options.mode==ModeType.MODE_MERGEEXISTING))
                        {
                            LOG.debug("Adding new image {}", fileName);

                            if (isPhotoFile(fileName))
                            {
                                album.addImage(new Image("", "", captureDateTime, fileName, null));
                                count++;
                            }
                            else if (isVideoFile(fileName))
                            {
                                album.addImage(new Image("", "", captureDateTime, null, fileName));
                                count++;
                            }
                            else
                            {
                                LOG.error("Not allowed file type {}", fileName);
                            }

                            thumbnail=new File(options.galleryDirectory+"/"+options.albumDirectory+"/"+THUMBNAILDIR+"/"+fileName);
                            if (!thumbnail.exists())
                            {
                                LOG.warn("Found file {} but not the thumbnail...", fileName);
                            }
                        }
                        else
                        {
                            if (options.mode==ModeType.MODE_COMPLEMENT)
                            {
                                Image image=gallery.getImage(options.albumDirectory, fileName);
                                if (image!=null)
                                {
                                    if (image.getCaptureDateTime()==null && captureDateTime!=null)
                                    {
                                        image.setCaptureDateTime(captureDateTime);
                                        LOG.debug("Complementing image {}, setting date to {}", fileName, captureDateTime);
                                    }
                                    else
                                    {
                                        LOG.debug("Skipping image {} because datetime already set or no date in exif", fileName);
                                    }
                                }
                                else
                                {
                                    LOG.debug("Skipping image {} because image not in JSON; first merge", fileName);
                                }
                            }
                            else
                            {
                                LOG.debug("Skipping image {} because of mode", fileName);
                            }
                        }
                    }
                }

            }
            // Only sort new albums; existing: keep existing order
            if (isNew)
            {
                Collections.sort(album.getImages());
            }
            LOG.info("Added {} items to album {}", count, options.albumDirectory);
        }
        else
        {
            LOG.info("Skipping album {}", options.albumDirectory);
        }
    }
    
    /**
     * Parse all subdirectories in the gallery directory as album
     * @param gallery The gallery instance to update
     * @param options Options controlling the process
     */
    private void parseAlbumDirectories(Gallery gallery, Options options)
    {
        File    galleryDir;
        File[]  files;
        
        galleryDir=new File(options.galleryDirectory);
        files = galleryDir.listFiles();
        for(File file : files)
        {
            if (file.isDirectory())
            {
                options.albumDirectory=file.getName();
                parseAlbumDirectory(gallery, options);
            }
        }
    }
    
    @Autowired
    public Generator(ObjectMapper mapper, ObjectWriter writer, FileBackupper backupper)
    {
        this.objectMapper=mapper;
        this.objectWriter=writer;
        this.backupper=backupper;
    }
    
    
    /**
     * Generate or update the gallery.json gallery definition file.
     * @param options Options controlling the generation process
     */
    public void generate(Options options)
    {
        Gallery gallery;
        
        // If the gallery json file exists, read it; else new gallery
        if (new File(options.galleryDirectory+'/'+JSONGALLERYFILE).exists())
        {
            gallery=readJsonGallery(options.galleryDirectory);
        }
        else
        {
            gallery=new Gallery("Gallery", "Description", null);
        }
        
        if (options.albumDirectory!=null)
        {
            this.parseAlbumDirectory(gallery, options);
        }
        else
        {
            this.parseAlbumDirectories(gallery, options);
        }
        
        writeJsonGallery(gallery, options);
        LOG.info("Done!");
    }
}
