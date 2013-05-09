package com.wildstangs.subsystems;

import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.Compressor;

/**
 *
 * @author Liam
 */
public class WsCompressor extends WsSubsystem {

    Compressor compressor;

    public WsCompressor(String name, int pressureSwitchSlot, int pressureSwitchChannel, int compresssorRelaySlot, int compressorRelayChannel) {
        super(name);
        compressor = new Compressor(pressureSwitchSlot, pressureSwitchChannel, compresssorRelaySlot, compressorRelayChannel);
        compressor.start();
    }

    public void init() {
    }

    public void update() {
    }

    public void notifyConfigChange() {
    }
}
