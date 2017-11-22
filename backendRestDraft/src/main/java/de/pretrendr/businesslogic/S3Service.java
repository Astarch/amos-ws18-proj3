package de.pretrendr.businesslogic;

import java.io.IOException;
import java.util.Map;

public interface S3Service {
	public Map<String, Integer> getWordCountMapFromBucketName(String bucket_name) throws IOException;
}
