package de.mensa.sh.core;

public interface FileWriterInterface {

	/**
	 * Sets file.
	 * @param aFile	{@link String} of filename and path.
	 */
	public void setFile(String aFile);
	
	/**
	 * @return	{@link String} of file content
	 * 			or {@code null} if file is not readable.
	 */
	public String readFromFile();
	
	/**
	 * Writes {@link String} to new file.
	 * @param aText	{@link String} to write.
	 * @return		{@code true} if successful, {@code false} otherwise.
	 */
	public boolean writeToFile(String aText);
	
	
	public long lastModified();
	
	public boolean delete();
	
	public boolean exsist();
	
	public boolean isFile();
	
	public boolean canWrite();
	
	public boolean canRead();
	
	public String getPath();
	
	public String getName();
	
}
