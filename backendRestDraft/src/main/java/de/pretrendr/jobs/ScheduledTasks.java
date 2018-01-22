package de.pretrendr.jobs;

import java.text.MessageFormat;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import de.pretrendr.businesslogic.ArticleService;
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
	public ArticleService articleService;

	@Value("${pretrendr.gdelt.update.onstart}")
	private boolean gdeltUpdateOnStart;

	/**
	 * Execute jobs after the system started
	 */
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		if (gdeltUpdateOnStart) {
			log.info("Gdelt cache update on startup invoked, because config said so.");
			startGdeltCrawl();
		} else {
			log.info("Gdelt cache update on startup skipped, because config said so.");
		}

	}

	private void startGdeltCrawl() {
		DateTime start = DateTime.now();

		articleService.crawlData();

		log.info(MessageFormat.format("Finished Gdelt crawl after {0} seconds.",
				new Duration(start, DateTime.now()).getStandardSeconds()));
	}

}
