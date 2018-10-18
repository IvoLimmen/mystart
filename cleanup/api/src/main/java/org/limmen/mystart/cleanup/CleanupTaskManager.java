package org.limmen.mystart.cleanup;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;

@Slf4j
public class CleanupTaskManager implements Runnable {

  private final CleanupContext context;

  private final AtomicLong errorCount = new AtomicLong();

  private final ExecutorService executorService = Executors.newFixedThreadPool(10);

  private final LinkStorage linkStorage;

  private final AtomicLong removeCount = new AtomicLong();

  private final AtomicLong skippedCount = new AtomicLong();

  private final AtomicLong updatedCount = new AtomicLong();

  private final Long userId;

  private CleanupTaskFactory cleanupTaskFactory;

  public CleanupTaskManager(LinkStorage linkStorage, Long userId, CleanupContext cleanupContext) {
    this.linkStorage = linkStorage;
    this.userId = userId;
    this.context = cleanupContext;
  }

  @Override
  public void run() {
    log.info("Starting cleanup task in background...");

    ServiceLoader<CleanupTaskFactory> serviceLoader = ServiceLoader.load(CleanupTaskFactory.class);
    cleanupTaskFactory = serviceLoader.iterator().next();

    LocalDateTime start = LocalDateTime.now();
    linkStorage.getAll(userId).forEach(this::startCleanupTask);
    Duration duration = Duration.between(start, LocalDateTime.now());
    log.info("Done with cleanup tasks in {}", duration);
    log.info("Total removed={}, updated={}, skipped={}, errors={}",
        removeCount.get(),
        updatedCount.get(),
        skippedCount.get(),
        errorCount.get());
  }

  public void startCleanupTask(Link oldLink) {
    try {
      CleanupResult cleanupResult = executorService.submit(cleanupTaskFactory.newCleanupTask(oldLink, context))
          .get(5 + context.getMaximumTimeoutInSeconds(), TimeUnit.SECONDS);

      Link link = cleanupResult.getLink();

      switch (cleanupResult.getCleanupResultType()) {
        case DELETE:
          log.info("Removing {}, reason: {}", link.getUrl(), cleanupResult.getReason());
          removeCount.incrementAndGet();
          linkStorage.remove(userId, link.getId());
          break;
        case UPDATE:
          log.info("Updating {}, reason: {}", link.getUrl(), cleanupResult.getReason());
          updatedCount.incrementAndGet();
          linkStorage.update(userId, cleanupResult.getLink());
          break;
        default:
          skippedCount.incrementAndGet();
          log.info("Skipping {}, reason: {}", link.getUrl(), cleanupResult.getReason());
      }
    } catch (InterruptedException | ExecutionException ex) {
      log.error("Failed to execute task", ex);
      errorCount.incrementAndGet();
    } catch (TimeoutException ex) {
      log.error("Failed to execute task for URL: {}", oldLink.getUrl(), ex);
      errorCount.incrementAndGet();
    }
  }
}
