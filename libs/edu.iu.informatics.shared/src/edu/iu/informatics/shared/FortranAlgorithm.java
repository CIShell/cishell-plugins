/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu/).
 * 
 * Created on Oct 12, 2005 at Indiana University.
 */
package edu.iu.informatics.shared;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.iu.iv.core.IVC;
import edu.iu.iv.core.algorithm.AbstractAlgorithm;
import edu.iu.iv.core.datamodels.DataModel;
import edu.iu.iv.core.persistence.BasicFileResourceDescriptor;
import edu.iu.iv.core.persistence.FileResourceDescriptor;
import edu.iu.iv.core.persistence.PersistenceException;
import edu.iu.iv.core.persistence.Persister;
import edu.iu.iv.core.util.staticexecutable.StaticExecutableRunner;

/**
 * Model and analysis fortran algorithms common methods
 * 
 * @author Team NWB
 */
public abstract class FortranAlgorithm extends AbstractAlgorithm {
    protected DataModel dm;
    
    public FortranAlgorithm(DataModel dm) {
        this.dm = dm;
    }

    /**
     * @see edu.iu.iv.core.algorithm.AbstractAlgorithm#execute()
     */
    public abstract boolean execute();

    /**
     * Saves the datamodel to disk for the static executable runner
     * @param runner Instance of StaticExecutableRunner
     * @param fileName File name to save it as
     * @param p The persister that will be able to write the model
     */
    protected void saveDataModel(StaticExecutableRunner runner,
			                     String fileName, 
			                     Persister p) {
    	//Set the parent of any output for this algorithm
    	runner.setParentDataModel(dm);
    	
    	//Get the temporary directory
		String file = runner.getTempDirectory().getPath() + File.separator
				+ fileName;
		
		
		FileResourceDescriptor frd = new BasicFileResourceDescriptor(
				new File(file));
		try {
			if (p == null) {
				IVC.getInstance().getPersistenceRegistry().save(dm.getData(),
						frd);
			} else {
				p.persist(dm.getData(), frd);
			}
		} catch (IOException e) {
			throw new Error(e);
		} catch (PersistenceException e) {
			throw new Error(e);
		}

	}

    /**
     * Saves the data model to disk without a specified persister
     * @param runner The static executable that needs the file
     * @param fileName The filename to use
     */
    protected void saveDataModel(StaticExecutableRunner runner, String fileName) {
    	saveDataModel(runner, fileName, null);
    }
    
    /**
     * Builds an input file for the Fortran code that will execute
     * @param runner The StaticExecutableRunner that will execute the Fortran code
     * @param fileName The filename to use for the input file
     * @param parmList Parameters passed to the fortran program
     * @param parmListAtBegin True when the parameter list is to start with
     */
    protected void makeInputFile(StaticExecutableRunner runner, String fileName, 
    		                     List parmList, boolean parmListAtBegin) {
        try {
            String file = runner.getTempDirectory().getPath() + File.separator + fileName;
            PrintStream out = new PrintStream(new FileOutputStream(file));
            
            if (parmListAtBegin) {
            	writeParameterList(out, parmList);
            	writeParameterMap(out);
            }
            else {
            	writeParameterMap(out);
            	writeParameterList(out, parmList);            	
            }
            
            out.close();
        } catch (FileNotFoundException e) {
            throw new Error(e);
        }
    }
    
    /**
     * Write the parameter list to output specified
     * @param out Where to print the parameters to
     * @param parmList The list of parameters to write
     */
    private void writeParameterList(PrintStream out, List parmList) {
    	Iterator iter = parmList.iterator();
    	
    	while(iter.hasNext()) {
    		out.println(iter.next());
    	}
    }

    /**
     * Writes the parameter map to the inputfile
     * @param out Where to write the parameter map
     */
    private void writeParameterMap(PrintStream out) {
        Iterator iter = parameterMap.getAllKeys();
        
        while (iter.hasNext()) {
            String key = iter.next().toString();
            
            out.println(parameterMap.getTextValue(key));
        }
    }
    
    /**
     * Build the input file for the fortran algorithm
     * @param runner The static executable runner that it will run under
     * @param fileName The filename of the input file
     */
    protected void makeInputFile(StaticExecutableRunner runner, String fileName) {
    	makeInputFile(runner, fileName, new ArrayList(), false);
    }
}