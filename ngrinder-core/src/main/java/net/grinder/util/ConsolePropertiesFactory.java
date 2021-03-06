/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package net.grinder.util;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.ngrinder.common.exception.NGrinderRuntimeException;

import net.grinder.SingleConsole;
import net.grinder.console.model.ConsoleProperties;

/**
 * Convenient class for {@link ConsoleProperties} creation.
 * 
 * @author JunHo Yoon
 * @since 3.0
 */
public abstract class ConsolePropertiesFactory {
	/**
	 * Create empty {@link ConsoleProperties}. the created
	 * {@link ConsoleProperties} instance links with temp/temp_console
	 * directory.
	 * 
	 * @return empty {@link ConsoleProperties} instance
	 * 
	 */
	public static ConsoleProperties createEmptyConsoleProperties() {
		File tmpFile = null;
		try {
			tmpFile = File.createTempFile("ngrinder", "tmp");
			ConsoleProperties consoleProperties = new ConsoleProperties(SingleConsole.RESOURCE, tmpFile);

			return consoleProperties;
		} catch (Exception e) {
			String message = "Exception occurs while merging entities while creating empty console";
			throw new NGrinderRuntimeException(message, e);
		} finally {
			FileUtils.deleteQuietly(tmpFile);
		}
	}
}
