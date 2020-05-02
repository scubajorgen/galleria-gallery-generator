/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.gallerygenerator;

import org.springframework.stereotype.Component;
import java.io.File;
/**
 *
 * @author jorgen
 */
@Component
public class FileBackupper
{
    public void backupFile(File file, File backupFile)
    {
        file.renameTo(backupFile);
    }
}
