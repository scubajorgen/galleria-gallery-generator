# galleria-gallery-generator
## Introduction
This application generates the JSON files required for galleria-generator, based on the gallery directory content.

## Usage
1. Create the gallery directory structure
1. Copy the image and thumbnail files to the image directories
1. Run the GalleryGenerator application

java -jar GalleryGenerator.jar <options>

Options
* -g <galleryname>
  * The gallery directory. This option is obligatory
* -m <mode>
  * Mode
    * MERGE: Merge new images to existing albums. When capture date time is present, new albums are sorted; in existing 
      albums merged images are appended
    * COMPLEMENT: Don't merge, parse existing images and add capture date time if not already present
    * SKIP: Simply parse only new albums, don't process existing albums
* -a <album>
  * Album directory if you want to process a single album instead of the entire gallery
* -s <yes/no>
  * No: Use one grand unifying gallery.json, Yes: Use a gallery.json plus an album.json per album
* -e <yes/no>
  * No: Don't parse exif info, yes: parse exif info and add the capture date time as "captureDateTime"
  
## To do
* Generate thumbnails based on the images
