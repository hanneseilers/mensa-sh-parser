package de.mensa.sh.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Class to write to files.
 * Overload to use parser with other systems like android.
 * @author Hannes Eilers
 *
 */
public class FileWriter{

	private File mFile = null;
	
	/**
	 * Constructor
	 */
	public FileWriter(){}
	
	/**
	 * Sets file.
	 * @param aFile	{@link String} of filename and path.
	 */
	public void setFile(String aFile){
		mFile = new File(aFile);
	}
	
	/**
	 * @return	{@link String} of file content
	 * 			or {@code null} if file is not readable.
	 */
	public String readFromFile(){
		if( exsist() && isFile() && canRead() ){
			try {
				
				BufferedReader vReader = new BufferedReader( new FileReader(mFile) );
				String vFileText = "";
				String vLine;
				while( (vLine = vReader.readLine()) != null ){
					vFileText += vLine;
				}
				
				vReader.close();
				return vFileText;
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	/**
	 * Writes {@link String} to new file.
	 * @param aText	{@link String} to write.
	 * @return		{@code true} if successful, {@code false} otherwise.
	 */
	public boolean writeToFile(String aText){
		try {
			
			mFile.createNewFile();
			if( exsist() && isFile() && canWrite() ){
				
					OutputStreamWriter vWriter = new OutputStreamWriter( new FileOutputStream(mFile) );
					vWriter.write(aText);
					vWriter.close();
					return true;
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public long lastModified(){
		if( mFile != null ){
			return mFile.lastModified();
		}
		
		return 0L;
	}
	
	public boolean delete(){
		if( mFile != null ){
			return mFile.delete();
		}
		
		return false;
	}
	
	public boolean exsist(){
		return mFile != null && mFile.exists();
	}
	
	public boolean isFile(){
		return mFile != null && mFile.isFile();
	}
	
	public boolean canWrite(){
		return mFile != null && mFile.canWrite();
	}
	
	public boolean canRead(){
		return mFile != null && mFile.canRead();
	}
	
	public String getPath(){
		return mFile != null ? mFile.getPath() : "";
	}
	
	public String getName(){
		return mFile != null ? mFile.getName() : "";
	}
	
}
