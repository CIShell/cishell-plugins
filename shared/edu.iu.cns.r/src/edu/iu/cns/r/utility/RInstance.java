package edu.iu.cns.r.utility;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.cishell.utilities.FileCopyingException;
import org.cishell.utilities.FileUtilities;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

import bsh.EvalError;

import com.google.common.collect.Sets;

import edu.iu.cns.r.exportdata.RFileExportLog;

public class RInstance {
	public static final String CSV_FILE_EXTENSION = ".csv";

	public static final String TEMPORARY_WORKING_DIRECTORY_PREFIX = "r_instance";
	public static final String TEMPORARY_IMPORT_TABLE_PREFIX = "_import_";
	public static final String TEMPORARY_EXPORT_TABLE_PREFIX = "_export_";

	private String rHome;
	private bsh.Interpreter javaInterpreter = new bsh.Interpreter();
	private File temporaryWorkingDirectory =
		FileUtilities.createTempDirectory(TEMPORARY_WORKING_DIRECTORY_PREFIX);

	public RInstance(String rHome, LogService logger) {
		this.rHome = rHome;

		/* TODO Document what we're doing here.  a start:
		 * 1: Run R, feeding it the definitions in makejava.r.  Flag it to save the session we just
		 * 		made those definitions into, then stop R.
		 * 2: ?
		 */
		try {
			RStreamLog messagesFromPreparingR = reportOutputFromProcess(
				installMakeJavaRModule(), false);
			messagesFromPreparingR.log(logger, false, true);
		} catch (Exception e) {
			logger.log(LogService.LOG_ERROR, StringUtilities.getStackTraceAsString(e));
		}
	}

	// Preparation.

	public Process installMakeJavaRModule()
			throws IOException {
		String rPath = String.format(
			"%s%s%s", this.rHome, File.separator, RProperties.R_EXECUTABLE_BASE_NAME);
		File temporaryMakeJava_RFile = File.createTempFile(RProperties.MAKE_JAVA_FILE_NAME, RProperties.MAKE_JAVA_FILE_NAME_EXTENSION, temporaryWorkingDirectory);

		FileUtilities.writeStreamToFile(RInstance.class.getResourceAsStream(RProperties.MAKE_JAVA_PATH), temporaryMakeJava_RFile);
		List<String> command = Arrays.asList(
			rPath,
			RProperties.R_SAVE_ENVIRONMENT_COMMAND_LINE_SWITCH,
			RProperties.R_FILE_COMMAND_LINE_SWITCH,
			temporaryMakeJava_RFile.getAbsolutePath());

		ProcessBuilder rScriptProcessBuilder = new ProcessBuilder(command);
		rScriptProcessBuilder.directory(temporaryWorkingDirectory);

		return rScriptProcessBuilder.start();
	}

	public RStreamLog runRGUI() throws IOException {
		String rGUIPath = String.format(
			"%s%s%s", this.rHome, File.separator, RProperties.R_GUI_EXECUTABLE_BASE_NAME);
		
		String rDataPath = String.format(
			"%s%s%s",
			this.temporaryWorkingDirectory.getAbsolutePath(),
			File.separator,
			RProperties.R_DATA_FILE_NAME);
		
		List<String> command =
			Arrays.asList(rGUIPath, rDataPath, RProperties.R_SAVE_ENVIRONMENT_COMMAND_LINE_SWITCH);

		ProcessBuilder rGUIProcessBuilder = new ProcessBuilder(command);
		rGUIProcessBuilder.directory(this.temporaryWorkingDirectory);

		return reportOutputFromProcess(rGUIProcessBuilder.start(), false);
	}

	// Get stuff out of R.

	public Object getObjectFromR(String rExpression, String javaVariableName) throws IOException {
		String rPath = String.format(
			"%s%s%s", this.rHome, File.separator, RProperties.R_EXECUTABLE_BASE_NAME);
		String rDataPath = String.format(
			"%s%s%s",
			this.temporaryWorkingDirectory.getAbsolutePath(),
			File.separator,
			RProperties.R_DATA_FILE_NAME);
		String fullRExpression = String.format(
			"cat(createJavaCodeForObject(%s, '%s'));q()", rExpression, javaVariableName);
		List<String> command = Arrays.asList(
			rPath,
			RProperties.R_SLAVE_COMMAND_LINE_SWITCH,
			rDataPath,
			RProperties.R_EXPRESSION_COMMAND_LINE_SWITCH,
			fullRExpression);

		ProcessBuilder rProcessBuilder = new ProcessBuilder(command);
		rProcessBuilder.directory(temporaryWorkingDirectory);

		Process rProcess = rProcessBuilder.start();
		JavaCodeStreamReader javaCodeReader =
			new JavaCodeStreamReader(rProcess.getInputStream(), this.javaInterpreter);
		javaCodeReader.start();

		try {
			javaCodeReader.join();
			rProcess.waitFor();

			return this.javaInterpreter.eval(javaVariableName);
		} catch (EvalError e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public Double getDoubleFromR(String rExpression, String javaVariableName) throws IOException {
		return (Double) getObjectFromR(rExpression, javaVariableName);
	}

	public List<Double> getDoubleListFromR(String rExpression, String javaVariableName)
			throws IOException {
		return Arrays.asList((Double[]) getObjectFromR(rExpression, javaVariableName));
	}

	public String getStringFromR(String rExpression, String javaVariableName) throws IOException {
		return (String) getObjectFromR(rExpression, javaVariableName);
	}

	public List<String> getStringListFromR(String rExpression, String javaVariableName)
			throws IOException {
		return Arrays.asList((String[]) getObjectFromR(rExpression, javaVariableName));
	}

	public Set<String> getAllObjectNamesFromR() throws IOException {
		return Sets.newHashSet(getStringListFromR("objects()", "objects").iterator());
	}

	// Put stuff into R.

	public RStreamLog importTable(File tableFile, boolean hasHeader, String rVariableName)
			throws FileCopyingException, IOException {
		File localTableFile = File.createTempFile(tableFile.getName() + TEMPORARY_IMPORT_TABLE_PREFIX, CSV_FILE_EXTENSION, this.temporaryWorkingDirectory) ;
		FileUtilities.copyFile(tableFile, localTableFile);

		String hasHeaderString = new Boolean(hasHeader).toString().toUpperCase();
		/* TODO Examine R's behavior for different values of fill and strip.white.
		 * What best fits our needs?
		 */
		String rCode = String.format(
			"%s <- read.table('%s', header=%s, fill=TRUE, strip.white=TRUE, sep=',')",
			rVariableName,
			localTableFile.getName(),
			hasHeaderString);

		return executeArbitaryRCode(rCode);
	}

	// Get Stuff out of R.

	public RFileExportLog exportTable(String rVariableName) throws IOException {		
		File tempFile = File.createTempFile(rVariableName + TEMPORARY_EXPORT_TABLE_PREFIX, CSV_FILE_EXTENSION, this.temporaryWorkingDirectory);
		String temporaryTableFileName = tempFile.getName();
		
		String rCode = String.format(
			"write.csv(%s, file='%s', sep=',', quote=TRUE, row.names=FALSE)",
			rVariableName,
			temporaryTableFileName);
		
		RStreamLog output = executeArbitaryRCode(rCode);
		
		/* TODO Check a file exists at fullWrittenFilePath (tempFile) and if not, tell the user nicely that R messed up */
		return new RFileExportLog(output, tempFile);
	}

	// Miscellaneous/Utilities.

	public RStreamLog executeArbitaryRCode(String rCode) throws IOException {
		String rPath = String.format(
			"%s%s%s", this.rHome, File.separator, RProperties.R_EXECUTABLE_BASE_NAME);
		String rDataPath = String.format(
			"%s%s%s",
			this.temporaryWorkingDirectory.getAbsolutePath(),
			File.separator,
			RProperties.R_DATA_FILE_NAME);
		List<String> command = Arrays.asList(
			rPath,
			rDataPath,
			RProperties.R_SAVE_ENVIRONMENT_COMMAND_LINE_SWITCH,
			RProperties.R_EXPRESSION_COMMAND_LINE_SWITCH,
			rCode);

		ProcessBuilder rProcessBuilder = new ProcessBuilder(command);
		rProcessBuilder.directory(temporaryWorkingDirectory);

		return reportOutputFromProcess(rProcessBuilder.start(), false);
	}

	public static RStreamLog reportOutputFromProcess(
			Process process, boolean shouldPrintImmediately) {
		try {
			ROutputStreamReader standardOut =
        		new ROutputStreamReader(process.getInputStream(), false, shouldPrintImmediately);
			ROutputStreamReader standardError =
				new ROutputStreamReader(process.getErrorStream(), true, shouldPrintImmediately);

			standardOut.start();
        	standardError.start();

        	process.waitFor();

        	return new RStreamLog(standardOut.getOutput(), standardError.getOutput());
		} catch (Exception e) {
			// TODO: Throw a more relevant exception here. !
			e.printStackTrace();

			return new RStreamLog("", "");
		}
	}
}