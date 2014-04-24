/*
 * MATLAB Compiler: 4.17 (R2012a)
 * Date: Sat Mar 15 20:24:30 2014
 * Arguments: "-B" "macro_default" "-W" "java:DTA,DTAT" "-T" "link:lib" "-d" "H:\\Program 
 * Files\\MATLAB\\R2012a\\bin\\DTA\\src" "-w" "enable:specified_file_mismatch" "-w" 
 * "enable:repeated_file" "-w" "enable:switch_ignored" "-w" "enable:missing_lib_sentinel" 
 * "-w" "enable:demo_license" "-v" "class{DTAT:H:\\Program 
 * Files\\MATLAB\\R2012a\\bin\\dtaf.m,H:\\Program 
 * Files\\MATLAB\\R2012a\\bin\\dtas.m,H:\\Program Files\\MATLAB\\R2012a\\bin\\dtat.m}" 
 */

package DTA;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class DTAMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "DTA_21AC0209BE29DDF3F7FDC5DD475BDC6E";
    
    /** Component name */
    private static final String sComponentName = "DTA";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(DTAMCRFactory.class)
        );
    
    
    private DTAMCRFactory()
    {
        // Never called.
    }
    
    public static MWMCR newInstance(MWComponentOptions componentOptions) throws MWException
    {
        if (null == componentOptions.getCtfSource()) {
            componentOptions = new MWComponentOptions(componentOptions);
            componentOptions.setCtfSource(sDefaultComponentOptions.getCtfSource());
        }
        return MWMCR.newInstance(
            componentOptions, 
            DTAMCRFactory.class, 
            sComponentName, 
            sComponentId,
            new int[]{7,17,0}
        );
    }
    
    public static MWMCR newInstance() throws MWException
    {
        return newInstance(sDefaultComponentOptions);
    }
}
