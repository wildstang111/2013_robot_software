<#assign licenseFirst = "/*">
<#assign licensePrefix = " * ">
<#assign licenseLast = " */">
<#include "../Licenses/license-${project.license}.txt">

<#if package?? && package != "">
package ${package};

</#if>
import com.wildstangs.autonomous.steps.WsAutonomousStepGroup;
/**
 *
 * @author ${user}
 */
public class ${name} extends WsAutonomousStepGroup
{
    
    public ${name}()
    {
        
    }
    public void defineSteps()
    {
        steps[0] = new WsAutonomousStep
    }
    public String toString()
    {
        return 
    }
}
