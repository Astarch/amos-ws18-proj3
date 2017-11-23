package de.pretrendr.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Slf4j
public class ScheduledTasks  implements ApplicationRunner {
	
	/**
	 * Execute jobs after the system started
	 */
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		startS3WordCountJob();
	}
	
	/**
	 * The jobs will be executed every night at midnight 
	 */
	@Scheduled(cron="0 0 0 * * ?")
    public void reportCurrentTime() {
		startS3WordCountJob();
    }
	
	/**
	 * Word count: job to update the database from S3 buckets
	 */
	private void startS3WordCountJob(){
		//TODO start job
		
		//Example
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		log.info("The time is now " + dateFormat.format(new Date()));
	}

	
}
