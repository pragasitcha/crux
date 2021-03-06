/*
 * Copyright (c) 2016. University Corporation for Atmospheric Research (UCAR). All rights reserved.
 */

package edu.ucar.ral.crux;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utility methods
 */
public class Utils {

  /**
   * Determines whether a URL or file path string points to a local file (as opposed to a remote URL)
   * @param path the file path or URL
   * @return true if the path represents a local file
   */
  public static boolean isLocalFile( String path ) {
    try {
      return "file".equals( new URL(path).getProtocol() );
    } catch (MalformedURLException e) {
      return true;
    }
  }

  /**
   * Return a file path which is unique under a base directory.
   * A base directory of /tmp/ and a file /home/username/rule/myschematron.sch would become
   * /tmp/home/username/rule/myschematron.xsl
   * @param baseDir the base directory under which the file is placed
   * @param file the file which should be placed under the base directory
   * @return a unique path under the base directory for the file
   * @throws java.io.IOException when a canonical path query encounters issues
   */
  public static String uniquePathUnder( File baseDir, File file ) throws IOException {
    //Remove colons from the file path which would cause problems on Windows, such as in C:/Users/foo
    return baseDir.getCanonicalPath() + File.separator + file.getParentFile().getCanonicalPath().replace( ":", "" );
  }

  /**
   * Export a resource embedded in a JAR file to a local file on disk.
   *
   * @param resourceName ie.: "/foo.txt"
   * @param outputFile the local path to where the resource should be written
   * @throws IOException if the resource name cannot be found in the classpath
   */
   public static void writeResourceToFile( String resourceName, File outputFile ) throws IOException {
    InputStream inputStream = null;
    try {
      inputStream = Utils.class.getResourceAsStream( resourceName );
      if(inputStream == null) {
        throw new IOException("Cannot get resource \"" + resourceName + "\" from Jar file.");
      }
      writeToFile( inputStream, outputFile );
    } finally {
      if( inputStream != null )
        inputStream.close();
    }
  }

  /**
   * Write an InputStream to an output file.  This method will close the InputStream when writing is finished
   * @param inputStream the input stream
   * @param outputFile the output file location the input stream will be written to
   * @throws IOException when I/O errors are encountered
   */
  public static void writeToFile( InputStream inputStream, File outputFile ) throws IOException{
    if(inputStream == null) {
      throw new NullPointerException("Input stream cannot be null");
    }

    int readBytes;
    byte[] buffer = new byte[4096];
    OutputStream resStreamOut = new FileOutputStream(outputFile);
    try {
      while( ( readBytes = inputStream.read( buffer ) ) > 0 ) {
        resStreamOut.write( buffer, 0, readBytes );
      }
    }finally{
      inputStream.close();
      resStreamOut.close();
    }
  }

  /**
   * Determine if the Java runtime is running on a Windows platform
   * @return whether the current platform is Windows
   */
  public static boolean isWindows(){
    return System.getProperty( "os.name" ).startsWith( "Windows" );
  }
}