<#assign licenseFirst = "/*">
<#assign licensePrefix = " * ">
<#assign licenseLast = " */">
<#include "../Licenses/license-${project.license}.txt">

<#if package?? && package != "">
package ${package};

</#if>
import com.wildstangs.autonomous.WsAutonomousProgram;

/**
 *
 * @author ${user}
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class ${name} extends WsAutonomousProgram
{
    public ${name}()
    {
        super();
    }
    public void defineSteps()
    {
        programSteps[0] = new WsAutonomousStep
    }
    
    public String toString()
    {
        return 
    }
}

