package com.wildstangs.outputmanager.outputs;

import com.wildstangs.outputmanager.base.IOutput;
import com.wildstangs.outputmanager.base.IOutputEnum;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;

/**
 *
 * @author Joey
 */
public class WsRelay implements IOutput
{
    private Relay relay;
    public WsRelay(int moduleNumber, int channel, Direction direction)
    {
        relay = new Relay(moduleNumber, channel, direction);
    }
    
    public void set(IOutputEnum key, Object value)
    {
        if(value instanceof Value)
        {
            relay.set((Value) value);
        }
    }

    public Object get(IOutputEnum key)
    {
        return relay;
    }

    public void update()
    {
    }

    public void notifyConfigChange()
    {
    }
}
