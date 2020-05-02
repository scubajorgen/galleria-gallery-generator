/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.gallerygenerator;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


/**
 *
 * @author jorgen
 */

@Configuration
@Import({ BeanConfiguration.class, Generator.class, FileBackupper.class })
public class GalleryGenerator implements CommandLineRunner
{
    private static final Logger LOG = LoggerFactory.getLogger(GalleryGenerator.class);    
    
    @Autowired
    Generator generator;
    
    /**
     * Print some nifty stuff if the user doesn't know how to use the application
     */
    private static void printHelp()
    {
        LOG.info("Usage: java -jar GalleryGenerator.jar <options>");
        LOG.info("       -g <gallery_dir> Directory of the gallery");
        LOG.info("       -a <album_dir>   Specific album to process (subdir of gallery_dir). If ommitted all albums are processed");
        LOG.info("       -e <yes/no>      Whether the exif capture date/time added to the json (yes) or not (no)");
        LOG.info("       -m <mode>        Defines what to do when an album has already been parsed: ");
        LOG.info("                        SKIP         to skip parsing of this album");
        LOG.info("                        MERGE        to append new found images at the end of the album");
        LOG.info("                        COMPLEMENT   to add new Images and parse capture date time for existing images");
        LOG.info("       -s <yes/no>      Specific whether one gallery.json is created containing all (no) or ");
        LOG.info("                        whether the file should be split in gallery.json and album.json files (yes)");
        LOG.info("Example: java -jar GalleryGenerator.jar -g gallery -a album -e no -s yes -m MERGE");
   }
    


    @Override
    public void run(String... args)
    {
        String              galleryDirectory=null;
        String              albumDirectory=null;
        Generator.ModeType  mode=Generator.ModeType.MODE_SKIPEXISTING;
        Options             options;
        
        options=Options.argsParser(args);
        
        if (options!=null)
        {
            generator.generate(options);            
        }
        else
        {
            printHelp();
        }        
    }
    
    
    /**
     * Starts the application
     * @param args Commandline arguments
     */
    public static void main(String[] args) 
    {
        LOG.info("Starting application");
        SpringApplication.run(GalleryGenerator.class, args);
    }
}
