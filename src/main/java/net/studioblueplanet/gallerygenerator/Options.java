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
public class Options
{
    public String               galleryDirectory    =null;
    public String               albumDirectory      =null;
    public Generator.ModeType   mode                =Generator.ModeType.MODE_SKIPEXISTING;  
    public boolean              split               =false;
    public boolean              addExifDateTime     =false;
    
    
    private Generator.ModeType getMode(String argument)
    {
        Generator.ModeType mode;
        
        mode=Generator.ModeType.MODE_SKIPEXISTING;
        if (argument.toLowerCase().equals("merge"))
        {
            mode=Generator.ModeType.MODE_MERGEEXISTING;
        }
        else if (argument.toLowerCase().equals("complement"))
        {
            mode=Generator.ModeType.MODE_COMPLEMENT;
        }
        else if(!argument.toLowerCase().equals("skip"))
        {
            System.err.println("Not a valid mode: "+argument+". Assuming SKIP");
        }
        return mode;
    }    
    
    public static Options argsParser(String[] args)
    {
        Options options;
        int     i;
        char    option;
        
        if (args.length%2==0 && args.length>0)
        {
            options=new Options();
            i=0;
            while (i<args.length)
            {
                if (args[i].startsWith(""))
                {
                    option=args[i].charAt(1);
                    switch(option)
                    {
                        case 'g':
                            options.galleryDirectory=args[i+1];
                            break;
                        case 'a':
                            options.albumDirectory=args[i+1];
                            break;
                        case 'e':
                            options.addExifDateTime=args[i+1].toLowerCase().equals("yes");;
                            break;
                        case 'm':
                            options.mode=options.getMode(args[i+1]);
                            break;
                        case 's':
                            options.split=args[i+1].toLowerCase().equals("yes");
                            break;
                    }
                }
                else
                {
                    options=null;
                    System.err.println("Illegal option "+args[i]);
                }
                i+=2;
            }
        }
        else
        {
            options=null;
            System.err.println("Incorrect options");
        }
        return options;
    }
    
}
