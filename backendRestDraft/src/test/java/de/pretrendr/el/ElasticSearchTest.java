package de.pretrendr.el;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.assertj.core.util.Lists;
import org.junit.Test;

import de.pretrendr.PretrendrTestBase;
import de.pretrendr.model.Credential;

public class ElasticSearchTest extends PretrendrTestBase {

	@Test
	public void test1() throws Exception {
		String file = "pwlist";
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			int innercount = 0;
			String email;
			String username;
			String platform;
			String password;
			while ((line = br.readLine()) != null && innercount < 500) {
				Pattern pattern = Pattern.compile("(.*?)\\:(.*)");
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					email = matcher.group(1);
					String[] s = matcher.group(1).split("@");
					if (s.length == 2) {
						username = s[0];
						platform = s[1];
					} else {
						username = "";
						platform = "";
						System.out.println("cannot parse: " + line);
					}
					password = matcher.group(2);
					articleService.save(new Credential(email, username, password, platform));
				}
				innercount++;
			}
		}

		// articleService.save(new Credential("index", "refresh", "email", "password",
		// "platform"));
		List<Credential> asd = Lists.newArrayList(articleService.findAll());
		System.out.println(asd);
	}
}
