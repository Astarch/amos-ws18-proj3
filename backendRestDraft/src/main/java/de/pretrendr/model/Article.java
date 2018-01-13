package de.pretrendr.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "logstash-2018.01.11", type = "csv")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
	@Id
	private String globaleventid;
	private String sqldate;
	private String monthyear;
	private String year;
	private String fractiondate;
	private String actor1code;
	private String actor1name;
	private String actor1countrycode;
	private String actor1knowngroupcode;
	private String actor1ethniccode;
	private String actor1religion1code;
	private String actor1religion2code;
	private String actor1type1code;
	private String actor1type2code;
	private String actor1type3code;
	private String actor2code;
	private String actor2name;
	private String actor2countrycode;
	private String actor2knowngroupcode;
	private String actor2ethniccode;
	private String actor2religion1code;
	private String actor2religion2code;
	private String actor2type1code;
	private String actor2type2code;
	private String actor2type3code;
	private String isrootevent;
	private String eventcode;
	private String eventbasecode;
	private String eventrootcode;
	private String quadclass;
	private String goldsteinscale;
	private String nummentions;
	private String numsources;
	private String numarticles;
	private String avgtone;
	private String actor1geo_type;
	private String actor1geo_fullname;
	private String actor1geo_countrycode;
	private String actor1geo_adm1code;
	private String actor1geo_lat;
	private String actor1geo_long;
	private String actor1geo_featureid;
	private String actor2geo_type;
	private String actor2geo_fullname;
	private String actor2geo_countrycode;
	private String actor2geo_adm1code;
	private String actor2geo_lat;
	private String actor2geo_long;
	private String actor2geo_featureid;
	private String actiongeo_type;
	private String actiongeo_fullname;
	private String actiongeo_countrycode;
	private String actiongeo_adm1code;
	private String actiongeo_lat;
	private String actiongeo_long;
	private String actiongeo_featureid;
	private String dateadded;
	private String sourceurl;
}
