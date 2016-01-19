/***************************************************************************
 * Copyright (c) 2007 Conselleria de Infraestructuras y Transporte,
 * Generalitat de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: 
 *   Mario Cervera Ubeda (Prodevelop) - Initial implementation
 *   H�ctor Iturria S�nchez (Prodevelop) - Message parameter added. Possibility to have a directory as target
 *
 **************************************************************************/
package es.cv.gvcase.launcher.xpand;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.mwe.core.monitor.NullProgressMonitor;

/**
 * The Class XPand2_Launcher.
 */
public class XPand2_Launcher {

	public static boolean runTransformation(String workflowPath,
			String destinationFile, String modelPath, String confModelPath) {

		return runTransformation(workflowPath, destinationFile, modelPath,
				confModelPath, null);
	}

	public static boolean runTransformation(String workflowPath,
			String destinationFile, String modelPath, String confModelPath,
			List<String> messageList) {

		return runTransformation(workflowPath, destinationFile, modelPath,
				confModelPath, null, messageList);
	}

	public static boolean runTransformation(String workflowPath,
			String destinationFile, String modelPath, String confModelPath,
			String traceModelPath, List<String> messageList) {

		return runTransformation(workflowPath, destinationFile, modelPath,
				confModelPath, null, null, null, messageList);
	}

	public static boolean runTransformation(String workflowPath,
			String destinationFile, String modelPath, String confModelPath,
			String traceModelPath, Map<String, String> oawProperties,
			Map<String, ?> oawSlotContents, List<String> messageList) {

		boolean result = false;

		if (oawProperties == null) {
			oawProperties = new HashMap<String, String>();
		}
		if (oawSlotContents == null) {
			oawSlotContents = new HashMap<String, Object>();
		}

		IPath path = new Path(destinationFile);

		String folder = null;
		String file = null;
		// If there is no file to generate, only a directory is given
		if (path.toFile().isDirectory()) {
			folder = destinationFile;

			oawProperties.put("srcGenPath", folder);
		} else {
			folder = path.toFile().getParent();
			file = path.toFile().getName();

			oawProperties.put("srcGenPath", folder);
			oawProperties.put("genFile", file);
		}

		oawProperties.put("modelFile", modelPath);
		if (traceModelPath != null) {
			oawProperties.put("traceModelFile", traceModelPath);
		}

		WorkflowRunnerWithMessages wrunner = new WorkflowRunnerWithMessages();

		// First delete a possible previousfile to make sure the transformation
		// creates
		// a new file from scratch
		deleteFile(destinationFile);

		result = wrunner.run(workflowPath, new NullProgressMonitor(),
				oawProperties, oawSlotContents, messageList);

		return result;
	}

	private static void deleteFile(String path) {

		File f = new File(path);
		if (f.exists() && f.canWrite() && !f.isDirectory()) {
			f.delete();
		}
	}

}
