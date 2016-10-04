package net.movilbox.dcsperu.utils;

import net.movilbox.dcsperu.Entry.ConfigSplash;
import net.movilbox.dcsperu.cnst.Flags;

public class ValidationUtil {

    public static int hasPath(ConfigSplash cs) {
        if (cs.getPathSplash().isEmpty())
            return Flags.WITH_LOGO;
        else
            return Flags.WITH_PATH;
    }
}
