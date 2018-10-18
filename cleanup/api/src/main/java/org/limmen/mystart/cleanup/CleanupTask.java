package org.limmen.mystart.cleanup;

import java.util.concurrent.Callable;

public interface CleanupTask extends Callable<CleanupResult> {
}
