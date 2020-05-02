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

/**
 *
 * @author jorgen
 */
public class OptionsTest
{
    
    public OptionsTest()
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
     * Test of argsParser method, of class Options.
     */
    @Test
    public void testArgsParser()
    {
        System.out.println("argsParser");
        String[] args1={};
        String[] args2={"-s", "yes", "-g"};
        String[] args3={"-g", "c:/test", "-a", "album", "-s", "yes", "-m", "MERGE", "-e",  "YeS"};
        String[] args4={"-g", "c:/test2", "-a", "album2", "-s", "nO", "-m", "CoMpLeMeNt", "-e",  "nonsense"};
        
        Options result;
        
        // No options
        result=Options.argsParser(args1);
        assertEquals(null, result);

        // Odd number of args
        result=Options.argsParser(args2);
        assertEquals(null, result);
        
        // Options
        result=Options.argsParser(args3);
        assertEquals(args3[1], result.galleryDirectory);
        assertEquals(args3[3], result.albumDirectory);
        assertEquals(true, result.addExifDateTime);
        assertEquals(true, result.split);
        assertEquals(Generator.ModeType.MODE_MERGEEXISTING, result.mode);
        
        // Options
        result=Options.argsParser(args4);
        assertEquals(args4[1], result.galleryDirectory);
        assertEquals(args4[3], result.albumDirectory);
        assertEquals(false, result.addExifDateTime);
        assertEquals(false, result.split);
        assertEquals(Generator.ModeType.MODE_COMPLEMENT, result.mode);
        

    }
    
}
