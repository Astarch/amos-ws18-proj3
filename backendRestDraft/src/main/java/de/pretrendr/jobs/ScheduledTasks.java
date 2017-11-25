package de.pretrendr.jobs;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.pretrendr.businesslogic.S3Service;
import lombok.extern.slf4j.Slf4j;

/**
 * Task Scheduler. Used for cronjobs handling the s3 cache update.
 * 
 * @author flmu
 * @author Tristan Schneider
 */
@Component
@Slf4j
public class ScheduledTasks implements ApplicationRunner {

	@Autowired
	public S3Service s3Service;

	@Value("${pretrendr.s3.update.onstart}")
	private boolean updateOnStart;

	/**
	 * Execute jobs after the system started
	 */
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		if (updateOnStart) {
			log.info("Cache update on startup invoked, because config said so.");
			startS3WordCountJob();
		} else {
			log.info("Cache update on startup skipped, because config said so.");
		}
	}

	/**
	 * The jobs will be executed every night at midnight
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void updateCacheAtMidNight() {
		startS3WordCountJob();
	}

	/**
	 * Word count: job to update the database from S3 buckets
	 */
	private void startS3WordCountJob() {

		// Example
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		DateTime start = DateTime.now();

		// update S3 cache
		log.info("Starting cache update. The time is now " + dateFormat.format(new Date()));
		s3Service.updateAllBuckets();
		log.info(MessageFormat.format("Finished cache update after {0} seconds.",
				new Duration(start, DateTime.now()).getStandardSeconds()));
	}

}
