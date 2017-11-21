package de.pretrendr.businesslogic;

import java.io.IOException;
import java.util.Map;

public interface S3Service {
	public Map<String, Integer> getWorkdCountMapFromBucketName(String bucket_name) throws IOException;
}
