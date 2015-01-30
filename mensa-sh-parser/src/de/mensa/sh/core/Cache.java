package de.mensa.sh.core;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Class to chache network connections
 * @author Hannes Eilers
 *
 */
public class Cache {
	
	protected static FileWriterInterface sFileWriterClass = new DefaultFileWriter(); 
	protected static int sNetworkTimeout = 10000;
	protected static long sTimeout = 86400000L;
	
	/**
	 * Sets class of {@link DefaultFileWriter} to use for file access.
	 * @param aFileWriter	{@link Class} of {@link DefaultFileWriter}.
	 */
	public static void setFileWriterClass(FileWriterInterface aFileWriter){
		sFileWriterClass = aFileWriter;
	}
	
	/**
	 * Sets timout before to get a requested URL again.
	 * @param aTimeout	{@link Long} timeout in ms.
	 */
	public static void setTimeout(long aTimeout){
		sTimeout = aTimeout;
	}
	
	/**
	 * Sets timeout for network connections.
	 * @param aTimeout	{@link Integer} timeout in ms.
	 */
	public static void setNetworkTimeout(int aTimeout){
		sNetworkTimeout = aTimeout;
	}

	/**
	 * Gets {@link Jsoup} {@link Document} from URL.
	 * Used cached version if available and timeout not exceeded.
	 * If new online version is used, it's getting cached for future requests.
	 * @param aURL	{@link String} of URL to get content from.
	 * @return		{@link Document} of URL, otherwise and on error {@code null}.
	 */
	public static Document getDocument(String aURL){
		
		try {
			
			// Generate unique hash of url
			MessageDigest vDigest = MessageDigest.getInstance("MD5");
			vDigest.update( aURL.getBytes() );
			String vHash = "";
			for( byte b : vDigest.digest() ){
				vHash += String.format("%02x", b);
			}
			
			// get file
			FileWriterInterface vFile = sFileWriterClass.getClass().newInstance();
			vFile.setFile(vHash);
			
			// check if file exists and is newer than timeout
			if( vFile.exsist()
					&& (System.currentTimeMillis() - vFile.lastModified()) < sTimeout
					&& vFile.canRead() ){
				
				String vHTML = vFile.readFromFile();
				if( vHTML != null ){
					return Jsoup.parse(vHTML);
				}
				
			}
			
			Connection vConnection = Jsoup.connect(aURL);
			vConnection.timeout(sNetworkTimeout);
			Document vDocument = vConnection.get();
			vFile.writeToFile( vDocument.html() );
			return vDocument;
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
