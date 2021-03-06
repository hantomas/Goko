/**
 * 
 */
package org.goko.core.gcode.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;

/**
 * @author PsyKo
 * @date 6 dec. 2015
 */
public interface IGCodeProviderRepositoryListener {
	
	public static int PRIORITY_HIGHEST = 1;
	
	public static int PRIORITY_STANDARD = 10;
	
	public static int PRIORITY_LEAST = 20;
	
	void onGCodeProviderCreate(IGCodeProvider provider) throws GkException;
	
	void onGCodeProviderUpdate(IGCodeProvider provider) throws GkException;
		
	void beforeGCodeProviderDelete(IGCodeProvider provider) throws GkException;
	
	void afterGCodeProviderDelete(IGCodeProvider provider) throws GkException;
	
	void onGCodeProviderLocked(IGCodeProvider provider) throws GkException;
	
	void onGCodeProviderUnlocked(IGCodeProvider provider) throws GkException;
}
