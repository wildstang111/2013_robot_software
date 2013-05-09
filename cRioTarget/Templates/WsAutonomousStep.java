<#assign licenseFirst = "/*">
<#assign licensePrefix = " * ">
<#assign licenseLast = " */">
<#include "../Licenses/license-${project.license}.txt">

<#if package?? && package != "">
package ${package};

</#if>
import com.wildstangs.autonomous.WsAutonomousStep;

/**
 *
 * @author ${user}
 */
public class ${name} extends WsAutonomousStep 
{
    
    public ${name}()
    {
        
    }

    public void initialize()
    {
        
    }

    public void update()
    {
        
    }

    public String toString()
    {
        return 
    }
}
